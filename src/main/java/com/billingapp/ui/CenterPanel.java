package com.billingapp.ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.billingapp.ui.bill.BillingForm;
import com.billingapp.ui.bill.BillingTable;

public class CenterPanel extends JPanel {

    public CenterPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new CustomerHistoryPanel());
        add(new BillingForm());
        add(new BillingTable());
    }
}