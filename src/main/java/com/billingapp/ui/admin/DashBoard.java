package com.billingapp.ui.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class DashBoard extends JFrame {
    CardLayout cardLayout;
    MainFrame mainFrame;
    SideNavBar sideNavBar;

    public DashBoard() {
        setTitle("DashBoard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon(getClass().getResource("/assets/app.png")).getImage());
        
        cardLayout = new CardLayout();
        mainFrame = new MainFrame(cardLayout);
        sideNavBar = new SideNavBar(cardLayout, mainFrame);

        add(sideNavBar, BorderLayout.LINE_START);
        add(mainFrame, BorderLayout.CENTER);

        setVisible(true);
    }

}