package com.billingapp.ui.admin.servicestab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.billingapp.service.Services;
import com.billingapp.util.eventmanagers.UpdateChangeEventManager;
import com.billingapp.util.listeners.UpdateChangeListener;

public class ServiceTable extends JPanel implements UpdateChangeListener {

    private JTable table;
    private DefaultTableModel model;

    public ServiceTable() {
        setLayout(new BorderLayout());

        String[] columnNames = { "ID", "Service ID", "Service Name", "Update", "Status", "Active" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setResizingAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        table.removeColumn(table.getColumnModel().getColumn(5));
        table.removeColumn(table.getColumnModel().getColumn(1));

        table.getColumnModel().getColumn(2).setCellRenderer(new ServiceButtonRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new ServiceButtonEditor(table, model));
        table.getColumnModel().getColumn(3).setCellRenderer(new ServiceButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ServiceButtonEditor(table, model));

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(1000);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);

        add(new JScrollPane(table), BorderLayout.CENTER);

        showAllServices();
        UpdateChangeEventManager.addListener(this);
    }

    public void showAllServices() {
        try {
            model.setRowCount(0);
            Services.getAllServices(false).forEach(service -> {
                model.addRow(new Object[] { service.getServiceId(), service.getServiceId(), service.getServiceName(),
                        "Update", service.getIsActive() ? "Disable" : "Enable", service.getIsActive() });
            });
        } catch (Exception e) {
            model.addRow(new Object[]{e.getMessage()});
        }
    }

    @Override
    public void onUpdated() {
        showAllServices();
    }
}