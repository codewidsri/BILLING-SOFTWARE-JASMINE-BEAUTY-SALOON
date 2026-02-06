package com.billingapp.ui.bill;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.billingapp.model.BillPreviewDTO;
import com.billingapp.model.ServiceItem;
import com.billingapp.model.ServicesModel;
import com.billingapp.model.SubServiceItem;
import com.billingapp.model.SubServicesModel;
import com.billingapp.service.Services;
import com.billingapp.service.SubServices;
import com.billingapp.util.eventmanagers.BillPreviewEventManager;
import com.billingapp.util.eventmanagers.UpdateChangeEventManager;
import com.billingapp.util.listeners.UpdateChangeListener;

public class BillingForm extends JPanel implements UpdateChangeListener {

    JPanel billpanel, formPanel, servicepanel, subservicepanel, pricepanel, quantitypanel, customernamepanel,
            mobilenumberpanel, addresspanel, paidbypanel, totalamountpanel;
    JLabel label, serviceLabel, subserviceLabel, priceLabel, quantityLabel, customernameLabel, mobilenumberLabel,
            addressLabel, paidbyLabel, totalamountLabel;
    JComboBox<ServiceItem> service;
    JComboBox<SubServiceItem> subservice;
    JComboBox<String> paidby;
    JTextField price, quantity, customername, mobilenumber, address, totalamount;
    JButton addtopreviewButton;

