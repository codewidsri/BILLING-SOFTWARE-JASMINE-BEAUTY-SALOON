package com.billingapp.ui.bill.invoice;

import java.awt.Desktop;
import java.io.File;

import javax.swing.JOptionPane;

import com.billingapp.model.Bill;

public class InvoicePreview {

    public static boolean preview(Bill bill) throws Exception {

        File temp = File.createTempFile("invoice_preview_", ".pdf");
        temp.deleteOnExit();

        InvoicePdfGenerator.generateInvoice(bill, temp);
        Desktop.getDesktop().open(temp);

        int res = JOptionPane.showConfirmDialog(
                null,
                "Save and print this invoice?",
                "Confirm Invoice",
                JOptionPane.YES_NO_OPTION
        );

        return res == JOptionPane.YES_OPTION;
    }
}
