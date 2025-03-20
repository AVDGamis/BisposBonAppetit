// Purpose: TableSelectionPanel class for selecting dining tables
// Team: 2
// Last Updated: 4/24/21
package com.brewhaven.ui;
import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
/**
 * TableSelectionPanel - A panel for selecting dining tables
 * 
 * Features:
 * - Displays tables 1-10 in a grid layout
 * - Shows vacant tables in green
 * - Allows selection of a table for dine-in orders
 * - Can be disabled for take-out orders
 */

public class TableSelectionPanel extends JPanel {
    
    // Colors
    private final Color VACANT_COLOR = new Color(40, 160, 40); // Green for vacant tables
    private final Color OCCUPIED_COLOR = new Color(180, 30, 30); // Red for occupied tables
    private final Color SELECTED_COLOR = new Color(0, 120, 215); // Blue for selected table
    private final Color TEXT_COLOR = Color.WHITE;
    
    private List<TableButton> tableButtons = new ArrayList<>();
    private int selectedTable = -1; // -1 means no table selected
    private boolean enabled = true;
    
    // Track which tables are vacant (all vacant by default)
    private boolean[] tableVacancy = new boolean[10];
    
    
    /**
     * Constructor - Creates a table selection panel
     */
    public TableSelectionPanel() {
        setLayout(new GridLayout(2, 5, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(248, 248, 248)); // Light gray background
        
        // Initialize all tables as vacant
        for (int i = 0; i < tableVacancy.length; i++) {
            tableVacancy[i] = true; // true means vacant
        }
        
        // Create table buttons
        for (int i = 1; i <= 10; i++) {
            final int tableNumber = i;
            TableButton tableButton = new TableButton(tableNumber);
            tableButton.addActionListener(e -> {
                if (enabled && tableVacancy[tableNumber - 1]) {
                    selectTable(tableNumber);
                }
            });
            
            add(tableButton);
            tableButtons.add(tableButton);
        }
        
        updateTableDisplay();
    }
    
    /**
     * Selects a table
     * @param tableNumber The table number to select (1-10)
     */
    public void selectTable(int tableNumber) {
        if (tableNumber < 1 || tableNumber > 10) return;
        
        // Deselect previous table
        if (selectedTable != -1) {
            tableButtons.get(selectedTable - 1).setSelected(false);
        }
        
        // Select new table
        selectedTable = tableNumber;
        tableButtons.get(selectedTable - 1).setSelected(true);
        
        updateTableDisplay();
    }
    
    /**
     * Gets the currently selected table
     * @return The selected table number, or -1 if none selected
     */
    public int getSelectedTable() {
        return selectedTable;
    }
    
    /**
     * Sets whether a table is vacant or occupied
     * @param tableNumber The table number (1-10)
     * @param isVacant True if the table is vacant, false if occupied
     */
    public void setTableVacancy(int tableNumber, boolean isVacant) {
        if (tableNumber < 1 || tableNumber > 10) return;
        
        tableVacancy[tableNumber - 1] = isVacant;
        updateTableDisplay();
    }
    
    /**
     * Enables or disables the table selection panel
     * @param enabled True to enable, false to disable
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        for (TableButton button : tableButtons) {
            button.setEnabled(enabled);
        }
        updateTableDisplay();
    }
    
    /**
     * Updates the display of all tables
     */
    private void updateTableDisplay() {
        for (int i = 0; i < tableButtons.size(); i++) {
            TableButton button = tableButtons.get(i);
            int tableNum = i + 1;
    
            if (!enabled) {
                button.setBackground(Color.GRAY);
            } else if (tableNum == selectedTable) {
                button.setBackground(SELECTED_COLOR);
            } else if (tableVacancy[i]) {
                button.setBackground(VACANT_COLOR);
            } else {
                button.setBackground(OCCUPIED_COLOR);
            }
        }
    }
    
    
    /**
     * Custom button class for tables
     */
    private class TableButton extends JButton {
        private int tableNumber;
        private boolean isSelected = false;
        
        public TableButton(int tableNumber) {
            this.tableNumber = tableNumber;
            setText("Table " + tableNumber);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(TEXT_COLOR);
            setFocusPainted(false);
            setBorderPainted(false);
            
            // Set initial color based on vacancy
            setBackground(tableVacancy[tableNumber - 1] ? VACANT_COLOR : OCCUPIED_COLOR);
        }
        
        public void setSelected(boolean selected) {
            this.isSelected = selected;
            setBackground(isSelected ? SELECTED_COLOR : 
                (tableVacancy[tableNumber - 1] ? VACANT_COLOR : OCCUPIED_COLOR));
        }
    }
}