    public BillingForm() {

        setFocusable(true);
        requestFocusInWindow();

        billpanel = new JPanel();
        billpanel.setLayout(new BoxLayout(billpanel, BoxLayout.Y_AXIS));
        billpanel.setPreferredSize(new Dimension(750, 300));
        billpanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        formPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        formPanel.setPreferredSize(new Dimension(1000, 250));
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        label = new JLabel("Billing Entry");
        label.setFont(new Font("Segoe UI", Font.BOLD, 25));
        label.setForeground(new Color(13, 110, 253));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        servicepanel = new JPanel(new BorderLayout(0, 5));
        service = new JComboBox<>();
        service.addItem(new ServiceItem(0, "Select Service"));
        service.addActionListener(e -> {
            ServiceItem selected = (ServiceItem) service.getSelectedItem();
            price.setText("");
            totalamount.setText("");
            if (selected != null && selected.getServiceId() != 0) {
                loadSubServices(selected.getServiceId());
            }
        });
        service.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        serviceLabel = new JLabel("Service");
        serviceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        servicepanel.add(serviceLabel, BorderLayout.NORTH);
        servicepanel.add(service, BorderLayout.CENTER);

        subservicepanel = new JPanel(new BorderLayout(0, 5));
        subservice = new JComboBox<>();
        subservice.addItem(new SubServiceItem(0, "Select SubService", 0));
        subservice.addActionListener(e -> {
            SubServiceItem selected = (SubServiceItem) subservice.getSelectedItem();
            price.setText("");
            totalamount.setText("");

            if (selected != null && selected.getSubServiceId() != 0) {
                price.setText(String.valueOf(selected.getSubServicePrice()));
                calculateTotal();
            }
        });
        subservice.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subserviceLabel = new JLabel("Sub Service");
        subserviceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subservicepanel.add(subserviceLabel, BorderLayout.NORTH);
        subservicepanel.add(subservice, BorderLayout.CENTER);

        pricepanel = new JPanel(new BorderLayout(0, 5));
        price = new JTextField(30);
        price.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        price.setEnabled(false);
        priceLabel = new JLabel("Price");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pricepanel.add(priceLabel, BorderLayout.NORTH);
        pricepanel.add(price, BorderLayout.CENTER);

        quantitypanel = new JPanel(new BorderLayout(0, 5));
        quantity = new JTextField(30);
        quantity.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateTotal();
            }
        });
        quantity.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ((AbstractDocument) quantity.getDocument()).setDocumentFilter(new DocumentFilter() {
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
        quantityLabel = new JLabel("Quantity");
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        quantitypanel.add(quantityLabel, BorderLayout.NORTH);
        quantitypanel.add(quantity, BorderLayout.CENTER);

        customernamepanel = new JPanel(new BorderLayout(0, 5));
        customername = new JTextField(30);
        customername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        customernameLabel = new JLabel("Customer Name");
        customernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        customernamepanel.add(customernameLabel, BorderLayout.NORTH);
        customernamepanel.add(customername, BorderLayout.CENTER);

        mobilenumberpanel = new JPanel(new BorderLayout(0, 5));
        mobilenumber = new JTextField(30);
        mobilenumber.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ((AbstractDocument) mobilenumber.getDocument()).setDocumentFilter(
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
        mobilenumberLabel = new JLabel("Mobile Number");
        mobilenumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mobilenumberpanel.add(mobilenumberLabel, BorderLayout.NORTH);
        mobilenumberpanel.add(mobilenumber, BorderLayout.CENTER);

        addresspanel = new JPanel(new BorderLayout(0, 5));
        address = new JTextField(30);
        address.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addressLabel = new JLabel("Address");
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addresspanel.add(addressLabel, BorderLayout.NORTH);
        addresspanel.add(address, BorderLayout.CENTER);

        paidbypanel = new JPanel(new BorderLayout(0, 5));
        paidby = new JComboBox<>();
        paidby.addItem("Select Payment");
        paidby.addItem("CASH");
        paidby.addItem("GPAY");
        paidby.addItem("EMI");
        paidby.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        paidbyLabel = new JLabel("Paid By");
        paidbyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        paidbypanel.add(paidbyLabel, BorderLayout.NORTH);
        paidbypanel.add(paidby, BorderLayout.CENTER);

        totalamountpanel = new JPanel(new BorderLayout(0, 5));
        totalamount = new JTextField(30);
        totalamount.setEnabled(false);
        totalamount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        totalamountLabel = new JLabel("Total Amount");
        totalamountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalamountpanel.add(totalamountLabel, BorderLayout.NORTH);
        totalamountpanel.add(totalamount, BorderLayout.CENTER);

        formPanel.add(servicepanel);
        formPanel.add(subservicepanel);
        formPanel.add(pricepanel);
        formPanel.add(quantitypanel);
        formPanel.add(customernamepanel);
        formPanel.add(mobilenumberpanel);
        formPanel.add(addresspanel);
        formPanel.add(paidbypanel);
        formPanel.add(totalamountpanel);

        addtopreviewButton = new JButton("Add to Preview");
        addtopreviewButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        addtopreviewButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addtopreviewButton.setFocusable(false);
        addtopreviewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addtopreviewButton.setContentAreaFilled(true);
        addtopreviewButton.setOpaque(true);
        addtopreviewButton.setBackground(new Color(92, 99, 106));
        addtopreviewButton.setForeground(Color.WHITE);
        addtopreviewButton.setFocusPainted(false);
        addtopreviewButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addtopreviewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ServiceItem serviceItem = (ServiceItem) service.getSelectedItem();
                SubServiceItem subServiceItem = (SubServiceItem) subservice.getSelectedItem();
                int q = quantity.getText().isBlank() ? 0 : Integer.parseInt(quantity.getText());
                String cstnm = customername.getText();
                String cstmbn = mobilenumber.getText();
                String cstarea = address.getText();
                String pdby = (String) paidby.getSelectedItem();
                double total = totalamount.getText().isBlank() ? 0 : Double.parseDouble(totalamount.getText());

                if (serviceItem == null || subServiceItem == null || q <= 0 || cstnm.isBlank() || cstnm == null
                        || cstarea.isBlank() || cstarea == null || pdby == null || pdby.equals("Select Payment")
                        || paidby.getSelectedIndex() == 0 || total <= 0 || cstmbn == null || cstmbn.isBlank()) {

                    JOptionPane.showMessageDialog(null, "Please fill all the fields", "Warning",
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }

                BillPreviewDTO dto = new BillPreviewDTO(serviceItem, subServiceItem, q, total, pdby, cstnm, cstmbn,
                        cstarea);
                BillPreviewEventManager.notifyAdd(dto);
            }

        });

        billpanel.add(label);
        billpanel.add(Box.createVerticalStrut(15));
        billpanel.add(formPanel);
        billpanel.add(Box.createVerticalStrut(10));
        billpanel.add(addtopreviewButton);

        add(billpanel);

        loadServices();

        UpdateChangeEventManager.addListener(this);
    }

    private void loadServices() {
        new SwingWorker<List<ServicesModel>, Void>() {
            @Override
            protected List<ServicesModel> doInBackground() throws Exception {
                return Services.getAllServices(true);
            }

            @Override
            protected void done() {
                try {
                    var servicesList = get();
                    if (servicesList != null) {
                        service.removeAllItems();
                        service.addItem(new ServiceItem(0, "Select Service"));
                        servicesList.forEach(s -> {
                            service.addItem(new ServiceItem(s.getServiceId(), s.getServiceName()));
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void loadSubServices(int service_id) {
        new SwingWorker<List<SubServicesModel>, Void>() {

            @Override
            protected List<SubServicesModel> doInBackground() throws Exception {
                return SubServices.getAllSubServicesByServiceId(service_id);
            }

            @Override
            protected void done() {
                try {
                    var subServicesList = get();
                    if (subServicesList != null) {
                        subservice.removeAllItems();
                        subservice.addItem(new SubServiceItem(0, "Select SubService", 0));
                        subServicesList.forEach(ss -> {
                            subservice.addItem(new SubServiceItem(ss.getSubserviceId(), ss.getSubserviceName(),
                                    ss.getSubservicePrice()));
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.execute();
    }

    private void calculateTotal() {
        try {
            double p = price.getText().isEmpty() ? 0 : Double.parseDouble(price.getText());
            int q = quantity.getText().isEmpty() ? 0 : Integer.parseInt(quantity.getText());
            totalamount.setText(String.valueOf(p * q));
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public void onUpdated() {
        loadServices();
    }

}