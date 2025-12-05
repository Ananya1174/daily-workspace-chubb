package com.flightapp.service;

import com.flightapp.request.Flight;
import com.flightapp.repository.FlightRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightService {
    private final FlightRepository repo;

    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }

    public Flux<Flight> getAll() {
        return repo.findAll();
    }

    public Mono<Flight> getById(Long id) {
        return repo.findById(id);
    }

    public Mono<Flight> create(Flight flight) {
        // validation: origin != destination
        if (flight.getOrigin() != null && flight.getOrigin().equalsIgnoreCase(flight.getDestination())) {
            return Mono.error(new IllegalArgumentException("Origin and destination cannot be the same"));
        }
        // validate tripType exists
        if (flight.getTripType() == null) {
            return Mono.error(new IllegalArgumentException("TripType must be ONE_WAY or ROUND_TRIP"));
        }
        return repo.save(flight);
    }

    public Mono<Void> delete(Long id) {
        // if you want hard delete: return repo.deleteById(id)
        // but app requires history of deleted tickets, so flight delete can be hard or restricted.
        return repo.deleteById(id);
    }
}