package com.billingapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.billingapp.model.Bill;
import com.billingapp.model.BillItems;
import com.billingapp.model.BillsView;

public class BillDAO {

    public int insertBillAndReturnBillId(Connection connection, int customer_id, String paidby)
            throws SQLException {
        String query = "INSERT INTO bills(invoice_number, customer_id, bill_date, paidby) VALUES(?,?,CURRENT_DATE,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query,
                PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, "invoice_number");
            preparedStatement.setInt(2, customer_id);
            preparedStatement.setString(3, paidby);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert bill");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Bill ID not generated");
                }
            }
        }
    }

    public void updateInvoiceNumber(Connection connection, int bill_id, String invoice_number) throws SQLException {

        String query = "UPDATE bills SET invoice_number = ? WHERE bill_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, invoice_number);
            preparedStatement.setInt(2, bill_id);
            preparedStatement.executeUpdate();
        }
    }

    public int insertBillItem(Connection connection, int bill_id, int service_id, int subservice_id, double price,
            int quantity, double total)
            throws SQLException {
        String query = "INSERT INTO bill_items(bill_id,service_id,subservice_id,price,quantity,total) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, bill_id);
            preparedStatement.setInt(2, service_id);
            preparedStatement.setInt(3, subservice_id);
            preparedStatement.setDouble(4, price);
            preparedStatement.setInt(5, quantity);
            preparedStatement.setDouble(6, total);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert bill item");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating bill Item failed, for the bill id " + bill_id);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Could not add item to bill " + bill_id, e);
        }
    }

    public Bill printBillItemsById(int bill_id) throws SQLException {

        String query = """
                SELECT
                s.service_name,
                ss.subservice_name,
                bi.price,
                bi.quantity,
                bi.total,
                b.bill_id,
                b.invoice_number,
                b.bill_date,
                c.customer_name,
                c.customer_mobile_number,
                c.customer_address
                FROM bill_items bi
                JOIN bills b ON b.bill_id = bi.bill_id
                JOIN customers c ON c.customer_id = b.customer_id
                JOIN services s ON s.service_id = bi.service_id
                JOIN subservices ss ON ss.subservice_id = bi.subservice_id
                WHERE b.bill_id = ?;
                """;

        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bill_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<BillItems> billItems = new ArrayList<>();

            String invoiceNumber = null, customerName = null, customerMobileNumber = null, customerAddress = null,
                    billDate = null;
            Integer billId = null;

            while (resultSet.next()) {

                if (invoiceNumber == null || billId == null) {
                    billId = resultSet.getInt("bill_id");
                    invoiceNumber = resultSet.getString("invoice_number");
                    customerName = resultSet.getString("customer_name");
                    customerMobileNumber = resultSet.getString("customer_mobile_number");
                    customerAddress = resultSet.getString("customer_address");
                    billDate = resultSet.getString("bill_date");
                }

                billItems.add(new BillItems(
                        resultSet.getString("service_name"),
                        resultSet.getString("subservice_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getBigDecimal("total")));

            }

            if (invoiceNumber == null) {
                throw new SQLException("Bill not found: " + bill_id);
            }

            return new Bill(
                    billId,
                    invoiceNumber,
                    customerName,
                    customerMobileNumber,
                    customerAddress,
                    billDate,
                    billItems);
        }
    }

    public ArrayList<BillsView> getAllBills() throws SQLException {

        String query = """
                        SELECT b.bill_id,
                               b.invoice_number,
                               c.customer_name,
                               c.customer_mobile_number,
                               c.customer_address,
                               b.bill_date,
                               b.paidby,
                               s.service_name,
                               ss.subservice_name,
                               bi.price,
                               bi.quantity,
                               bi.total
                        FROM bill_items bi
                        JOIN bills b ON b.bill_id = bi.bill_id
                        JOIN customers c ON c.customer_id = b.customer_id
                        JOIN services s ON s.service_id = bi.service_id
                        JOIN subservices ss ON ss.subservice_id = bi.subservice_id
                """;
        ArrayList<BillsView> bills = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                bills.add(new BillsView(resultSet.getInt("bill_id"), resultSet.getString("invoice_number"),
                        resultSet.getString("service_name"), resultSet.getString("subservice_name"),
                        resultSet.getInt("quantity"), resultSet.getBigDecimal("price"),
                        resultSet.getBigDecimal("total"),
                        resultSet.getString("customer_name"), resultSet.getString("customer_mobile_number"),
                        resultSet.getString("customer_address"),
                        resultSet.getString("bill_date"), resultSet.getString("paidby")));
            }
            return bills;
        }
    }

    public ArrayList<BillsView> getBillsByFilters(Integer year, Integer month, Integer serviceId, Integer subServiceId,
            boolean forGST)
            throws SQLException {

        StringBuilder sql = new StringBuilder("""
                    SELECT b.bill_id,
                           b.invoice_number,
                           c.customer_name,
                           c.customer_mobile_number,
                           c.customer_address,
                           b.bill_date,
                           b.paidby,
                           s.service_name,
                           ss.subservice_name,
                           bi.price,
                           bi.quantity,
                           bi.total
                    FROM bill_items bi
                    JOIN bills b ON b.bill_id = bi.bill_id
                    JOIN customers c ON c.customer_id = b.customer_id
                    JOIN services s ON s.service_id = bi.service_id
                    JOIN subservices ss ON ss.subservice_id = bi.subservice_id
                    WHERE 1 = 1
                """);

        ArrayList<Object> params = new ArrayList<>();

        if (year != null) {
            sql.append(" AND strftime('%Y', b.bill_date) = ?");
            params.add(String.valueOf(year));
        }

        if (month != null) {
            sql.append(" AND strftime('%m', b.bill_date) = ?");
            params.add(String.format("%02d", month));
        }

        if (serviceId != null) {
            sql.append(" AND s.service_id = ?");
            params.add(serviceId);
        }

        if (subServiceId != null) {
            sql.append(" AND ss.subservice_id = ?");
            params.add(subServiceId);
        }

        if (forGST) {
            sql.append("""
                        ORDER BY b.bill_date ASC, b.bill_id ASC
                    """);
        } else {
            sql.append("""
                        ORDER BY
                        b.bill_date DESC,
                        s.service_name,
                        ss.subservice_name
                    """);
        }

        ArrayList<BillsView> bills = new ArrayList<>();

        try (Connection con = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bills.add(new BillsView(resultSet.getInt("bill_id"), resultSet.getString("invoice_number"),
                            resultSet.getString("service_name"), resultSet.getString("subservice_name"),
                            resultSet.getInt("quantity"), resultSet.getBigDecimal("price"),
                            resultSet.getBigDecimal("total"),
                            resultSet.getString("customer_name"), resultSet.getString("customer_mobile_number"),
                            resultSet.getString("customer_address"),
                            resultSet.getString("bill_date"), resultSet.getString("paidby")));
                }
            }
            return bills;
        }
    }

}