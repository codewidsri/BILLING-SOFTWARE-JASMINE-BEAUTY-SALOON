package com.billingapp.ui.admin.customerstab;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class CustomerTab extends JPanel {

    public CustomerTab() {
        setLayout(new BorderLayout());
        
        add(new CustomerTable(),BorderLayout.CENTER);
    }
}