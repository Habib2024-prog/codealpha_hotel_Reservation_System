package org.example.hrs.src;

import java.time.LocalDate;

/**
 * Represents a booking in the Hotel Reservation System,
 * including guest information, room details, booking status, and payment information.
 */
public class Booking {
    private String gustName; // Note: consider renaming to 'guestName' for correct spelling
    private Room bookedRoom;
    private String bookingId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;
    private String paymentMethod;
    private LocalDate paymentDate;

    /**
     * Constructs a new Booking with guest name, room, ID, check-in and check-out dates.
     * Sets the default booking status to "notConfirmed".
     *
     * @param gustName   The name of the guest.
     * @param bookedRoom The room booked.
     * @param bookingId  The unique booking ID.
     * @param checkIn    The check-in date.
     * @param checkOut   The check-out date.
     */
    public Booking(String gustName, Room bookedRoom,
                   String bookingId, LocalDate checkIn, LocalDate checkOut) {
        this.bookedRoom = bookedRoom;
        this.bookingId = bookingId;
        this.gustName = gustName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = "notConfirmed";
    }

    // Getters

    /**
     * Gets the booking ID.
     *
     * @return The booking ID.
     */
    public String getBookingId() {
        return bookingId;
    }

    /**
     * Gets the check-in date.
     *
     * @return The check-in date.
     */
    public LocalDate getCheckIn() {
        return checkIn;
    }

    /**
     * Gets the booked room.
     *
     * @return The booked Room object.
     */
    public Room getBookedRoom() {
        return bookedRoom;
    }

    /**
     * Gets the guest name.
     *
     * @return The name of the guest.
     */
    public String getGustName() {
        return gustName;
    }

    /**
     * Gets the check-out date.
     *
     * @return The check-out date.
     */
    public LocalDate getCheckOut() {
        return checkOut;
    }

    /**
     * Gets the current booking status.
     *
     * @return Booking status (e.g., Confirmed, Cancelled, notConfirmed).
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the payment date.
     *
     * @return The payment date.
     */
    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    /**
     * Gets the payment method used.
     *
     * @return The payment method.
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    // Setters

    /**
     * Sets the booking ID.
     *
     * @param bookingId The unique booking ID.
     */
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Sets the guest name.
     *
     * @param gustName The name of the guest.
     */
    public void setGustName(String gustName) {
        this.gustName = gustName;
    }

    /**
     * Sets the booked room.
     *
     * @param bookedRoom The Room object.
     */
    public void setBookedRoom(Room bookedRoom) {
        this.bookedRoom = bookedRoom;
    }

    /**
     * Sets the check-out date.
     *
     * @param checkOut The new check-out date.
     */
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    /**
     * Sets the check-in date.
     *
     * @param checkIn The new check-in date.
     */
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    /**
     * Sets the status of the booking.
     *
     * @param status New status (e.g., Confirmed, Cancelled).
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the payment date.
     *
     * @param paymentDate The date of payment.
     */
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Sets the payment method.
     *
     * @param paymentMethod The payment method used (e.g., Cash, Credit Card).
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Checks whether the booking is confirmed.
     *
     * @return true if the status is "Confirmed", false otherwise.
     */
    public boolean isConfirmed() {
        return "Confirmed".equalsIgnoreCase(status);
    }

    /**
     * Checks whether the booking is cancelled.
     *
     * @return true if the status is "Cancelled", false otherwise.
     */
    public boolean isCancelled() {
        return "Cancelled".equalsIgnoreCase(status);
    }
}
