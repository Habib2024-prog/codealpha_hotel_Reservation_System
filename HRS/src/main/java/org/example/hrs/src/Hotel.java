package org.example.hrs.src;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents the core Hotel system containing rooms and bookings,
 * and provides functionality to manage them (e.g., booking, cancellation, availability checking, etc.).
 */
public class Hotel {

    private List<Room> allRoom;
    private List<Booking> allBookings;

    /**
     * Constructs a new Hotel instance and initializes rooms.
     */
    public Hotel() {
        this.allRoom = new ArrayList<>();
        initializeRoom();
        this.allBookings = new ArrayList<>();
    }

    /**
     * Initializes rooms of different types (Standard, Deluxe, Suite) with room numbers and availability.
     */
    public void initializeRoom() {
        for (int i = 100; i <= 124; i++) {
            allRoom.add(new Room(i, Room.roomType.STANDARD, 50, true));
        }
        for (int i = 200; i <= 214; i++) {
            allRoom.add(new Room(i, Room.roomType.DELUXE, 75, true));
        }
        for (int i = 300; i <= 309; i++) {
            allRoom.add(new Room(i, Room.roomType.SUITE, 100, true));
        }
    }

    /**
     * Retrieves a list of available rooms between check-in and check-out dates.
     *
     * @param checkIn  Check-in date.
     * @param checkOut Check-out date.
     * @return List of available rooms.
     */
    public List<Room> getAvailableRoom(LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRoom = new ArrayList<>();
        for (Room room : allRoom) {
            if (room.isAvailable() && !isRoomBooked(checkIn, checkOut, room)) {
                availableRoom.add(room);
            }
        }
        return availableRoom;
    }

