package com.billingapp.ui.admin.servicestab;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ServiceTab extends JPanel {

    public ServiceTab() {
        setLayout(new BorderLayout());

        add(new ServiceForm(), BorderLayout.NORTH);
        add(new ServiceTable(), BorderLayout.CENTER);
    }

}