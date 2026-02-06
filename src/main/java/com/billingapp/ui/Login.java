package com.billingapp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.billingapp.service.Authentication;
import com.billingapp.ui.admin.DashBoard;

public class Login extends JFrame {

    JPanel formPanel, emailpanel, passwordpanel, loginpanel;
    JLabel emaillabel, passwordlabel;
    JTextField emailfield;
    JPasswordField passwordfield;
    JButton loginbutton, registerbutton;

    public Login() {
        setLayout(new GridBagLayout());
        setTitle("Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setIconImage(new ImageIcon(getClass().getResource("/assets/app.png")).getImage());

        loginpanel = new JPanel();
        loginpanel.setLayout(new BoxLayout(loginpanel, BoxLayout.Y_AXIS));
        loginpanel.setPreferredSize(new Dimension(300, 200));
        loginpanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        formPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        formPanel.setPreferredSize(new Dimension(300, 150));
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        emailpanel = new JPanel(new BorderLayout(0, 5));
        emaillabel = new JLabel("Email");
        emaillabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        emailfield = new JTextField();
        emailfield.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailpanel.add(emaillabel, BorderLayout.NORTH);
        emailpanel.add(emailfield, BorderLayout.CENTER);

        passwordpanel = new JPanel(new BorderLayout(0, 5));
        passwordlabel = new JLabel("Password");
        passwordlabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passwordfield = new JPasswordField();
        passwordfield.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordpanel.add(passwordlabel, BorderLayout.NORTH);
        passwordpanel.add(passwordfield, BorderLayout.CENTER);

        loginbutton = new JButton("Login");
        loginbutton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        loginbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginbutton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginbutton.setOpaque(true);
        loginbutton.setBackground(new Color(13, 110, 253));
        loginbutton.setForeground(Color.WHITE);
        loginbutton.setContentAreaFilled(true);
        loginbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailfield.getText();
                char[] password = passwordfield.getPassword();

                try {
                    boolean success = Authentication.login(email, password);
                    if (success) {
                        new DashBoard();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid email or password", "Login Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

        });

        registerbutton = new JButton("Register");
        registerbutton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        registerbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerbutton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerbutton.setOpaque(true);
        registerbutton.setBackground(new Color(13, 110, 253));
        registerbutton.setForeground(Color.WHITE);
        registerbutton.setContentAreaFilled(true);
        registerbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailfield.getText();
                char[] password = passwordfield.getPassword();
                try {
                    boolean success = Authentication.register(email, password);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Registration successful", "success",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(null, "missing email or password", "Registration Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        formPanel.add(emailpanel);
        formPanel.add(passwordpanel);

        loginpanel.add(formPanel);
        loginpanel.add(loginbutton);
        loginpanel.add(registerbutton);

        add(loginpanel);
        setVisible(true);
    }
}