    /**
     * Checks if a room is already booked during the given period.
     *
     * @param checkIn  Desired check-in date.
     * @param checkOut Desired check-out date.
     * @param room     The room to check.
     * @return true if the room is booked; false otherwise.
     */
    public boolean isRoomBooked(LocalDate checkIn, LocalDate checkOut, Room room) {
        for (Booking booking : allBookings) {
            if (booking.getBookedRoom().equals(room) &&
                    !((checkOut.isBefore(booking.getCheckIn())) || (checkIn.isAfter(booking.getCheckOut())))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the total price for a stay based on room and duration.
     *
     * @param targetRoom Room being booked.
     * @param checkIn    Check-in date.
     * @param checkOut   Check-out date.
     * @return Total price of the booking.
     */
    public double calculateTotalPrice(Room targetRoom, LocalDate checkIn, LocalDate checkOut) {
        return ChronoUnit.DAYS.between(checkIn, checkOut) * targetRoom.getPricePerNight();
    }

    /**
     * Books a room if available and adds the booking to the system.
     *
     * @param room       Room to book.
     * @param guestName  Name of the guest.
     * @param bookingId  Unique booking ID.
     * @param checkIn    Check-in date.
     * @param checkOut   Check-out date.
     * @return true if booking is successful, false otherwise.
     */
    public boolean bookRoom(Room room, String guestName, String bookingId,
                            LocalDate checkIn, LocalDate checkOut) {

        Room targetRoom = null;
        for (Room r : allRoom) {
            if (r.getRoomNumber() == room.getRoomNumber() && r.getType().equals(room.getType())) {
                targetRoom = r;
                break;
            }
        }

        if (targetRoom == null) {
            System.out.println("Error: Room " + room.getRoomNumber() + " (" + room.getType() + ") doesn't exist");
            return false;
        }

        if (!targetRoom.isAvailable() || isRoomBooked(checkIn, checkOut, targetRoom)) {
            System.out.println("Room " + targetRoom.getRoomNumber() + " (" + targetRoom.getType() + ") is not available");
            return false;
        }

        targetRoom.setAvailable(false);
        allBookings.add(new Booking(guestName, targetRoom, bookingId, checkIn, checkOut));
        return true;
    }

    /**
     * Cancels a booking and updates room availability.
     *
     * @param bookingId ID of the booking to cancel.
     * @return true if cancellation successful; false if booking not found.
     */
    public boolean cancelBook(String bookingId) {
        Optional<Booking> bookingToCancel = allBookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst();

        if (bookingToCancel.isPresent()) {
            Booking booking = bookingToCancel.get();
            booking.setStatus("Cancelled");
            Room room = booking.getBookedRoom();
            room.setAvailable(true);
            RoomDAO.updateRoom(room);
            return true;
        }
        return false;
    }

    /**
     * Processes the payment for a booking.
     *
     * @param bookingId     ID of the booking.
     * @param paymentMethod Payment method used ("Cash" or "Card").
     * @return true if payment is successful; false otherwise.
     */
    public boolean processPayment(String bookingId, String paymentMethod) {
        if (!paymentMethod.equalsIgnoreCase("Cash") && !paymentMethod.equalsIgnoreCase("Card")) {
            System.out.println("Error: Invalid payment method. Use 'cash' or 'card'");
            return false;
        }

        Booking targetBooking = null;
        for (Booking booking : allBookings) {
            if (booking.getBookingId().equals(bookingId)) {
                targetBooking = booking;
                break;
            }
        }

        if (targetBooking == null) {
            System.out.println("Error: Booking " + bookingId + " not found!");
            return false;
        }

        if (!targetBooking.getStatus().equalsIgnoreCase("notConfirmed")) {
            System.out.println("Error: Booking already confirmed or cancelled!");
            return false;
        }

        targetBooking.setStatus("Confirmed");
        targetBooking.setPaymentMethod(paymentMethod);
        targetBooking.setPaymentDate(LocalDate.now());
        return true;
    }

    /**
     * Saves all rooms and bookings to the database using DAO classes.
     */
    public void saveToDatabase() {
        for (Room room : allRoom) {
            RoomDAO.saveRoom(room);
        }
        for (Booking booking : allBookings) {
            BookingDAO.saveBooking(booking);
        }
    }

    /**
     * Loads all rooms and bookings from the database.
     */
    public void loadFromDatabase() {
        this.allRoom = RoomDAO.loadAllRooms();
        this.allBookings = BookingDAO.loadAllBookings(this.allRoom);
    }

    /**
     * Generates a unique 5-character confirmation number.
     *
     * @return Unique confirmation number.
     */
    public String generateConfirmationNumber() {
        return UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    /**
     * Adds a new booking to the system.
     *
     * @param newBooking The new booking to add.
     */
    public void addBooking(Booking newBooking) {
        allBookings.add(newBooking);
    }

    /**
     * Gets all rooms in the hotel.
     *
     * @return List of all Room objects.
     */
    public List<Room> getAllRoom() {
        return allRoom;
    }

    /**
     * Gets all bookings in the hotel.
     *
     * @return List of all Booking objects.
     */
    public List<Booking> getAllBookings() {
        return allBookings;
    }

    /**
     * Calculates refund amount based on current date and booking check-in.
     * Full refund if cancelled more than 2 days before check-in, otherwise 50%.
     *
     * @param booking The booking to calculate refund for.
     * @return Refund amount.
     */
    public double calculateRefundAmount(Booking booking) {
        LocalDate today = LocalDate.now();
        double totalPrice = calculateTotalPrice(booking.getBookedRoom(), booking.getCheckIn(), booking.getCheckOut());
        boolean fullRefund = today.isBefore(booking.getCheckIn().minusDays(2));
        return fullRefund ? totalPrice : totalPrice * 0.5;
    }

    /**
     * Finds a booking by its ID.
     *
     * @param id The booking ID.
     * @return The Booking object if found; null otherwise.
     */
    public Booking findById(String id) {
        for (Booking booking : allBookings) {
            if (booking.getBookingId().equals(id)) {
                return booking;
            }
        }
        return null;
    }
}
