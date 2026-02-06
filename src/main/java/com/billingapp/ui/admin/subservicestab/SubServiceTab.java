package com.billingapp.ui.admin.subservicestab;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class SubServiceTab extends JPanel {

    public SubServiceTab() {
        setLayout(new BorderLayout());

        add(new SubServiceForm(),BorderLayout.NORTH);
        add(new SubServiceTable(),BorderLayout.CENTER);
    }

}