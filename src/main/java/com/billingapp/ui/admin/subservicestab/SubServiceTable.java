package com.billingapp.ui.admin.subservicestab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.billingapp.service.SubServices;
import com.billingapp.util.eventmanagers.UpdateChangeEventManager;
import com.billingapp.util.listeners.UpdateChangeListener;

public class SubServiceTable extends JPanel implements UpdateChangeListener {

    private JTable table;
    private DefaultTableModel model;

    public SubServiceTable() {
        setLayout(new BorderLayout());

        String columnNames[] = { "ID", "Service ID", "Service Name", "SubService ID", "SubService Name", "Price",
                "Update", "Status", "Active" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7;
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
        table.removeColumn(table.getColumnModel().getColumn(8));
        table.removeColumn(table.getColumnModel().getColumn(3));
        table.removeColumn(table.getColumnModel().getColumn(1));

        table.getColumnModel().getColumn(4).setCellRenderer(new SubServiceButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new SubServiceButtonEditor(table, model));
        table.getColumnModel().getColumn(5).setCellRenderer(new SubServiceButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new SubServiceButtonEditor(table, model));

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(500);
        columnModel.getColumn(2).setPreferredWidth(500);
        columnModel.getColumn(3).setPreferredWidth(500);
        columnModel.getColumn(4).setPreferredWidth(150);
        columnModel.getColumn(5).setPreferredWidth(150);

        add(new JScrollPane(table), BorderLayout.CENTER);

        showAllSubServices();
        UpdateChangeEventManager.addListener(this);
    }

    public void showAllSubServices() {
        try {
            model.setRowCount(0);
            SubServices.getAllSubServicesWithServices().forEach(subservice -> {
                model.addRow(new Object[] { subservice.getSubServiceId(), subservice.getServiceId(),
                        subservice.getServiceName(), subservice.getSubServiceId(), subservice.getSubServiceName(),
                        subservice.getSubServicePrice(), "Update", subservice.getIsActive() ? "Disable" : "Enable",
                        subservice.getIsActive() });
            });
        } catch (Exception e) {
            model.addRow(new Object[] { e.getMessage() });
        }
    }

    @Override
    public void onUpdated() {
        showAllSubServices();
    }

}