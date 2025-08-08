package org.example.hrs.src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO {
    private static final String TABLE_NAME = "rooms";

    /**
     * Saves a room to the database (inserts new or updates existing)
     * @param room The room to save
     * @return true if operation succeeded, false otherwise
     */
    public static boolean saveRoom(Room room) {
        String sql = "INSERT INTO " + TABLE_NAME + " (room_number, type, price_per_night, is_available) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "type = VALUES(type), " +
                "price_per_night = VALUES(price_per_night), " +
                "is_available = VALUES(is_available)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room.getRoomNumber());
            stmt.setString(2, room.getType().name());
            stmt.setDouble(3, room.getPricePerNight());
            stmt.setBoolean(4, room.isAvailable());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error saving room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads all rooms from the database
     * @return List of all rooms
     */
    public static List<Room> loadAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading rooms: " + e.getMessage());
        }

        return rooms;
    }

    /**
     * Finds a room by room number
     * @param roomNumber The room number to search for
     * @return Optional containing the room if found
     */
    public static Optional<Room> findRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE room_number = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding room: " + e.getMessage());
        }

        return Optional.empty();
    }



    public static boolean updateRoom(Room room) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE rooms SET is_available = ? WHERE room_number = ?")) {

            statement.setBoolean(1, room.isAvailable());
            statement.setInt(2, room.getRoomNumber());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a room from the database
     * @param roomNumber The room number to delete
     * @return true if deletion succeeded
     */
    public static boolean deleteRoom(int roomNumber) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE room_number = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomNumber);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets all available rooms
     * @return List of available rooms
     */
    public static List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE is_available = TRUE";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading available rooms: " + e.getMessage());
        }

        return rooms;
    }

    /**
     * Helper method to map ResultSet to Room object
     */
    private static Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("room_number"),
                Room.roomType.valueOf(rs.getString("type")),
                rs.getDouble("price_per_night"),
                rs.getBoolean("is_available")
        );
    }
}