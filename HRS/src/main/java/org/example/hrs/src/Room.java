package org.example.hrs.src;

/**
 * Represents a hotel room with attributes such as room number, type,
 * availability status, and price per night.
 */
public class Room {

    /**
     * Enum to represent the type of room.
     */
    public enum roomType {
        STANDARD("Standard"),
        DELUXE("Deluxe"),
        SUITE("Suite");

        private final String description;

        /**
         * Constructs a room type with a description.
         *
         * @param description Description of the room type.
         */
        roomType(String description) {
            this.description = description;
        }

        /**
         * Returns the description of the room type.
         *
         * @return Room type description.
         */
        public String getDescription() {
            return description;
        }
    }

    private int roomNumber;
    private roomType type;
    private double pricePerNight;
    private boolean isAvailable;

    /**
     * Constructs a Room with specified details.
     *
     * @param roomNumber     Room number.
     * @param type           Type of the room (STANDARD, DELUXE, SUITE).
     * @param pricePerNight  Price per night for the room.
     * @param isAvailable    Availability status of the room.
     */
    public Room(int roomNumber, roomType type, double pricePerNight, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.isAvailable = isAvailable;
        this.pricePerNight = pricePerNight;
    }

    /**
     * Checks if the room is available.
     *
     * @return true if the room is available, false otherwise.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Gets the room number.
     *
     * @return Room number.
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * Gets the price per night.
     *
     * @return Price per night.
     */
    public double getPricePerNight() {
        return pricePerNight;
    }

    /**
     * Gets the room type.
     *
     * @return Type of the room.
     */
    public roomType getType() {
        return type;
    }

    /**
     * Sets the availability of the room.
     *
     * @param available true to mark the room as available, false otherwise.
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Sets the room number.
     *
     * @param roomNumber New room number.
     */
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Sets the price per night.
     *
     * @param pricePerNight New price per night.
     */
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    /**
     * Sets the room type.
     *
     * @param type New room type.
     */
    public void setType(roomType type) {
        this.type = type;
    }
}
