package com.flightapp.controller;

import com.flightapp.request.BookingResponse;
import com.flightapp.service.BookingService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/users")
public class UserController {

    private final BookingService bookingService;

    public UserController(BookingService bookingService) { this.bookingService = bookingService; }

    /**
     * GET /users/{email}/history
     * Returns all bookings for the user (includes CANCELLED and DELETED statuses).
     */
    @GetMapping("/{email}/history")
    public Flux<BookingResponse> history(@PathVariable String email) {
        return bookingService.getByUserEmail(email);
    }
}