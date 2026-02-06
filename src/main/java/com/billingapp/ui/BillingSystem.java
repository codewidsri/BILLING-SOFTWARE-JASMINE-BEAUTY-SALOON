package com.billingapp.ui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.billingapp.db.DataBaseInitializer;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class BillingSystem {

    JFrame frame;

    public BillingSystem() {
        FlatIntelliJLaf.setup();
    
        DataBaseInitializer.createTables();

        frame = new JFrame("JASMINE BEAUTY SALOON");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setIconImage(new ImageIcon(getClass().getResource("/assets/app.png")).getImage());
        frame.setLayout(new BorderLayout()); 
        
        frame.add(new HeaderPanel(), BorderLayout.NORTH);
        frame.add(new CenterPanel(), BorderLayout.CENTER);
        
        frame.setVisible(true);
    }
}