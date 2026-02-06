package com.billingapp.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.billingapp.db.CustomerDAO;
import com.billingapp.model.BillsView;
import com.billingapp.model.CustomersModel;

public class CustomerServices {

    private static CustomerDAO customerDAO = new CustomerDAO();

    public static int getCustomerIdByMobileNumber(String mobileNumber) throws SQLException {
        if (mobileNumber == null || mobileNumber.isBlank()) {
            return -1;
        }
        try {
            return customerDAO.getCustomerIdByMobileNumber(mobileNumber);
        } catch (Exception e) {
            throw new SQLException("Failed to get customer id. " + "\n" + e.getMessage(), e);
        }
    }

    public static ArrayList<BillsView> getCustomerLatestBills(String mobilenumber) throws SQLException {
        if (mobilenumber == null || mobilenumber.isBlank()) {
            return null;
        }
        try {
            return customerDAO.getCustomerLatestBills(mobilenumber);
        } catch (Exception e) {
            throw new SQLException("Failed to get customer BILLS " + "\n" + e.getMessage());
        }
    }

    public static int addCustomer(String customer_name, String customer_mobile_number, String customer_address)
            throws SQLException {
        if (customer_name == null || customer_name.isBlank() || customer_mobile_number == null
                || customer_mobile_number.isBlank() || customer_address == null || customer_address.isBlank()) {
            return -1;
        }
        try {
            return customerDAO.addCustomer(customer_name, customer_mobile_number, customer_address);
        } catch (Exception e) {
            throw new SQLException("Failed to add customer. " + "\n" + e.getMessage(), e);
        }
    }

    public static ArrayList<CustomersModel> getAllCustomers() throws SQLException {
        try {
            return customerDAO.getAllCustomers();
        } catch (Exception e) {
            throw new SQLException("Failed to load customers list. " + "\n" + e.getMessage(), e);
        }
    }

}