package com.flightapp.service;

import com.flightapp.repository.FlightRepository;
import com.flightapp.request.Flight;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightServiceImpl {

    private final FlightRepository flightRepo;

    public FlightServiceImpl(FlightRepository flightRepo) {
        this.flightRepo = flightRepo;
    }

    public Flux<Flight> getAllFlights() {
        return flightRepo.findAll();
    }

    public Mono<Flight> getById(Long id) {
        return flightRepo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Flight not found: " + id)));
    }

    public Mono<Flight> createFlight(Flight flight) {

        // Validate: origin must not be the same as destination
        if (flight.getOrigin().equalsIgnoreCase(flight.getDestination())) {
            return Mono.error(new IllegalArgumentException("Origin and destination cannot be the same"));
        }

        // Validate trip type
        if (flight.getTripType() == null) {
            return Mono.error(new IllegalArgumentException("TripType must be ONE_WAY or ROUND_TRIP"));
        }

        // Set available seats on creation
        flight.setAvailableSeats(flight.getTotalSeats());

        return flightRepo.save(flight);
    }

    public Mono<Void> deleteFlight(Long id) {
        return flightRepo.deleteById(id);
    }

    /**
     * Basic search using existing reactive repository methods.
     */
    public Flux<Flight> searchFlights(String origin, String destination) {
        return flightRepo.findByOriginAndDestination(origin, destination)
                .switchIfEmpty(Flux.empty());
    }

    /**
     * If you want to filter by time window (optional)
     */
    public Flux<Flight> searchFlightsByTime(String origin, String destination) {
        return flightRepo.findByOriginAndDestination(origin, destination)
                .filter(f -> f.getDepartureTime() != null);
    }
}