package com.billingapp.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.billingapp.service.CustomerServices;

public class CustomerHistoryPanel extends JPanel {

    JPanel toppanel, bottompanel;
    JLabel label, mobilenumberlabel;
    JTextField textfield;
    JButton button;
    JTable table;
    DefaultTableModel model;

    public CustomerHistoryPanel() {

        setLayout(new GridLayout(2, 1));
        setPreferredSize(new Dimension(500, 200));
        setMaximumSize(new Dimension(500, 200));
        setFocusable(true);
        requestFocusInWindow();

        toppanel = new JPanel(new GridBagLayout());
        toppanel.setPreferredSize(new Dimension(500, 100));

        label = new JLabel("View Customer History");
        label.setFont(new Font("Segoe UI", Font.BOLD, 25));

        toppanel.add(label);

        bottompanel = new JPanel();
        bottompanel.setLayout(new BoxLayout(bottompanel, BoxLayout.X_AXIS));
        bottompanel.setPreferredSize(new Dimension(150, 50));
        bottompanel.setMaximumSize(new Dimension(150, 50));

        mobilenumberlabel = new JLabel("Enter Mobile Number");
        mobilenumberlabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        textfield = new JTextField(30);
        textfield.setPreferredSize(new Dimension(0, 30));
        textfield.setMaximumSize(new Dimension(0, 30));
        textfield.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ((AbstractDocument) textfield.getDocument()).setDocumentFilter(
                new DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                            throws BadLocationException {

                        if (isValid(fb, text)) {
                            super.insertString(fb, offset, text, attr);
                        }
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                            throws BadLocationException {

                        if (isValid(fb, text)) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }

                    private boolean isValid(FilterBypass fb, String text) throws BadLocationException {
                        if (!text.matches("\\d*"))
                            return false;
                        return fb.getDocument().getLength() + text.length() <= 10;
                    }
                });

        button = new JButton("Check History");
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(125, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBackground(new Color(13, 110, 253));
        button.setForeground(Color.WHITE);
        button.setBorder(new LineBorder(new Color(13, 110, 253), 0, true));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setContentAreaFilled(true);
                button.setBackground(new Color(0x0b5ed7));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setContentAreaFilled(true);
                button.setBackground(new Color(13, 110, 253));
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    String phone = textfield.getText();
                    String[] columns = { "Invoice No", "Service", "Sub Service", "Qty", "Price", "Total", "Date" };
                    model = new DefaultTableModel(columns, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

                    if (phone == null || phone.isBlank()) {
                        JOptionPane.showMessageDialog(null, "Please Enter Mobile Number ","Warning",JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    CustomerServices.getCustomerLatestBills(textfield.getText()).forEach(customer -> {
                        model.addRow(new Object[] { customer.getInvoiceNumber(), customer.getServiceName(),
                                customer.getSubServiceName(), customer.getQuantity(), customer.getPrice(),
                                customer.getTotal(), customer.getBillDate() });
                    });

                    table = new JTable(model);
                    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
                    table.getTableHeader().setBackground(Color.BLACK);
                    table.getTableHeader().setForeground(Color.WHITE);
                    table.getTableHeader().setResizingAllowed(true);
                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setPreferredSize(new Dimension(600, 250));
                    JOptionPane.showMessageDialog(null, scrollPane, "Recent Bills", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bottompanel.add(mobilenumberlabel);
        bottompanel.add(textfield);
        bottompanel.add(button);

        add(toppanel);
        add(bottompanel);
    }
}