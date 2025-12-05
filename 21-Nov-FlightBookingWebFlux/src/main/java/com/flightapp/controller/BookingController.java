package com.flightapp.controller;

import com.flightapp.request.BookingResponse;
import com.flightapp.request.CreateBookingRequest;
import com.flightapp.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    /**
     * Create booking:
     * Request body should be CreateBookingRequest JSON.
     * Returns 201 Created with BookingResponse JSON.
     */
    @PostMapping
    public Mono<ResponseEntity<BookingResponse>> create(@Valid @RequestBody Mono<CreateBookingRequest> reqMono) {
        return reqMono
                .flatMap(service::createBooking)                // returns Mono<BookingResponse>
                .map(resp -> ResponseEntity.status(HttpStatus.CREATED).body(resp))
                .onErrorResume(IllegalArgumentException.class,
                        e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    /**
     * Soft-delete booking by id (keeps history). Returns 204 No Content.
     * Response has X-Message header "Deleted successfully".
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
        return service.DeleteBooking(id)
                .then(Mono.just(
                        ResponseEntity.noContent()
                                .header("X-Message", "Deleted successfully")
                                .build()
                ))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * Cancel booking by PNR. Provide requesterEmail as query param for authorization check.
     * Example: POST /bookings/PNR1234567/cancel?requesterEmail=alice@example.com
     * Returns 200 OK on success.
     */
    @PostMapping("/{pnr}/cancel")
    public Mono<ResponseEntity<Object>> cancel(@PathVariable String pnr,
                                               @RequestParam("requesterEmail") String requesterEmail) {
        return service.cancelBooking(pnr, requesterEmail)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }
}