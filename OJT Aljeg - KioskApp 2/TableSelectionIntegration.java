import javax.swing.*;

import com.brewhaven.ui.TableSelectionPanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * This file contains the code snippets you need to add to your main BrewHavenKioskApp class
 * to integrate the table selection functionality.
 */

public class TableSelectionIntegration extends JFrame {
    private TableSelectionPanel tableSelectionPanel;
    private int selectedTable = -1;
    private String orderType;
    private JPanel mainPanel;
    private JFrame mainFrame;

    // Modify your createOrderTypeOptionPanel method to handle table selection
    private JPanel createOrderTypeOptionPanel(String type, boolean isDineIn) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE); // Replace BH_WHITE with Color.WHITE
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.YELLOW, 2), // Replace BH_GOLD with Color.YELLOW
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Create icon
        BufferedImage iconImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = iconImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(Color.RED); // Replace BH_RED with Color.RED
        if (isDineIn) {
            // Table icon for dine in
            g.fillRoundRect(25, 60, 50, 10, 5, 5);  // Table surface
            g.fillRect(45, 70, 10, 25);            // Table leg
            g.fillOval(35, 30, 15, 15);           // Plate
            g.fillOval(55, 30, 15, 15);           // Cup
        } else {
            // Bag icon for take out
            g.fillRoundRect(30, 30, 40, 45, 10, 10);  // Bag
            g.fillRect(20, 30, 60, 10);              // Bag top
            g.drawArc(35, 20, 30, 20, 0, 180);      // Handle
        }
        g.dispose();
        
        JLabel iconLabel = new JLabel(new ImageIcon(iconImage));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel typeLabel = new JLabel(type);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        typeLabel.setForeground(Color.RED); // Replace BH_DARK_RED with Color.RED
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton selectButton = createStyledButton("Select", 200, 50);
        selectButton.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Replace HEADING_FONT with new Font
        selectButton.setBackground(Color.RED); // Replace BH_RED with Color.RED
        selectButton.setForeground(Color.WHITE); // Replace BH_WHITE with Color.WHITE
        selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectButton.addActionListener(e -> {
            orderType = type;
            
            // If Dine In is selected, show table selection screen
            if (isDineIn) {
                animateTransition("tableSelection");
            } else {
                // For Take Out, skip table selection
                selectedTable = -1; // No table needed
                animateTransition("menu");
            }
        });
        
        panel.add(iconLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(typeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(selectButton);
        
        // Add hover effect
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED, 2), // Replace BH_RED with Color.RED
                    BorderFactory.createEmptyBorder(30, 30, 30, 30)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.YELLOW, 2), // Replace BH_GOLD with Color.YELLOW
                    BorderFactory.createEmptyBorder(30, 30, 30, 30)
                ));
            }
        });
        
        return panel;
    }

    // Add this method to create the table selection screen
    private JPanel createTableSelectionScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY); // Replace BH_LIGHT_GRAY with Color.LIGHT_GRAY
        
        // Header
        JPanel headerPanel = createHeader("Select a Table", false);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE); // Replace BH_WHITE with Color.WHITE
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Instructions
        JLabel instructionsLabel = new JLabel("Please select a table for your dine-in order:");
        instructionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // Replace SUBTITLE_FONT with new Font
        instructionsLabel.setForeground(Color.RED); // Replace BH_DARK_RED with Color.RED
        contentPanel.add(instructionsLabel, BorderLayout.NORTH);
        
        // Table selection panel
        tableSelectionPanel = new TableSelectionPanel();
       contentPanel.add(tableSelectionPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE); // Replace BH_WHITE with Color.WHITE
        
        JButton backButton = createStyledButton("Back", 120, 40);
        backButton.setBackground(Color.DARK_GRAY); // Replace BH_DARK_GRAY with Color.DARK_GRAY
        backButton.addActionListener(e -> animateTransition("orderType"));
        
        JButton continueButton = createStyledButton("Continue", 120, 40);
        continueButton.setBackground(Color.GREEN); // Replace BH_GREEN with Color.GREEN
        continueButton.addActionListener(e -> {
            selectedTable = tableSelectionPanel.getSelectedTable();
            if (selectedTable != -1) {
                animateTransition("menu");
            } else {
                // Show error message if no table is selected
                JOptionPane.showMessageDialog(mainFrame, 
                    "Please select a table to continue.", 
                    "Table Selection Required", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(continueButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // Modify your constructor to add the table selection screen
    public TableSelectionIntegration() {
        // Existing code...
        
        // Create and add screens
        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(createWelcomeScreen(), "welcome");
        mainPanel.add(createMenuScreen(), "menu");
        mainPanel.add(createCustomizeScreen(), "customize");
        mainPanel.add(createCartScreen(), "cart");
        mainPanel.add(createCheckoutScreen(), "checkout");
        mainPanel.add(createOrderConfirmationScreen(), "confirmation");
        mainPanel.add(createLoyaltyScreen(), "loyalty");
        mainPanel.add(createOrderTypeScreen(), "orderType");
        mainPanel.add(createOrderTypeOptionPanel("Dine In", true), "dineInOption");
        mainPanel.add(createOrderTypeOptionPanel("Take Out", false), "takeOutOption");
        mainPanel.add(createTableSelectionScreen(), "tableSelection"); // Add this line
        mainPanel.add(createAboutScreen(), "about");
        
        // Existing code...
    }

    // Modify your checkout screen to display the selected table
    private JPanel createCheckoutScreen() {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE); // Replace BH_WHITE with Color.WHITE
        
        // Existing code...
        
        // Add table information if dining in
        if ("Dine In".equals(orderType) && selectedTable != -1) {
            JPanel tableInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            tableInfoPanel.setBackground(Color.WHITE); // Replace BH_WHITE with Color.WHITE
            
            JLabel tableLabel = new JLabel("Table: " + selectedTable);
            tableLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // Replace BODY_FONT with new Font
            tableLabel.setForeground(Color.RED); // Replace BH_DARK_RED with Color.RED
            
            tableInfoPanel.add(tableLabel);
            
            // Add this after the orderTypePanel in your existing code
            summaryPanel.add(tableInfoPanel);
            summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        // Rest of your existing code...
        
        return summaryPanel;
    }

    // Modify your order confirmation screen to display the selected table
    private JPanel createOrderConfirmationScreen() {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Color.WHITE); // Replace BH_WHITE with Color.WHITE
        
        // Existing code...
        
        // Add table information if dining in
        if ("Dine In".equals(orderType) && selectedTable != -1) {
            JLabel tableNumberLabel = new JLabel("Table #" + selectedTable);
            tableNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Replace HEADING_FONT with new Font
            tableNumberLabel.setForeground(Color.DARK_GRAY); // Replace BH_DARK_GRAY with Color.DARK_GRAY
            tableNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add this after orderTypeLabel in your existing code
            messagePanel.add(tableNumberLabel);
        }
        
        // Rest of your existing code...
        
        return messagePanel;
    }

    // Add any other necessary methods and classes here...

    private JButton createStyledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }

    private void animateTransition(String screenName) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, screenName);
    }

    private JPanel createHeader(String title, boolean backButtonVisible) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createWelcomeScreen() {
        // Placeholder for the actual implementation
        return new JPanel();
    }

    private JPanel createMenuScreen() {
        // Placeholder for the actual implementation
        return new JPanel();
    }

    private JPanel createCustomizeScreen() {
        // Placeholder for the actual implementation
        return new JPanel();
    }

    private JPanel createCartScreen() {
        // Placeholder for the actual implementation
        return new JPanel();
    }

    private JPanel createLoyaltyScreen() {
        // Placeholder for the actual implementation
        return new JPanel();
    }

    private JPanel createOrderTypeScreen() {
        // Placeholder for the actual implementation
        return new JPanel();
    }

    private JPanel createAboutScreen() {
        // Placeholder for the actual implementation
        return new JPanel();
    }
}