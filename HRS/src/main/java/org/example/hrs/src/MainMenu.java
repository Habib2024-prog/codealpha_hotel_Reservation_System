package org.example.hrs.src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
/**
 * MainMenu - The primary GUI interface for the Hotel Reservation System
 *
 * This class provides a graphical user interface with these functions:
 * - Book rooms
 * - Cancel bookings
 * - View room availability
 * - View all bookings
 * - Process payments
 *
 * Features:
 * - CardLayout for switching between different views
 * - Input validation for dates and payments
 * - Real-time updates of room/booking status
 * - Responsive design with clear navigation
 *
 * Usage:
 * The main() method launches the application window.
 * All hotel data is loaded from database on startup
 * and saved when exiting.
 */
public class MainMenu {
    // Hotel instance that manages all data
    private static Hotel hotel;

    // Main application window
    private static JFrame mainFrame;

    // CardLayout components for view switching
    private static JPanel cardPanel;
    private static CardLayout cardLayout;
    /**
     * Application entry point
     */
    public static void main(String[] args) {
        hotel = new Hotel();
        hotel.loadFromDatabase();

        // Set up the main frame
        mainFrame = new JFrame("🏨 Hotel Reservation System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        // Create card layout for different views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create menu panel
        JPanel menuPanel = createMenuPanel();
        cardPanel.add(menuPanel, "Menu");

        // Add other panels
        cardPanel.add(createBookRoomPanel(), "BookRoom");
        cardPanel.add(createCancelBookingPanel(), "CancelBooking");
        cardPanel.add(createViewRoomsPanel(), "ViewRooms");
        cardPanel.add(createViewBookingsPanel(), "ViewBookings");
        cardPanel.add(createProcessPaymentPanel(), "ProcessPayment");

        mainFrame.add(cardPanel);
        mainFrame.setVisible(true);
    }
    /**
     * Creates the main menu panel with navigation buttons
     */
    private static JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton bookRoomBtn = new JButton("1. Book a Room");
        JButton cancelBookingBtn = new JButton("2. Cancel Booking");
        JButton viewRoomsBtn = new JButton("3. View All Rooms");
        JButton viewBookingsBtn = new JButton("4. View All Bookings");
        JButton processPaymentBtn = new JButton("5. Process Payment");
        JButton exitBtn = new JButton("6. Exit");

        // Style buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        bookRoomBtn.setFont(buttonFont);
        cancelBookingBtn.setFont(buttonFont);
        viewRoomsBtn.setFont(buttonFont);
        viewBookingsBtn.setFont(buttonFont);
        processPaymentBtn.setFont(buttonFont);
        exitBtn.setFont(buttonFont);

        bookRoomBtn.addActionListener(e -> cardLayout.show(cardPanel, "BookRoom"));
        cancelBookingBtn.addActionListener(e -> {
            refreshBookingsTable();
            cardLayout.show(cardPanel, "CancelBooking");
        });
        viewRoomsBtn.addActionListener(e -> {
            refreshRoomsTable();
            cardLayout.show(cardPanel, "ViewRooms");
        });
        viewBookingsBtn.addActionListener(e -> {
            refreshBookingsTable();
            cardLayout.show(cardPanel, "ViewBookings");
        });
        processPaymentBtn.addActionListener(e -> {
            refreshBookingsTable();
            cardLayout.show(cardPanel, "ProcessPayment");
        });
        exitBtn.addActionListener(e -> {
            hotel.saveToDatabase();
            mainFrame.dispose();
        });

        panel.add(bookRoomBtn);
        panel.add(cancelBookingBtn);
        panel.add(viewRoomsBtn);
        panel.add(viewBookingsBtn);
        panel.add(processPaymentBtn);
        panel.add(exitBtn);

        return panel;
    }
    /**
     * Creates the room booking panel with:
     * - Date selection
     * - Guest name input
     * - Available room listing
     * - Booking confirmation
     */

    private static JPanel createBookRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel checkInLabel = new JLabel("Check-in Date (YYYY-MM-DD):");
        JTextField checkInField = new JTextField();
        JLabel checkOutLabel = new JLabel("Check-out Date (YYYY-MM-DD):");
        JTextField checkOutField = new JTextField();
        JLabel guestNameLabel = new JLabel("Guest Name:");
        JTextField guestNameField = new JTextField();

        formPanel.add(checkInLabel);
        formPanel.add(checkInField);
        formPanel.add(checkOutLabel);
        formPanel.add(checkOutField);
        formPanel.add(guestNameLabel);
        formPanel.add(guestNameField);

