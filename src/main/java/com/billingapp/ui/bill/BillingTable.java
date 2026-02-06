package com.billingapp.ui.bill;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.billingapp.model.Bill;
import com.billingapp.model.BillPreviewDTO;
import com.billingapp.service.BillServices;
import com.billingapp.ui.bill.invoice.InvoicePdfGenerator;
import com.billingapp.util.PrinterUtil;
import com.billingapp.util.eventmanagers.BillListenerEventManager;
import com.billingapp.util.eventmanagers.BillPreviewEventManager;
import com.billingapp.util.listeners.BillPreviewListener;

public class BillingTable extends JPanel implements BillPreviewListener {

    private JTable table;
    private DefaultTableModel model;
    private JButton submit;

    public BillingTable() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(1300, 350));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        String columnNames[] = { "ID", "Service ID", "Service Name", "SubService ID", "SubService Name", "Price",
                "Quantity", "Total", "Customer Name", "Phone", "Area", "Paid by", "Action", "data" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 12;
            }
        };

        table = new JTable(model);
        table.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setFillsViewportHeight(true); // important for scroll
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setResizingAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.removeColumn(table.getColumnModel().getColumn(13));
        table.removeColumn(table.getColumnModel().getColumn(3));
        table.removeColumn(table.getColumnModel().getColumn(1));
        table.getColumnModel().getColumn(10).setCellRenderer(new BillingButtonRenderer());
        table.getColumnModel().getColumn(10).setCellEditor(new BillingButtonEditor(table, model));
        table.setAutoscrolls(true);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);

        submit = new JButton("Submit");
        submit.setMaximumSize(new Dimension(500, 100));
        submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submit.setContentAreaFilled(true);
        submit.setOpaque(true);
        submit.setBackground(new Color(13, 110, 253));
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bill_id = BillServices.generateBillAndReturnId(model);
                    if (bill_id > 0) {
                        JOptionPane.showMessageDialog(null, "Bill added successfully", "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                        BillListenerEventManager.notifyBilled();

                        model.setRowCount(0);

                        Bill bill = BillServices.printBillItemsById(bill_id);
                        File finalPdf = new File(System.getProperty("user.home"),
                                "BillingInvoices/invoice_" + bill.getInvoiceNumber().replace("/", "_") + ".pdf");
                        finalPdf.getParentFile().mkdirs();

                        InvoicePdfGenerator.generateInvoice(bill, finalPdf);
                        PrinterUtil.print(finalPdf);

                    }

                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane);
        add(submit);

        BillPreviewEventManager.addListener(this);
    }

    @Override
    public void onAddPreviewBill(BillPreviewDTO dto) {
        model.addRow(new Object[] { model.getRowCount() + 1, dto.getServiceId(), dto.getServiceName(),
                dto.getSubserviceId(), dto.getSubserviceName(), dto.getSubservicePrice(), dto.getQuantity(),
                dto.getTotal(), dto.getCustomerName(), dto.getCustomerMobileNumber(), dto.getCustomerAddress(),
                dto.getPaidby(), "Delete", dto });
    }

}