package com.billingapp.ui.admin.billstab;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class BillTab extends JPanel {

    public BillTab() {
        setLayout(new BorderLayout());

        add(new BillForm(), BorderLayout.NORTH);
        add(new BillTable(), BorderLayout.CENTER);
    }

}
