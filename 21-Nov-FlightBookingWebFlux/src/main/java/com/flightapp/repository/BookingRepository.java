package com.flightapp.repository;

import com.flightapp.request.Booking;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingRepository extends ReactiveCrudRepository<Booking, Long> {
    Flux<Booking> findByUserEmailOrderByBookingDateDesc(String email);
    Flux<Booking> findByFlightId(Long flightId);
    Mono<Booking> findByPnr(String pnr);
}