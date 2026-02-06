package com.billingapp.ui.admin.gstbillingtab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.billingapp.model.Bill;
import com.billingapp.service.BillServices;
import com.billingapp.ui.bill.invoice.MonthlyGstPdfGenerator;
import com.billingapp.util.PrinterUtil;

public class GSTBillingTab extends JPanel {
    JComboBox<Integer> year;
    JComboBox<Integer> month;
    JLabel yearlabel, monthlabel;
    JPanel panel, yearpanel, monthpanel;
    JButton button;

    public GSTBillingTab() {
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 90));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        yearpanel = new JPanel();
        yearpanel.setLayout(new BoxLayout(yearpanel, BoxLayout.Y_AXIS));
        yearlabel = new JLabel("Select Year");
        yearlabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        year = new JComboBox<>();
        year.setFont(new Font("Segoe UI", Font.BOLD, 15));
        year.setPreferredSize(new Dimension(250, 70));
        year.setMaximumSize(new Dimension(250, 70));
        for (int i = 2026; i < LocalDate.now().getYear() + 5; i++) {
            year.addItem(i);
        }
        yearpanel.add(yearlabel);
        yearpanel.add(year);

        monthpanel = new JPanel();
        monthpanel.setLayout(new BoxLayout(monthpanel, BoxLayout.Y_AXIS));
        monthlabel = new JLabel("Select Month");
        monthlabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        month = new JComboBox<>();
        month.setFont(new Font("Segoe UI", Font.BOLD, 15));
        month.setPreferredSize(new Dimension(250, 70));
        month.setMaximumSize(new Dimension(250, 70));
        for (int index = 1; index <= 12; index++) {
            month.addItem(index);
        }
        monthpanel.add(monthlabel);
        monthpanel.add(month);

        button = new JButton("Generate");
        button.setBackground(new Color(92, 99, 106));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Integer y = (Integer) year.getSelectedItem();
                Integer m = (Integer) month.getSelectedItem();
                try {
                    ArrayList<Bill> billsofmonth = BillServices.generateGSTBills(y, m);
                    File file = new File(System.getProperty("user.home"),
                            "MonthlyGSTBIlling/GST_BILLS_" + y + "_" + m + ".pdf");
                    file.getParentFile().mkdirs();
                    MonthlyGstPdfGenerator.generateMonthlyReport(billsofmonth, file);
                    JOptionPane.showMessageDialog(null, "Generated", "Success", JOptionPane.INFORMATION_MESSAGE);
                    PrinterUtil.print(file);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(yearpanel);
        panel.add(monthpanel);
        panel.add(button);

        add(panel, BorderLayout.NORTH);
    }

}
