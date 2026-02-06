package com.billingapp.ui.admin;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SideNavBar extends JPanel {
    JButton buttons[];
    String buttonnames[] = { "Services", "Sub Services", "Customers", "Bills", "GST Billing", "Import Data" };

    public SideNavBar(CardLayout cardLayout, MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, Integer.MAX_VALUE));
        setMaximumSize(new Dimension(200, Integer.MAX_VALUE));

        buttons = new JButton[buttonnames.length];
        for (int i = 0; i < buttonnames.length; i++) {
            int index = i;
            buttons[i] = new JButton(buttonnames[i]);
            buttons[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            buttons[i].setFont(new Font("Segoe UI", Font.BOLD, 16));
            buttons[i].setOpaque(true);
            buttons[i].setBackground(new Color(13, 110, 253));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setContentAreaFilled(true);
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    buttons[index].setBackground(Color.BLACK);
                    buttons[index].setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    buttons[index].setBackground(new Color(13, 110, 253));
                    buttons[index].setForeground(Color.WHITE);
                }
            });
            buttons[i].addActionListener(e -> {
                cardLayout.show(mainFrame, buttonnames[index]);
            });
            add(buttons[i]);
        }
    }
}