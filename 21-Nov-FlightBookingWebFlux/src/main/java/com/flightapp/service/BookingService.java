package com.flightapp.service;

import com.flightapp.request.BookingResponse;
import com.flightapp.request.CreateBookingRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingService {
    Mono<BookingResponse> createBooking(CreateBookingRequest req);
    Mono<BookingResponse> getByPnr(String pnr);
    Flux<BookingResponse> getByUserEmail(String email);
    Mono<Void> cancelBooking(String pnr, String requesterEmail); // soft-cancel
    Mono<Void> DeleteBooking(Long bookingId); // soft-delete
}