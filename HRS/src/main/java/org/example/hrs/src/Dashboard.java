
package org.example.hrs.src;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
/**
 * Dashboard - The entry point GUI for the Luxury Hotel Reservation System
 *
 * Displays a welcome screen with:
 * - Branded hotel background
 * - Role-based welcome message (Admin/Staff)
 * - Entry point to the main reservation system
 *
 * Features:
 * - Layered UI with semi-transparent overlay
 * - Responsive background image loading
 * - Professional button with hover effects
 * - Clean, modern design
 */

public class Dashboard {
    /**
     * Launches the dashboard screen
     * @param args Command line arguments to pass to main system
     * @param role User role ("Admin" or "Staff") for personalized greeting
     */
    public static void showDashboard(String[] args,String role) {
        SwingUtilities.invokeLater(() -> createAndShowGUI(args,role));
    }
    /**
     * Creates and displays the main dashboard window
     */
    private static void createAndShowGUI(String[] args,String role) {
        // 1. Create main frame
        JFrame frame = new JFrame("Luxury Hotel Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);

        // 2. Create layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        frame.setContentPane(layeredPane);

        // 3. Create and add background
        JLabel background = createBackground();
        background.setBounds(0, 0, 1200, 700);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // 4. Add semi-transparent overlay
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setBounds(0, 0, 1200, 700);
        overlay.setLayout(new BorderLayout());
        overlay.setOpaque(false);
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);

        // 5. Add welcome message
        JLabel welcomeLabel = new JLabel("WELCOME " + role + " TO GRAND LUXURY HOTELS",
                SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 48));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));

        // 6. Add enter button
        JButton enterButton = createEnterButton(args, frame);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(enterButton);

        // 7. Add footer
        JLabel footerLabel = new JLabel("© 2025 Grand Luxury Hotels. All Rights Reserved.",
                SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // 8. Add components to overlay
        overlay.add(welcomeLabel, BorderLayout.NORTH);
        overlay.add(buttonPanel, BorderLayout.CENTER);
        overlay.add(footerLabel, BorderLayout.SOUTH);

        // 9. Make frame visible
        frame.setVisible(true);
    }
    /**
     * Creates the background with fallback options:
     * 1. Tries loading from absolute path
     * 2. Falls back to embedded resource
     * 3. Final fallback to solid color
     * @return JLabel containing the background image/color
     */
    private static JLabel createBackground() {
        try {
            // Try loading from absolute path first
            String absPath = "C:/Users/Habibullah Khaliqyar/Desktop/HRS/image.jpg";
            File file = new File(absPath);

            if (file.exists()) {
                Image image = ImageIO.read(file);
                if (image != null) {
                    image = image.getScaledInstance(1200, 700, Image.SCALE_SMOOTH);
                    return new JLabel(new ImageIcon(image));
                }
            }

            // Fallback to resource
            URL resourceUrl = Dashboard.class.getResource("/resources/hotel_bg.jpg");
            if (resourceUrl != null) {
                Image image = ImageIO.read(resourceUrl);
                image = image.getScaledInstance(1200, 700, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(image));
            }
        } catch (Exception e) {
            System.err.println("Error loading background: " + e.getMessage());
        }

        // Final fallback - solid color
        JLabel fallback = new JLabel();
        fallback.setOpaque(true);
        fallback.setBackground(new Color(8, 40, 77)); // Dark blue
        return fallback;
    }
    /**
     * Creates the main entry button with:
     * - Professional styling
     * - Hover effects
     * - Action to launch main reservation system
     */
    private static JButton createEnterButton(String[] args, JFrame frame) {
        JButton button = new JButton("ENTER RESERVATION SYSTEM");
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        // Action to open main menu
        button.addActionListener(e -> {
            frame.dispose();
            MainMenu.main(args);
        });

        return button;
    }
}