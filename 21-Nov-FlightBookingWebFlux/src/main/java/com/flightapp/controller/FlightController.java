package com.flightapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.request.Flight;
import com.flightapp.service.FlightService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService service;
    public FlightController(FlightService service) { this.service = service; }

    @GetMapping
    public Flux<Flight> list() {
        return service.getAll(); // 200 OK by default
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Flight>> get(@PathVariable Long id) {
        return service.getById(id)
                .map(f -> ResponseEntity.ok(f))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Flight>> create(@Valid @RequestBody Mono<Flight> flightMono) {
        return flightMono
                .flatMap(service::create)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
        return service.delete(id)
                .then(Mono.just(ResponseEntity.noContent().header("X-Message", "Deleted successfully").build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }
}