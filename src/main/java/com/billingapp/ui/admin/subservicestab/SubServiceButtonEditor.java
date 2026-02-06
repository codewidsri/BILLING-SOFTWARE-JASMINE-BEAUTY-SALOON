package com.billingapp.ui.admin.subservicestab;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.billingapp.service.SubServices;
import com.billingapp.util.eventmanagers.UpdateChangeEventManager;

public class SubServiceButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private JTable table;
    private DefaultTableModel model;

    public SubServiceButtonEditor(JTable table, DefaultTableModel model) {
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
        int subservice_id = (int) model.getValueAt(row, 3);
        String subservice_name = (String) model.getValueAt(row, 4);
        double subservice_price = (double) model.getValueAt(row, 5);

        if (label.equals("Update")) {
            JTextField ssname = new JTextField();
            JTextField ssprice = new JTextField();

            ssname.setText(subservice_name);
            ssprice.setText(subservice_price + "");
            ((AbstractDocument) ssprice.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                        throws BadLocationException {
                    if (text != null && text.matches("\\d+")) { // only digits
                        super.insertString(fb, offset, text, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                        throws BadLocationException {
                    if (text != null && text.matches("\\d+")) { // only digits
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            });

            Object[] fields = { "SubService Name : ", ssname, "SubService Price : ", ssprice };

            int confirm = JOptionPane.showConfirmDialog(null, fields, "Update", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                try {
                    if (SubServices.updateSubServiceById(subservice_id, ssname.getText().toUpperCase(),
                            Double.parseDouble(ssprice.getText()), service_id)) {
                        UpdateChangeEventManager.notifyUpdated();
                        JOptionPane.showMessageDialog(null, "Update Operation Succesfull", "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "please fill the details properly", "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Update Operation cancelled", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (label.equals("Disable")) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure? \n Do you want to disable " + subservice_name + " sub service?", "Disable",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (SubServices.disableSubServiceById(service_id, subservice_id, subservice_name)) {
                        UpdateChangeEventManager.notifyUpdated();
                        JOptionPane.showMessageDialog(null, "Disable Operation Succesfull", "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Disable Operation cancelled", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (label.equals("Enable")) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure? \n Do you want to Enable " + subservice_name + " service?", "Enable",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (SubServices.enableSubServiceById(service_id, subservice_id, subservice_name)) {
                        UpdateChangeEventManager.notifyUpdated();
                        JOptionPane.showMessageDialog(null, "Enable Operation Succesfull", "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Enable Operation cancelled", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

}