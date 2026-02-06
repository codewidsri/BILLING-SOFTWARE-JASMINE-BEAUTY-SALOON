package com.billingapp.ui.admin.servicestab;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.billingapp.service.Services;
import com.billingapp.util.eventmanagers.UpdateChangeEventManager;

import javax.swing.border.EmptyBorder;

public class ServiceForm extends JPanel {

    JTextField servicename;
    JButton addservice;
    JLabel servicenamelabel;
    JPanel servicenamepanel;

    public ServiceForm() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        servicenamepanel = new JPanel();
        servicenamepanel.setLayout(new BoxLayout(servicenamepanel, BoxLayout.Y_AXIS));
        servicenamelabel = new JLabel("Enter Service Name");
        servicenamelabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        servicename = new JTextField(30);
        servicename.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        servicename.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        servicename.setFont(new Font("Segoe UI", Font.BOLD, 15));
        servicename.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                servicename.setText(servicename.getText().toUpperCase());
            }
        });
        servicenamepanel.add(servicenamelabel);
        servicenamepanel.add(servicename);

        addservice = new JButton("Add Service");
        addservice.setPreferredSize(new Dimension(150, Integer.MAX_VALUE));
        addservice.setMaximumSize(new Dimension(150, Integer.MAX_VALUE));
        addservice.setFont(new Font("Segoe UI", Font.BOLD, 15));
        addservice.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addservice.setForeground(Color.WHITE);
        addservice.setBorder(new LineBorder(new Color(13, 110, 253), 0, true));
        addservice.setBackground(new Color(13, 110, 253));
        addservice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String service = servicename.getText();
                if (service.isBlank()) {
                    JOptionPane.showMessageDialog(null, "please enter the service name", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    boolean success = Services.addService(service);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Service added successfully", "Information", JOptionPane.INFORMATION_MESSAGE);
                        UpdateChangeEventManager.notifyUpdated();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please Enter all Fields Correctly", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(servicenamepanel);
        add(Box.createHorizontalStrut(10));
        add(addservice);
    }
}