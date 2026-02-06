package com.billingapp.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.billingapp.db.SubServiceDAO;
import com.billingapp.model.SubServicesModel;
import com.billingapp.model.SubServicesView;

public class SubServices {

    private static SubServiceDAO subServicesDAO = new SubServiceDAO();

    public static boolean addSubService(String subservice_name, double subservice_price, int service_id)
            throws SQLException {
        if (service_id <= 0 || subservice_name == null || subservice_name.isBlank() || subservice_price <= 0)
            return false;
        try {
            return subServicesDAO.addSubService(subservice_name, subservice_price, service_id) > 0;
        } catch (Exception e) {
            throw new SQLException("Failed to add sub service " + subservice_name + "\n" + e.getMessage());
        }
    }

    public static ArrayList<SubServicesView> getAllSubServicesWithServices() throws SQLException {
        try {
            return subServicesDAO.getAllSubServicesWithServices();
        } catch (Exception e) {
            throw new SQLException("Failed to load sub services list. "+ "\n"  + e.getMessage());
        }
    }

    public static ArrayList<SubServicesModel> getAllSubServicesByServiceId(int service_id) throws SQLException {
        if (service_id == 0)
            return null;
        try {
            return subServicesDAO.getAllSubServicesByServiceId(service_id);
        } catch (Exception e) {
            throw new SQLException("Failed to load subservice list "+ "\n"  + e.getMessage());
        }
    }

    public static boolean disableSubServiceById(int service_id, int subservice_id, String subservice_name) throws SQLException {
        if (service_id <= 0 || subservice_id <= 0) {
            return false;
        }
        try {
            return subServicesDAO.updateSubServiceActiveStateById(service_id, subservice_id, false) == 1;
        } catch (Exception e) {
           throw new SQLException("Failed to disable subservice " + subservice_name + "\n" + e.getMessage());
        }
    }

    public static boolean enableSubServiceById(int service_id, int subservice_id, String subservice_name) throws SQLException {
        if (service_id <= 0 || subservice_id <= 0) {
            return false;
        }
        try {
            return subServicesDAO.updateSubServiceActiveStateById(service_id, subservice_id, true) == 1;
        } catch (Exception e) {
            throw new SQLException("Failed to enable subservice " + subservice_name + "\n" + e.getMessage());
        }
    }

    public static boolean updateSubServiceById(int subservice_id, String subservice_name, double subservice_price,
            int service_id) throws SQLException{
        if (subservice_id <= 0 || subservice_name == null || subservice_name.isBlank() || subservice_price <= 0
                || service_id <= 0)
            return false;
        try {
            return subServicesDAO.updateSubServiceById(subservice_id, subservice_name, subservice_price, service_id) == 1;
        } catch (Exception e) {
            throw new SQLException("Failed to update sub service '" + subservice_name + "'. "+ "\n"  + e.getMessage());
        }
    }

}