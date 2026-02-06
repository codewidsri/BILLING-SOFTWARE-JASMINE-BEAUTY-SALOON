package com.billingapp.ui.admin.subservicestab;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.billingapp.model.ServiceItem;
import com.billingapp.model.ServicesModel;
import com.billingapp.service.Services;
import com.billingapp.service.SubServices;
import com.billingapp.util.eventmanagers.UpdateChangeEventManager;
import com.billingapp.util.listeners.UpdateChangeListener;

public class SubServiceForm extends JPanel implements UpdateChangeListener {

    JComboBox<ServiceItem> servicenames;
    JTextField subservicename, subserviceprice;
    JButton addsubservice;
    JLabel subervicenamelabel, subservicepricelabel;
    JPanel subservicenamepanel, subservicepricepanel;

    public SubServiceForm() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        servicenames = new JComboBox<>();
        servicenames.addItem(new ServiceItem(0, "Select Service"));
        servicenames.setFont(new Font("Segoe UI", Font.BOLD, 15));
        servicenames.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        servicenames.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));

        subservicenamepanel = new JPanel();
        subservicenamepanel.setLayout(new BoxLayout(subservicenamepanel, BoxLayout.Y_AXIS));
        subervicenamelabel = new JLabel("Enter Sub Service Name");
        subervicenamelabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subservicename = new JTextField(30);
        subservicename.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        subservicename.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        subservicename.setFont(new Font("Segoe UI", Font.BOLD, 15));
        subservicenamepanel.add(subervicenamelabel);
        subservicenamepanel.add(subservicename);
        subservicename.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                subservicename.setText(subservicename.getText().toUpperCase());
            }
        });

        subservicepricepanel = new JPanel();
        subservicepricepanel.setLayout(new BoxLayout(subservicepricepanel, BoxLayout.Y_AXIS));
        subservicepricelabel = new JLabel("Enter SubService Price");
        subservicepricelabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subserviceprice = new JTextField(30);
        subserviceprice.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        subserviceprice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        subserviceprice.setFont(new Font("Segoe UI", Font.BOLD, 15));
        subservicepricepanel.add(subservicepricelabel);
        subservicepricepanel.add(subserviceprice);
        ((AbstractDocument) subserviceprice.getDocument()).setDocumentFilter(new DocumentFilter() {
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

        addsubservice = new JButton("Add Sub Service");
        addsubservice.setPreferredSize(new Dimension(150, Integer.MAX_VALUE));
        addsubservice.setMaximumSize(new Dimension(150, Integer.MAX_VALUE));
        addsubservice.setFont(new Font("Segoe UI", Font.BOLD, 15));
        addsubservice.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addsubservice.setForeground(Color.WHITE);
        addsubservice.setBorder(new LineBorder(new Color(13, 110, 253), 0, true));
        addsubservice.setBackground(new Color(13, 110, 253));
        addsubservice.setForeground(Color.WHITE);
        addsubservice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ServiceItem serviceItem = (ServiceItem) servicenames.getSelectedItem();
                String subservice = subservicename.getText();
                int price = subserviceprice.getText().isBlank() ? 0 : Integer.parseInt(subserviceprice.getText());

                if (serviceItem == null || serviceItem.getServiceId() == 0) {
                    JOptionPane.showMessageDialog(null, "please enter the sub services field properly", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {
                    if (SubServices.addSubService(subservice, price, serviceItem.getServiceId())) {
                        UpdateChangeEventManager.notifyUpdated();
                        JOptionPane.showMessageDialog(null, "SubService added successfully", "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        subservicename.setText("");
                        subserviceprice.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Please Enter all Fields Correctly", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(servicenames);
        add(Box.createHorizontalStrut(10));
        add(subservicenamepanel);
        add(Box.createHorizontalStrut(10));
        add(subservicepricepanel);
        add(Box.createHorizontalStrut(10));
        add(addsubservice);
        UpdateChangeEventManager.addListener(this);
        loadServices();
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
                        servicenames.removeAllItems();
                        servicenames.addItem(new ServiceItem(0, "Select Service"));
                        servicesList.forEach(s -> {
                            servicenames.addItem(new ServiceItem(s.getServiceId(), s.getServiceName()));
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public void onUpdated() {
        loadServices();
    }

}