        // Room selection table
        DefaultTableModel roomsModel = new DefaultTableModel(new Object[]{"Room No.", "Type", "Price/Night"}, 0);
        JTable roomsTable = new JTable(roomsModel);
        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomsTable.setRowHeight(30);
        JScrollPane roomsScrollPane = new JScrollPane(roomsTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton searchBtn = new JButton("Search Available Rooms");
        JButton bookBtn = new JButton("Book Selected Room");
        JButton backBtn = new JButton("Back to Menu");

        searchBtn.addActionListener(e -> {
            try {
                LocalDate checkIn = LocalDate.parse(checkInField.getText());
                LocalDate checkOut = LocalDate.parse(checkOutField.getText());

                if (checkOut.isBefore(checkIn.plusDays(1))) {
                    JOptionPane.showMessageDialog(panel, "Error: Check-out date must be after check-in date.");
                    return;
                }

                List<Room> availableRooms = hotel.getAvailableRoom(checkIn, checkOut);
                roomsModel.setRowCount(0);

                if (availableRooms.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "No rooms available for these dates.");
                } else {
                    for (Room room : availableRooms) {
                        roomsModel.addRow(new Object[]{
                                room.getRoomNumber(),
                                room.getType(),
                                String.format("$%.2f", room.getPricePerNight())
                        });
                    }
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid date format. Please use YYYY-MM-DD.");
            }
        });

        bookBtn.addActionListener(e -> {
            int selectedRow = roomsTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a room to book.");
                return;
            }

            if (guestNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter guest name.");
                return;
            }

            try {
                LocalDate checkIn = LocalDate.parse(checkInField.getText());
                LocalDate checkOut = LocalDate.parse(checkOutField.getText());

                int roomNumber = (Integer) roomsTable.getValueAt(selectedRow, 0);
                Optional<Room> selectedRoom = hotel.getAllRoom().stream()
                        .filter(r -> r.getRoomNumber() == roomNumber)
                        .findFirst();

                if (selectedRoom.isPresent()) {
                    String bookingID = hotel.generateConfirmationNumber();
                    if (hotel.bookRoom(selectedRoom.get(), guestNameField.getText(), bookingID, checkIn, checkOut)) {
                        JOptionPane.showMessageDialog(panel,
                                "Booking created successfully!\nBooking ID: " + bookingID +
                                        "\nTotal cost:$"+hotel.calculateTotalPrice(selectedRoom.get(),checkIn,checkOut)+ "\nPayment required to confirm.");
                        checkInField.setText("");
                        checkOutField.setText("");
                        guestNameField.setText("");
                        roomsModel.setRowCount(0);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to create booking. Please try again.");
                    }
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid date format. Please use YYYY-MM-DD.");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));

        buttonPanel.add(searchBtn);
        buttonPanel.add(bookBtn);
        buttonPanel.add(backBtn);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(roomsScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    /**
     * Creates the booking cancellation panel with:
     * - List of all bookings
     * - Cancellation function
     * - Refund calculation
     */
    private static JPanel createCancelBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Bookings table
        String[] bookingsColumns = {"Booking ID", "Guest", "Room", "Check-In", "Check-Out", "Status", "Payment"};
        DefaultTableModel bookingsModel = new DefaultTableModel(bookingsColumns, 0);
        JTable bookingsTable = new JTable(bookingsModel);
        bookingsTable.setRowHeight(25);
        JScrollPane bookingsScrollPane = new JScrollPane(bookingsTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton cancelBtn = new JButton("Cancel Selected Booking");
        JButton backBtn = new JButton("Back to Menu");
        JButton refreshBtn = new JButton("Refresh");

        cancelBtn.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a booking to cancel.");
                return;
            }

            String bookingId = (String) bookingsTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to cancel booking " + bookingId + "?",
                    "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (hotel.cancelBook(bookingId)) {
                    JOptionPane.showMessageDialog(panel, "Booking cancelled successfully\nRefund Amount:$"
                            +hotel.calculateRefundAmount(hotel.findById(bookingId)));
                    // Refresh both bookings and rooms views
                    refreshBookingsTable();
                    refreshRoomsTable();
                } else {
                    JOptionPane.showMessageDialog(panel, "Cancellation failed. Please check the booking ID.");
                }
            }
        });

        refreshBtn.addActionListener(e -> refreshBookingsTable());
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));

        buttonPanel.add(cancelBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);

        panel.add(new JLabel("All Bookings (select one to cancel):"), BorderLayout.NORTH);
        panel.add(bookingsScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    /**
     * Creates the room viewing panel showing:
     * - Room numbers
     * - Types and prices
     * - Current availability
     */
    private static JPanel createViewRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Rooms table
        String[] roomsColumns = {"Room No.", "Type", "Price/Night", "Status"};
        DefaultTableModel roomsModel = new DefaultTableModel(roomsColumns, 0);
        JTable roomsTable = new JTable(roomsModel);
        roomsTable.setRowHeight(25);
        JScrollPane roomsScrollPane = new JScrollPane(roomsTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back to Menu");

        refreshBtn.addActionListener(e -> refreshRoomsTable());
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));

        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);

        panel.add(new JLabel("All Rooms:"), BorderLayout.NORTH);
        panel.add(roomsScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    /**
     * Creates the booking viewing panel showing:
     * -  booked Room numbers
     * - Types and prices
     * - Current state
     */

    private static JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Bookings table
        String[] bookingsColumns = {"Booking ID", "Guest", "Room", "Check-In", "Check-Out", "Status", "Payment"};
        DefaultTableModel bookingsModel = new DefaultTableModel(bookingsColumns, 0);
        JTable bookingsTable = new JTable(bookingsModel);
        bookingsTable.setRowHeight(25);
        JScrollPane bookingsScrollPane = new JScrollPane(bookingsTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back to Menu");

        refreshBtn.addActionListener(e -> refreshBookingsTable());
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));

        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);

        panel.add(new JLabel("All Bookings:"), BorderLayout.NORTH);
        panel.add(bookingsScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    /**
     * Creates the payment processing panel with:
     * - Booking selection
     * - Payment method input
     * - Payment confirmation
     */

    private static JPanel createProcessPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Bookings table
        String[] bookingsColumns = {"Booking ID", "Guest", "Room", "Check-In", "Check-Out", "Status", "Payment"};
        DefaultTableModel bookingsModel = new DefaultTableModel(bookingsColumns, 0);
        JTable bookingsTable = new JTable(bookingsModel);
        bookingsTable.setRowHeight(25);
        JScrollPane bookingsScrollPane = new JScrollPane(bookingsTable);

        // Payment form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel bookingIdLabel = new JLabel("Booking ID:");
        JTextField bookingIdField = new JTextField();
        JLabel methodLabel = new JLabel("Payment Method (cash/card):");
        JTextField methodField = new JTextField();

        formPanel.add(bookingIdLabel);
        formPanel.add(bookingIdField);
        formPanel.add(methodLabel);
        formPanel.add(methodField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton processBtn = new JButton("Process Payment");
        JButton backBtn = new JButton("Back to Menu");

        processBtn.addActionListener(e -> {
            String bookingId = bookingIdField.getText().trim();
            String method = methodField.getText().trim().toLowerCase();

            if (bookingId.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter a booking ID.");
                return;
            }

            if (!method.equals("cash") && !method.equals("card")) {
                JOptionPane.showMessageDialog(panel, "Payment method must be either 'cash' or 'card'.");
                return;
            }

            if (hotel.processPayment(bookingId, method)) {
                Booking target=hotel.findById(bookingId);
                double amount=hotel.calculateTotalPrice(target.getBookedRoom(),target.getCheckIn(),target.getCheckOut());
                JOptionPane.showMessageDialog(panel, "Payment processed successfully.\nPaidAmount:$"+amount+"\nBooking confirmed!");
                bookingIdField.setText("");
                methodField.setText("");
                refreshBookingsTable();
            } else {
                JOptionPane.showMessageDialog(panel, "Payment failed. Please check booking status.");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));

        buttonPanel.add(processBtn);
        buttonPanel.add(backBtn);

        panel.add(new JLabel("All Bookings (select one to process payment):"), BorderLayout.NORTH);
        panel.add(bookingsScrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Adjust layout
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }
    /**
     * Refreshes the rooms table with current data
     */

    private static void refreshRoomsTable() {
        JPanel viewRoomsPanel = (JPanel) cardPanel.getComponent(3);
        JScrollPane scrollPane = (JScrollPane) viewRoomsPanel.getComponent(1);
        JTable roomsTable = (JTable) scrollPane.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) roomsTable.getModel();

        model.setRowCount(0);
        hotel.getAllRoom().forEach(room -> {
            model.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getType(),
                    String.format("$%.2f", room.getPricePerNight()),
                    room.isAvailable() ? "Available" : "Booked"
            });
        });
    }
    /**
     * Refreshes all booking tables with current data
     */
    private static void refreshBookingsTable() {
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getComponentCount() > 1 && panel.getComponent(1) instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane) panel.getComponent(1);
                    if (scrollPane.getViewport().getView() instanceof JTable) {
                        JTable table = (JTable) scrollPane.getViewport().getView();
                        DefaultTableModel model = (DefaultTableModel) table.getModel();

                        model.setRowCount(0);
                        hotel.getAllBookings().forEach(booking -> {
                            model.addRow(new Object[]{
                                    booking.getBookingId(),
                                    booking.getGustName(),
                                    booking.getBookedRoom().getRoomNumber(),
                                    booking.getCheckIn(),
                                    booking.getCheckOut(),
                                    booking.getStatus(),
                                    booking.getPaymentMethod() != null ? booking.getPaymentMethod() : "Pending"
                            });
                        });
                    }
                }
            }
        }
    }
}