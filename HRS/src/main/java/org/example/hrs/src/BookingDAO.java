package org.example.hrs.src;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for managing booking data in the database.
 * Provides methods to save and load bookings from the `bookings` table.
 */
public class BookingDAO {

    /**
     * Saves or updates a booking record in the database using the REPLACE INTO SQL command.
     * If the booking already exists (same booking_id), it will be updated.
     *
     * @param booking The {@link Booking} object to save.
     */
    public static void saveBooking(Booking booking) {
        String sql = "REPLACE INTO bookings (booking_id, guest_name, room_number, check_in, check_out, status, payment_method, payment_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, booking.getBookingId());
            stmt.setString(2, booking.getGustName()); // Consider renaming to getGuestName()
            stmt.setInt(3, booking.getBookedRoom().getRoomNumber());
            stmt.setDate(4, Date.valueOf(booking.getCheckIn()));
            stmt.setDate(5, Date.valueOf(booking.getCheckOut()));
            stmt.setString(6, booking.getStatus());
            stmt.setString(7, booking.getPaymentMethod());

            // Save the associated room status to database
            RoomDAO.updateRoom(booking.getBookedRoom());

            if (booking.getPaymentDate() != null) {
                stmt.setDate(8, Date.valueOf(booking.getPaymentDate()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // You can replace with proper logging
        }
    }

    /**
     * Loads all bookings from the database and matches them to rooms using the provided room list.
     *
     * @param roomList List of rooms to associate with bookings.
     * @return List of {@link Booking} objects loaded from the database.
     */
    public static List<Booking> loadAllBookings(List<Room> roomList) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("booking_id");
                String guest = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_number");
                LocalDate checkIn = rs.getDate("check_in").toLocalDate();
                LocalDate checkOut = rs.getDate("check_out").toLocalDate();
                String status = rs.getString("status");
                String paymentMethod = rs.getString("payment_method");
                Date payDateRaw = rs.getDate("payment_date");

                // Match booking to room
                Room room = roomList.stream()
                        .filter(r -> r.getRoomNumber() == roomNumber)
                        .findFirst()
                        .orElse(null);

                if (room == null) continue; // Skip if no matching room found

                Booking booking = new Booking(guest, room, id, checkIn, checkOut);
                booking.setStatus(status);
                booking.setPaymentMethod(paymentMethod);

                if (payDateRaw != null) {
                    booking.setPaymentDate(payDateRaw.toLocalDate());
                }

                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging in production
        }

        return bookings;
    }
}
