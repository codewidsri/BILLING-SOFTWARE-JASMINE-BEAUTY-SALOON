package com.billingapp.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DataBaseInitializer {

    private DataBaseInitializer() {
    }

    public static void createTables() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String service = """
                    CREATE TABLE IF NOT EXISTS services(
                        service_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        service_name TEXT NOT NULL UNIQUE,
                        is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0,1))
                    )""";

            String subservice = """
                    CREATE TABLE IF NOT EXISTS subservices(
                        subservice_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        subservice_name TEXT NOT NULL,
                        subservice_price REAL NOT NULL CHECK(subservice_price >= 0),
                        service_id INTEGER NOT NULL,
                        is_active INTEGER NOT NULL DEFAULT 1 CHECK(is_active IN (0,1)),
                        FOREIGN KEY(service_id) REFERENCES services(service_id) ON UPDATE CASCADE,
                        UNIQUE (subservice_name, service_id)
                    )""";

            String customer = """
                    CREATE TABLE IF NOT EXISTS customers(
                        customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        customer_name TEXT NOT NULL,
                        customer_mobile_number TEXT NOT NULL UNIQUE,
                        customer_address TEXT NOT NULL
                    )""";

            String bills = """
                    CREATE TABLE IF NOT EXISTS bills (
                        bill_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        invoice_number TEXT NOT NULL UNIQUE,
                        customer_id INTEGER NOT NULL,
                        bill_date DATE NOT NULL,
                        paidby TEXT NOT NULL CHECK(paidby IN ('CASH','GPAY','EMI')),
                        FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
                    )
                    """;

            String bill_items = """
                    CREATE TABLE IF NOT EXISTS bill_items (
                        item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        bill_id INTEGER NOT NULL,
                        service_id INTEGER NOT NULL,
                        subservice_id INTEGER NOT NULL,
                        price REAL NOT NULL,
                        quantity INTEGER NOT NULL,
                        total REAL NOT NULL,
                        FOREIGN KEY (bill_id) REFERENCES bills(bill_id),
                        FOREIGN KEY (service_id) REFERENCES services(service_id),
                        FOREIGN KEY (subservice_id) REFERENCES subservices(subservice_id)
                    )""";

            String admin = """
                        CREATE TABLE IF NOT EXISTS admins(
                            admin_id INTEGER PRIMARY KEY AUTOINCREMENT,
                            admin_email TEXT NOT NULL,
                            admin_password TEXT NOT NULL,
                            salt TEXT NOT NULL
                    )""";

            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute(service);
            statement.execute(subservice);
            statement.execute(customer);
            statement.execute(bills);
            statement.execute(bill_items);
            statement.execute(admin);
            connection.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}