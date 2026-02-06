package com.billingapp.ui.admin;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;

import com.billingapp.ui.admin.subservicestab.SubServiceTab;
import com.billingapp.ui.admin.billstab.BillTab;
import com.billingapp.ui.admin.customerstab.CustomerTab;
import com.billingapp.ui.admin.gstbillingtab.GSTBillingTab;
import com.billingapp.ui.admin.importexporttab.ImportExportDataTab;
import com.billingapp.ui.admin.servicestab.ServiceTab;

public class MainFrame extends JPanel{
    
    public MainFrame(CardLayout cardLayout){
        setLayout(cardLayout);
        setBackground(Color.MAGENTA);

        add(new ServiceTab(),"Services");
        add(new SubServiceTab(),"Sub Services");
        add(new CustomerTab(),"Customers");
        add(new BillTab(),"Bills");
        add(new GSTBillingTab(),"GST Billing");
        add(new ImportExportDataTab(),"Import Data");

        cardLayout.show(this, "Services");
    }

}