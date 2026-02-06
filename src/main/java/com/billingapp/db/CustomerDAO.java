package com.billingapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.billingapp.model.BillsView;
import com.billingapp.model.CustomersModel;

public class CustomerDAO {

    public int getCustomerIdByMobileNumber(String mobileNumber) throws SQLException {
        String query = "SELECT customer_id FROM customers WHERE customer_mobile_number = ?";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, mobileNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return -1;
        }
    }

    public int addCustomer(String customer_name, String customer_mobile_number, String customer_address)
            throws SQLException {
        String query = "INSERT INTO customers(customer_name,customer_mobile_number,customer_address) VALUES(?,?,?)";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customer_name);
            preparedStatement.setString(2, customer_mobile_number);
            preparedStatement.setString(3, customer_address);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating bill failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Adding customer failed no ID obtained.");
                }
            }
        }
    }

    public ArrayList<CustomersModel> getAllCustomers() throws SQLException {
        String query = "SELECT * FROM customers";
        ArrayList<CustomersModel> customerList = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                customerList.add(new CustomersModel(resultSet.getInt("customer_id"),
                        resultSet.getString("customer_name"), resultSet.getString("customer_mobile_number"),
                        resultSet.getString("customer_address")));
            }
            return customerList;
        }
    }

    public ArrayList<BillsView> getCustomerLatestBills(String mobilenumber) throws SQLException {
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
                        b.paidby,
                        c.customer_name,
                        c.customer_mobile_number,
                        c.customer_address
                        FROM bill_items bi
                        JOIN bills b ON b.bill_id = bi.bill_id
                        JOIN customers c ON c.customer_id = b.customer_id
                        JOIN services s ON s.service_id = bi.service_id
                        JOIN subservices ss ON ss.subservice_id = bi.subservice_id
                        WHERE b.bill_id IN (
                            SELECT bill_id
                            FROM bills
                            WHERE customer_id = (
                                SELECT customer_id
                                FROM customers
                                WHERE customer_mobile_number = ?
                            )
                            ORDER BY bill_date DESC
                            LIMIT 3
                        )
                        ORDER BY b.bill_date DESC;
                """;

        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, mobilenumber);
            ArrayList<BillsView> bills = new ArrayList<>();
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