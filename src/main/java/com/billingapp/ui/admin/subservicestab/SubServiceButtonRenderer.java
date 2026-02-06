package com.billingapp.ui.admin.subservicestab;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class SubServiceButtonRenderer extends JButton implements TableCellRenderer {

    public SubServiceButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(value.toString().equals("Update") ? Color.YELLOW : value.toString().equals("Disable") ? Color.RED :Color.GREEN);
        setForeground(value.toString().equals("Update") ? Color.BLACK : Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        return this;
    }
    
}
