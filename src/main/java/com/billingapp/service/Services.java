package com.billingapp.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.billingapp.db.ServiceDAO;
import com.billingapp.model.ServicesModel;

public class Services {

    private static ServiceDAO serviceDAO = new ServiceDAO();

    public static boolean addService(String servicename) throws SQLException {
        if (servicename == null || servicename.isBlank()) return false;
        try {
            return serviceDAO.addService(servicename) > 0;
        } catch (SQLException e) {
            throw new SQLException("Failed to add service '" + servicename + "'. " + e.getMessage(), e);
        }
    }

    public static ArrayList<ServicesModel> getAllServices(boolean is_active) throws SQLException {
        try {
            return serviceDAO.getAllServices(is_active);
        } catch (SQLException e) {
            throw new SQLException("Failed to load services list. " + e.getMessage(), e);
        }
    }

    public static boolean disableServiceById(int service_id,String service_name) throws SQLException {
        if (service_id <= 0) return false;
        try {
            return serviceDAO.updateServiceStatusById(service_id, false) == 1;
        } catch (SQLException e) {
            throw new SQLException("Failed to disable service " + service_name + "\n" + e.getMessage(), e);
        }
    }

    public static boolean enableServiceById(int service_id,String service_name) throws SQLException {
        if (service_id <= 0) return false;
        try {
            return serviceDAO.updateServiceStatusById(service_id, true) == 1;
        } catch (SQLException e) {
            throw new SQLException("Failed to enable service " + service_name + "\n" + e.getMessage(), e);
        }
    }

    public static boolean updateServiceById(int service_id, String updatedservicename, String service_name) throws SQLException {
        if (service_id <= 0 || updatedservicename == null || updatedservicename.isBlank() || updatedservicename.equals(service_name)) return false;
        try {
            return serviceDAO.updateServiceById(service_id, updatedservicename) == 1;
        } catch (SQLException e) {
            throw new SQLException("Failed to update service '" + service_name + "'. " + e.getMessage(), e);
        }
    }

}