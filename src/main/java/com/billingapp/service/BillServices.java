package com.billingapp.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import com.billingapp.db.BillDAO;
import com.billingapp.db.DataBaseConnection;
import com.billingapp.model.Bill;
import com.billingapp.model.BillItems;
import com.billingapp.model.BillPreviewDTO;
import com.billingapp.model.BillsView;

public class BillServices {

    private static final BillDAO billingDAO = new BillDAO();

    public static int generateBillAndReturnId(DefaultTableModel model) throws SQLException {

        if (model.getRowCount() == 0)
            throw new SQLException("No items to bill");

        BillPreviewDTO dto = (BillPreviewDTO) model.getValueAt(0, 13);

        try (Connection connection = DataBaseConnection.getConnection()) {

            int existing_customer_id = CustomerServices.getCustomerIdByMobileNumber(dto.getCustomerMobileNumber());
            int customer_id = existing_customer_id == -1
                    ? CustomerServices.addCustomer(dto.getCustomerName(), dto.getCustomerMobileNumber(),
                            dto.getCustomerAddress())
                    : existing_customer_id;

            connection.setAutoCommit(false);
            try {

                int bill_id = billingDAO.insertBillAndReturnBillId(connection, customer_id, dto.getPaidby());
                String invoice = generateInvoiceNumber(bill_id);
                billingDAO.updateInvoiceNumber(connection, bill_id, invoice);
                for (int i = 0; i < model.getRowCount(); i++) {
                    dto = (BillPreviewDTO) model.getValueAt(i, 13);
                    billingDAO.insertBillItem(connection, bill_id, dto.getServiceId(), dto.getSubserviceId(),
                            dto.getSubservicePrice(),
                            dto.getQuantity(), dto.getTotal());
                }
                connection.commit();
                connection.setAutoCommit(true);
                return bill_id;
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException("Billing transaction failed", e);
            }

        }

    }

    public static Bill printBillItemsById(int bill_id) throws SQLException {
        if (bill_id < 0)
            return null;
        try {
            return billingDAO.printBillItemsById(bill_id);
        } catch (Exception e) {
            throw new SQLException("Failed to fetch Bill Items " + " \n" + e.getMessage());
        }
    }

    public static ArrayList<BillsView> getAllBills() throws SQLException {
        try {
            return billingDAO.getAllBills();
        } catch (Exception e) {
            throw new SQLException("Failed to fetch Bills " + "\n" + e.getMessage());
        }
    }

    public static ArrayList<Bill> generateGSTBills(Integer year,Integer month) throws SQLException {
        try {
            ArrayList<BillsView> billsViews = getBillsByFilters(year,month,0,0,true);
            LinkedHashMap<Integer, Bill> billHashMap = new LinkedHashMap<>();
            for (BillsView billView : billsViews) {
                Bill bill = billHashMap.get(billView.getBillId());
                if (bill == null) {
                    bill = new Bill(billView.getBillId(),billView.getInvoiceNumber(), billView.getCustomerName(),
                            billView.getCustomerMobileNumber(), billView.getCustomerAddress(), billView.getBillDate(),
                            new ArrayList<>());
                    billHashMap.put(billView.getBillId(), bill);
                }
                bill.getBillItems().add(
                        new BillItems(billView.getServiceName(), billView.getSubServiceName(), billView.getQuantity(),
                                billView.getPrice(), billView.getTotal()));
            }
            return new ArrayList<>(billHashMap.values());
        } catch (Exception e) {
            throw new SQLException("Failed to generate GST BILLS " + "\n" + e.getMessage());
        }
    }

    public static ArrayList<BillsView> getBillsByFilters(Integer year, Integer month, Integer serviceId,
            Integer subServiceId, boolean forGST) throws SQLException {
        Integer service_id = serviceId == 0 ? null : serviceId, subservice_id = subServiceId == 0 ? null : subServiceId;
        try {
            return billingDAO.getBillsByFilters(year, month, service_id, subservice_id, forGST);
        } catch (Exception e) {
            throw new SQLException("Failed to sort Bills " + "\n" + e.getMessage());
        }
    }

    public static String generateInvoiceNumber(int invoice_number) {
        return "Jas-" + String.format("%04d", invoice_number) + "/" + getFinancialYear();
    }

    public static String getFinancialYear() {
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        if (month > 4) {
            return String.valueOf(year).substring(2) + "-" + String.valueOf(year + 1).substring(2);
        }
        return String.valueOf(year - 1).substring(2) + "-" + String.valueOf(year).substring(2);
    }

}