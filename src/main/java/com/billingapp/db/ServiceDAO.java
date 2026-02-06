package com.billingapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.billingapp.model.ServicesModel;

public class ServiceDAO {

    public int addService(String service_name) throws SQLException {
        String query = "INSERT INTO services(service_name) VALUES(?)";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, service_name);
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating service failed, no ID obtained.");
                }
            }
        }
    }

    public ArrayList<ServicesModel> getAllServices(boolean is_active) throws SQLException {
        String query = is_active ? "SELECT * FROM services where is_active = 1" : "SELECT * FROM services";
        ArrayList<ServicesModel> servicesList = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                servicesList.add(new ServicesModel(resultSet.getInt("service_id"), resultSet.getString("service_name"),
                        resultSet.getBoolean("is_active")));
            }
            return servicesList;
        }
    }

    public int updateServiceStatusById(int service_id, boolean is_active) throws SQLException {
        String query = "UPDATE services SET is_active = ? WHERE service_id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, is_active ? 1 : 0);
            preparedStatement.setInt(2, service_id);
            return preparedStatement.executeUpdate();
        }
    }

    public int updateServiceById(int service_id, String service_name) throws SQLException {
        String query = "UPDATE services SET service_name = ? WHERE service_id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, service_name);
            preparedStatement.setInt(2, service_id);
            return preparedStatement.executeUpdate();
        }
    }

}