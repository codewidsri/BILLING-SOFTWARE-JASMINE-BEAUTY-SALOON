package com.billingapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.billingapp.model.AdminModel;

public class AdminDAO {

    public AdminModel findbyemail(String email) throws SQLException {
        String query = "SELECT * FROM admins WHERE admin_email = ?";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedstatement = connection.prepareStatement(query)) {
            preparedstatement.setString(1, email);
            ResultSet resultset = preparedstatement.executeQuery();
            AdminModel adminModel = null;
            if (resultset.next()) {
                adminModel = new AdminModel(resultset.getInt("admin_id"), resultset.getString("admin_email"),
                        resultset.getString("admin_password"), resultset.getString("salt"));
            }
            return adminModel;
        }
    }

    public int insert(String email, String password, String salt) throws SQLException {
        String query = "INSERT INTO admins(admin_email,admin_password,salt) VALUES(?,?,?)";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, salt);
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Adding admin failed, no ID obtained.");
                }
            }
        }
    }
}