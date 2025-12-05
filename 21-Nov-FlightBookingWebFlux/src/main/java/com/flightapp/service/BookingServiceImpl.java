package com.flightapp.service;

import com.flightapp.PnrGenerator;
import com.flightapp.repository.BookingRepository;
import com.flightapp.repository.FlightRepository;
import com.flightapp.request.BookingResponse;
import com.flightapp.request.CreateBookingRequest;
import com.flightapp.request.Booking; // entity
import com.flightapp.request.Flight;   // entity
import com.flightapp.request.BookingStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public Mono<BookingResponse> createBooking(CreateBookingRequest req) {
        // validate seat numbers vs count
        if (req.getSeatNumbers() == null || req.getSeats() == null || req.getSeatNumbers().size() != req.getSeats()) {
            return Mono.error(new IllegalArgumentException("seatNumbers size must equal seats"));
        }
        Long flightId = req.getFlightId();
        if (flightId == null) return Mono.error(new IllegalArgumentException("flightId required"));

        // Check flight exists (reactively)
        return flightRepository.findById(flightId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Flight not found: " + flightId)))
            .flatMap(flight -> {
                // convert requested seatNumbers list to Set to match entity APIs
                Set<Integer> seatSet = new HashSet<>(req.getSeatNumbers());

                // check seat availability
                for (Integer s : seatSet) {
                    if (!flight.isSeatAvailable(s)) {
                        return Mono.error(new IllegalArgumentException("Seat " + s + " not available"));
                    }
                }

                // book seats on flight entity (modify and save)
                flight.bookSeats(seatSet);

                return flightRepository.save(flight)
                        .flatMap(savedFlight -> {
                            Booking booking = new Booking();
                            booking.setUserEmail(req.getUserEmail());
                            booking.setContactName(req.getContactName());
                            booking.setSeatsBooked(req.getSeats());
                            booking.setSeatNumbers(seatSet); // entity expects Set<Integer>
                            // passengers are already PassengerInfo objects
                            booking.setPassengers(req.getPassengers() != null ? req.getPassengers() : List.of());
                            booking.setFlight(savedFlight); // entity holds Flight reference
                            booking.setBookingTime(LocalDateTime.now());
                            booking.setStatus(BookingStatus.ACTIVE);

                            // compute price
                            double price = savedFlight.getPrice() * req.getSeats();

                            if (req.getReturnFlightId() != null) {
                                Long retId = req.getReturnFlightId();
                                return flightRepository.findById(retId)
                                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Return flight not found: " + retId)))
                                        .flatMap(retFlight -> {
                                            booking.setReturnFlight(retFlight);
                                            double total = price + retFlight.getPrice() * req.getSeats();
                                            booking.setTotalPrice(total);

                                            // generate unique PNR reactively, set it, then save booking
                                            return generateUniquePnrMono()
                                                    .flatMap(pnr -> {
                                                        booking.setPnr(pnr);
                                                        return bookingRepository.save(booking)
                                                                .map(BookingResponse::fromEntity);
                                                    });
                                        });
                            } else {
                                booking.setTotalPrice(price);
                                return generateUniquePnrMono()
                                        .flatMap(pnr -> {
                                            booking.setPnr(pnr);
                                            return bookingRepository.save(booking).map(BookingResponse::fromEntity);
                                        });
                            }
                        });
            });
    }

    // Reactive unique PNR generator (no blocking). Recursively tries until unique.
    private Mono<String> generateUniquePnrMono() {
        return Mono.defer(() -> {
            String p = PnrGenerator.generatePnr();
            return bookingRepository.findByPnr(p)
                    .flatMap(existing -> {
                        // already exists -> try again recursively
                        return generateUniquePnrMono();
                    })
                    .switchIfEmpty(Mono.just(p));
        });
    }

    @Override
    public Mono<BookingResponse> getByPnr(String pnr) {
        return bookingRepository.findByPnr(pnr)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Booking not found for PNR: " + pnr)))
                .map(BookingResponse::fromEntity);
    }

    @Override
    public Flux<BookingResponse> getByUserEmail(String email) {
        return bookingRepository.findByUserEmailOrderByBookingDateDesc(email)
                .map(BookingResponse::fromEntity);
    }

    @Override
    public Mono<Void> cancelBooking(String pnr, String requesterEmail) {
        return bookingRepository.findByPnr(pnr)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Booking not found for PNR: " + pnr)))
                .flatMap(b -> {
                    if (!b.getUserEmail().equalsIgnoreCase(requesterEmail)) {
                        return Mono.error(new IllegalArgumentException("Only booking owner can cancel"));
                    }
                    // Ensure we check the flight's departure time (booking stores Flight reference)
                    Flight flightRef = b.getFlight();
                    if (flightRef == null || flightRef.getDepartureTime() == null) {
                        // try to fetch flight from repository as fallback
                        Long fid = flightRef != null ? flightRef.getId() : null;
                        if (fid == null) {
                            return Mono.error(new IllegalArgumentException("Flight info unavailable for cancellation check"));
                        }
                        return flightRepository.findById(fid)
                                .flatMap(f -> validateAndCancel(b, f));
                    } else {
                        return validateAndCancel(b, flightRef);
                    }
                });
    }

    // helper to validate timeframe and perform seat release + update booking status
    private Mono<Void> validateAndCancel(Booking b, Flight f) {
        // cannot cancel within 24 hours of departure
        if (f.getDepartureTime() != null && f.getDepartureTime().minusHours(24).isBefore(LocalDateTime.now())) {
            return Mono.error(new IllegalArgumentException("Cannot cancel within 24 hours of departure"));
        }
        // release seats on flight
        Set<Integer> seatsToRelease = b.getSeatNumbers() != null ? b.getSeatNumbers() : new HashSet<>();
        f.releaseSeats(seatsToRelease);
        return flightRepository.save(f)
                .then(bookingRepository.findById(b.getId())
                        .flatMap(saved -> {
                            saved.setStatus(BookingStatus.CANCELLED);
                            return bookingRepository.save(saved);
                        }))
                .then();
    }

    @Override
    public Mono<Void> DeleteBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Booking not found")))
                .flatMap(b -> {
                    b.setStatus(BookingStatus.DELETED);
                    return bookingRepository.save(b).then();
                });
    }
}