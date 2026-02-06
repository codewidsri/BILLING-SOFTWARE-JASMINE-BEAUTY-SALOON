package com.billingapp.ui.admin.servicestab;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.billingapp.service.Services;
import com.billingapp.util.eventmanagers.UpdateChangeEventManager;

public class ServiceButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private JTable table;
    private DefaultTableModel model;

    public ServiceButtonEditor(JTable table, DefaultTableModel model) {
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
        int service_id = (int) model.getValueAt(row, 1);
        String servicename = (String) model.getValueAt(row, 2);

        if (label.equals("Update")) {
            String updatedservicename = (String) JOptionPane.showInputDialog(null, "Enter new service name:", "Update",
                    JOptionPane.OK_CANCEL_OPTION, null, null, servicename);
            if (updatedservicename == null) {
                return;
            }
            try {
                if (Services.updateServiceById(service_id, updatedservicename.toUpperCase(), servicename)) {
                    JOptionPane.showMessageDialog(null, "update Operation Succesfull", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                    UpdateChangeEventManager.notifyUpdated();
                } else {
                    JOptionPane.showMessageDialog(null, "Please Enter all Fields Correctly", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (label.equals("Disable")) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure? \n Do you want to disable " + servicename + " service?", "Disable",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (Services.disableServiceById(service_id, servicename)) {
                        JOptionPane.showMessageDialog(null, "Disable Operation Succesfull", "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        UpdateChangeEventManager.notifyUpdated();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please Enter all Fields Correctly", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Disable Operation cancelled", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (label.equals("Enable")) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure? \n Do you want to Enable " + servicename + " service?", "Enable",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (Services.enableServiceById(service_id, servicename)) {
                        JOptionPane.showMessageDialog(null, "Enable Operation Succesfull", "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        UpdateChangeEventManager.notifyUpdated();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please Enter all Fields Correctly", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Enable Operation cancelled", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}