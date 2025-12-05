package com.flightapp.repository;

import com.flightapp.request.Flight;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FlightRepository extends ReactiveCrudRepository<Flight, Long> {
    Flux<Flight> findByOrigin(String origin);
    Flux<Flight> findByDestination(String destination);
    Flux<Flight> findByOriginAndDestination(String origin, String destination);
}