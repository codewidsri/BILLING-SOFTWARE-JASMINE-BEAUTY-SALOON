package com.billingapp.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class HeaderPanel extends JPanel {

    JPanel leftPanel, rightPanel, buttonPanel;
    ImageIcon logo;
    JLabel logoLabel;
    JButton loginButton;

    public HeaderPanel() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 100));
        setBackground(Color.WHITE);

        leftPanel = new JPanel();
        logo = new ImageIcon(getClass().getResource("/assets/images/logo.png"));
        Image resized = logo.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(resized));
        leftPanel.add(logoLabel);
        leftPanel.setOpaque(false);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(150, 125));
        rightPanel.setOpaque(false);

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(85, 35));
        loginButton.setFocusable(false);
        loginButton.setBorder(new LineBorder(new Color(13, 110, 253), 2, true));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 15));
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(true);
        loginButton.setForeground(new Color(13, 110, 253));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setOpaque(true);
                loginButton.setContentAreaFilled(true);
                loginButton.setBackground(new Color(13, 110, 253));
                loginButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setOpaque(false);
                loginButton.setContentAreaFilled(false);
                loginButton.setBackground(null);
                loginButton.setForeground(new Color(13, 110, 253));
            }
        });
        loginButton.addActionListener(e -> {
            new Login();
        });

        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);

        rightPanel.add(buttonPanel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

}