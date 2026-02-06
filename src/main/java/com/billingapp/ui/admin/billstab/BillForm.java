package com.billingapp.ui.admin.billstab;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.billingapp.model.ServiceItem;
import com.billingapp.model.ServicesModel;
import com.billingapp.model.SubServiceItem;
import com.billingapp.model.SubServicesModel;
import com.billingapp.service.BillServices;
import com.billingapp.service.Services;
import com.billingapp.service.SubServices;
import com.billingapp.util.eventmanagers.BillListenerEventManager;

public class BillForm extends JPanel {

    JComboBox<Integer> year;
    JComboBox<Integer> month;
    JComboBox<ServiceItem> service;
    JComboBox<SubServiceItem> subservice;
    JLabel yearlabel,monthlabel;
    JPanel yearpanel,monthpanel;
    JButton button;

    public BillForm() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 90));
        setBorder(new EmptyBorder(10, 10, 10, 10));

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

        service = new JComboBox<>();
        service.setFont(new Font("Segoe UI", Font.BOLD, 15));
        service.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        service.addItem(new ServiceItem(0, "Select Service"));
        service.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
        service.addActionListener(e -> {
            ServiceItem selected = (ServiceItem) service.getSelectedItem();
            if (selected != null && selected.getServiceId() != 0) {
                loadSubServices(selected.getServiceId());
            }
        });

        subservice = new JComboBox<>();
        subservice.setFont(new Font("Segoe UI", Font.BOLD, 15));
        subservice.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        subservice.addItem(new SubServiceItem(0, "Select Subservice", 0));
        subservice.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));

        button = new JButton("Search");
        button.setBackground(new Color(92, 99, 106));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                Integer y = (Integer) year.getSelectedItem();
                Integer m = (Integer) month.getSelectedItem();
                ServiceItem serviceItem = (ServiceItem) service.getSelectedItem();
                SubServiceItem subServiceItem = (SubServiceItem) subservice.getSelectedItem();
                try {
                    BillListenerEventManager.notifyBillFiltered(BillServices.getBillsByFilters(y, m, serviceItem.getServiceId(), subServiceItem.getSubServiceId(),false));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(yearpanel);
        add(Box.createHorizontalStrut(10));
        add(monthpanel);
        add(Box.createHorizontalStrut(10));
        add(service);
        add(Box.createHorizontalStrut(10));
        add(subservice);
        add(Box.createHorizontalStrut(10));
        add(button);

        loadServices();
    }

    private void loadServices() {
        new SwingWorker<List<ServicesModel>, Void>() {
            @Override
            protected List<ServicesModel> doInBackground() throws Exception {
                return Services.getAllServices(true);
            }

            @Override
            protected void done() {
                try {
                    var servicesList = get();
                    if (servicesList != null) {
                        service.removeAllItems();
                        service.addItem(new ServiceItem(0, "Select Service"));
                        servicesList.forEach(s -> {
                            service.addItem(new ServiceItem(s.getServiceId(), s.getServiceName()));
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void loadSubServices(int service_id) {
        new SwingWorker<List<SubServicesModel>, Void>() {

            @Override
            protected List<SubServicesModel> doInBackground() throws Exception {
                return SubServices.getAllSubServicesByServiceId(service_id);
            }

            @Override
            protected void done() {
                try {
                    var subServicesList = get();
                    if (subServicesList != null) {
                        subservice.removeAllItems();
                        subservice.addItem(new SubServiceItem(0, "Select SubService", 0));
                        subServicesList.forEach(ss -> {
                            subservice.addItem(new SubServiceItem(ss.getSubserviceId(), ss.getSubserviceName(),
                                    ss.getSubservicePrice()));
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.execute();
    }
}