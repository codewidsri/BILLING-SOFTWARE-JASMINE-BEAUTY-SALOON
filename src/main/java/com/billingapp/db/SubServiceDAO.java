package com.billingapp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.billingapp.model.SubServicesModel;
import com.billingapp.model.SubServicesView;

public class SubServiceDAO {

    public int addSubService(String subservice_name, double subservice_price, int service_id) throws SQLException {
        String query = "INSERT INTO subservices(subservice_name,subservice_price,service_id) VALUES(?,?,?)";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, subservice_name);
            preparedStatement.setDouble(2, subservice_price);
            preparedStatement.setInt(3, service_id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating sub service failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating Subservice failed, no ID obtained.");
                }
            }
        }
    }

    public ArrayList<SubServicesView> getAllSubServicesWithServices() throws SQLException {
        String query = """
                SELECT s.service_id as service_id,
                s.service_name as service_name,
                ss.subservice_id as subservice_id,
                ss.subservice_name as subservice_name,
                ss.subservice_price as subservice_price,
                ss.is_active as is_active FROM services s JOIN subservices ss ON s.service_id = ss.service_id
                """;
        ArrayList<SubServicesView> subServicesList = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
                ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                subServicesList.add(
                        new SubServicesView(resultSet.getInt("subservice_id"), resultSet.getString("subservice_name"),
                                resultSet.getDouble("subservice_price"), resultSet.getInt("service_id"),
                                resultSet.getString("service_name"), resultSet.getBoolean("is_active")));
            }
            return subServicesList;
        }
    }

    public ArrayList<SubServicesModel> getAllSubServicesByServiceId(int service_id) throws SQLException {
        String query = "SELECT * FROM subservices where is_active = 1 AND service_id = ?";
        ArrayList<SubServicesModel> subServiceList = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, service_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subServiceList.add(new SubServicesModel(resultSet.getInt("subservice_id"),
                        resultSet.getString("subservice_name"), resultSet.getDouble("subservice_price"),
                        resultSet.getInt("service_id"), resultSet.getBoolean("is_active")));
            }
            return subServiceList;
        }
    }

    public int updateSubServiceActiveStateById(int service_id, int subservice_id, boolean is_active)
            throws SQLException {
        String query = "UPDATE subservices SET is_active = ? WHERE service_id = ? AND subservice_id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, is_active ? 1 : 0);
            preparedStatement.setInt(2, service_id);
            preparedStatement.setInt(3, subservice_id);
            return preparedStatement.executeUpdate();
        }
    }

    public int updateSubServiceById(int subservice_id, String subservice_name, double subservice_price,
            int service_id) throws SQLException {
        String query = "UPDATE subservices SET subservice_name = ?, subservice_price = ? WHERE service_id = ? AND subservice_id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, subservice_name);
            preparedStatement.setDouble(2, subservice_price);
            preparedStatement.setInt(3, service_id);
            preparedStatement.setInt(4, subservice_id);
            return preparedStatement.executeUpdate();
        }
    }

}