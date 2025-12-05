package com.flightapp.request;

import java.util.List;

public class CreateBookingRequest {
    private Long flightId;
    private Integer seats;
    private List<Integer> seatNumbers;
    private String userEmail;
    private String contactName;
    private List<PassengerInfo> passengers;
    private Long returnFlightId; // optional, for ROUND_TRIP

    // getters / setters

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }

    public Integer getSeats() { return seats; }
    public void setSeats(Integer seats) { this.seats = seats; }

    public List<Integer> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<Integer> seatNumbers) { this.seatNumbers = seatNumbers; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public List<PassengerInfo> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerInfo> passengers) { this.passengers = passengers; }

    public Long getReturnFlightId() { return returnFlightId; }
    public void setReturnFlightId(Long returnFlightId) { this.returnFlightId = returnFlightId; }
}