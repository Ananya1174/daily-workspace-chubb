package com.flightapp.request;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("bookings")
public class Booking {
    @Id
    private Long id;
    private Long flightId;
    private Long returnFlightId; // nullable
    private String userEmail;
    private String contactName;
    private String pnr;
    private LocalDateTime bookingTime;
    private Integer seatsBooked;
    private String status; // ACTIVE, CANCELLED, DELETED
    private Double totalPrice;
    // getters & setters
}