package com.billingapp.ui.admin.customerstab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.billingapp.model.BillsView;
import com.billingapp.service.CustomerServices;
import com.billingapp.util.eventmanagers.BillListenerEventManager;
import com.billingapp.util.listeners.BillListener;

public class CustomerTable extends JPanel implements BillListener {

    private JTable table;
    private DefaultTableModel model;

    public CustomerTable() {
        setLayout(new BorderLayout());

        String columnNames[] = { "ID", "Customer Name", "Phone", "Address" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        showAllCustomers();

        BillListenerEventManager.addListener(this);
    }

    public void showAllCustomers() {
        try {
            model.setRowCount(0);
            CustomerServices.getAllCustomers().forEach(customer -> {
                model.addRow(new Object[] { customer.getCustomer_id(), customer.getCustomer_name(),
                        customer.getCustomer_mobile_number(), customer.getCustomer_address() });
            });
        } catch (Exception e) {
            model.addRow(new Object[]{e.getMessage()});
        }
    }

    @Override
    public void onBilled() {
        showAllCustomers();
    }

    @Override
    public void onBillFiltered(ArrayList<BillsView> list) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onBillFiltered'");
    }
}