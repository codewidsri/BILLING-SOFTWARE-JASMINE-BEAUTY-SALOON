package com.billingapp.ui.bill;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BillingButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private JTable table;
    private DefaultTableModel model;

    public BillingButtonEditor(JTable table, DefaultTableModel model) {
        super(new JCheckBox());
        this.table = table;
        this.model = model;

        button = new JButton();
        button.addActionListener(e -> {
            handleClick();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        label = value.toString();
        button.setText(label);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    public void handleClick() {

        fireEditingStopped();
        int row = table.getSelectedRow();

         if (label.equals("Delete")) {
            model.removeRow(row);
         }
    }
}