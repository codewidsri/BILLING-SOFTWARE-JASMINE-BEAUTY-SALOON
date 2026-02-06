package com.billingapp.ui.admin.billstab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.billingapp.model.BillsView;
import com.billingapp.service.BillServices;
import com.billingapp.util.eventmanagers.BillListenerEventManager;
import com.billingapp.util.listeners.BillListener;

public class BillTable extends JPanel implements BillListener {

    private JTable table;
    private DefaultTableModel model;
    private JLabel totalamountlLabel;

    public BillTable() {
        setLayout(new BorderLayout());

        totalamountlLabel = new JLabel("Total Revenue :- ");
        totalamountlLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        String columnNames[] = { "Bill ID", "Invoice ID", "Service", "Subservice", "Quantity", "Price", "Total",
                "Customer Name", "Customer Phone", "Date", "PaidBy" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);

        add(totalamountlLabel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        showAllBills();

        BillListenerEventManager.addListener(this);
    }

    private void showAllBills() {
        try {
            BigDecimal totalamount = BigDecimal.ZERO;
            model.setRowCount(0);
            for (var bill : BillServices.getAllBills()) {
                model.addRow(new Object[] { bill.getBillId(), bill.getInvoiceNumber(), bill.getServiceName(),
                        bill.getSubServiceName(), bill.getQuantity(), bill.getPrice(), bill.getTotal(),
                        bill.getCustomerName(), bill.getCustomerMobileNumber(), bill.getBillDate(), bill.getPaidBy() });
                 totalamount = totalamount.add(bill.getTotal());
            }
            totalamountlLabel.setText("TOTAL AMOUNT :- ₹ " + totalamount);
        } catch (Exception e) {
            model.addRow(new Object[] { e.getMessage() });
        }
    }

    @Override
    public void onBilled() {
        showAllBills();
    }

    @Override
    public void onBillFiltered(ArrayList<BillsView> bills) {
        try {
            BigDecimal totalamount = BigDecimal.ZERO;
            model.setRowCount(0);
            for (var bill : bills) {
                model.addRow(new Object[] { bill.getBillId(), bill.getInvoiceNumber(), bill.getServiceName(),
                        bill.getSubServiceName(), bill.getQuantity(), bill.getPrice(), bill.getTotal(),
                        bill.getCustomerName(), bill.getCustomerMobileNumber(), bill.getBillDate(), bill.getPaidBy() });
                 totalamount = totalamount.add(bill.getTotal());
            }
            totalamountlLabel.setText("TOTAL AMOUNT :- ₹ " + totalamount);
        } catch (Exception e) {
            model.addRow(new Object[] { e.getMessage() });
        }
    }
}