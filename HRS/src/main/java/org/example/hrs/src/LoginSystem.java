package org.example.hrs.src;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

/**
 * LoginSystem - Authentication gateway for Hotel Reservation System
 *
 * Provides a secure login interface with:
 * - Role-based access (Admin/Staff)
 * - Modern UI with gradient background
 * - Input validation and placeholder handling
 * - Secure password field
 * Features:
 *
 * - Two predefined user accounts (admin/staff)
 * - Responsive design with image fallback
 * - Interactive form elements
 * - Forgot password assistance
 */
public class LoginSystem {
    // Credential constants
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";
    private static final String STAFF_USER = "staff";
    private static final String STAFF_PASS = "staff123";

    /**
     * Application entry point - launches login window
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowLogin(args));
    }
    /**
     * Creates and displays the login interface
     * @param args Arguments to pass to dashboard after login
     */
    private static void createAndShowLogin(String[] args) {
        // Create main frame
        JFrame frame = new JFrame("Hotel Reservation System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);


        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(23, 107, 135);
                Color color2 = new Color(100, 204, 197);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(400, 0));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Load and scale the image properly
        try {
            // Try loading from resources first
            InputStream imgStream = LoginSystem.class.getResourceAsStream("C:/Users/Habibullah Khaliqyar/Desktop/HRS/Login.jpg");
            if (imgStream != null) {
                ImageIcon originalIcon = new ImageIcon(ImageIO.read(imgStream));
                Image scaledImage = originalIcon.getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                // Fallback to file system path if not found in resources
                String imagePath = "C:/Users/Habibullah Khaliqyar/Desktop/HRS/Login.jpg";
                ImageIcon originalIcon = new ImageIcon(imagePath);
                Image scaledImage = originalIcon.getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            // Fallback design if image can't be loaded
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(255, 255, 255, 50));
            imageLabel.setText("<html><center>HOTEL<br>MANAGEMENT<br>SYSTEM</center></html>");
            imageLabel.setFont(new Font("Arial", Font.BOLD, 24));
            imageLabel.setForeground(Color.WHITE);
            System.err.println("Image loading failed: " + e.getMessage());
        }

        leftPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right panel with login form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 20, 10);

        // Title
        JLabel titleLabel = new JLabel("WELCOME BACK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        rightPanel.add(titleLabel, gbc);

        // Username field
        JTextField usernameField = createStyledTextField("Username");
        gbc.insets = new Insets(0, 10, 15, 10);
        rightPanel.add(usernameField, gbc);

        // Password field
        JPasswordField passwordField = createStyledPasswordField("Password");
        rightPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("LOGIN");
        styleLoginButton(loginButton);
        gbc.insets = new Insets(20, 10, 10, 10);
        rightPanel.add(loginButton, gbc);

        // Forgot password
        JLabel forgotLabel = new JLabel("Forgot password?");
        forgotLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotLabel.setForeground(Color.WHITE);
        forgotLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.insets = new Insets(0, 10, 0, 10);
        rightPanel.add(forgotLabel, gbc);

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Login button action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticate(username, password)) {
                frame.dispose();
                String role = username.equals(ADMIN_USER) ? "ADMIN" : "STAFF";

                Dashboard.showDashboard(args, role);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        // Forgot password action
        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Please contact your system administrator",
                        "Password Help",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    /**
     * Creates a styled text field with placeholder functionality
     * @param placeholder Hint text to display when empty
     * @return Configured JTextField
     */

    private static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.DARK_GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setOpaque(false);
        field.setBackground(new Color(255, 255, 255, 150));
        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }
    /**
     * Creates a secure password field with:
     * - Placeholder display
     * - Echo character toggle
     * - Consistent styling
     */
    private static JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setOpaque(false);
        field.setBackground(new Color(255, 255, 255, 150));
        field.setEchoChar((char)0);
        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char)0);
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }
    /**
     * Styles the login button with hover effects
     * @param button Button to style
     */
    private static void styleLoginButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 153, 102));
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 133, 82));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 153, 102));
            }
        });
    }
    /**
     * Validates user credentials
     * @param username Entered username
     * @param password Entered password
     * @return true if credentials match, false otherwise
     */

    private static boolean authenticate(String username, String password) {
        return (username.equals(ADMIN_USER) && password.equals(ADMIN_PASS)) ||
                (username.equals(STAFF_USER) && password.equals(STAFF_PASS));
    }
}