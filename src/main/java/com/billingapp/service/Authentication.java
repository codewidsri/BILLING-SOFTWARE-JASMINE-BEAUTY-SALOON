package com.billingapp.service;

import java.sql.SQLException;
import java.util.Base64;

import com.billingapp.db.AdminDAO;
import com.billingapp.model.AdminModel;
import com.billingapp.util.PasswordUtil;

public class Authentication {

    private static final AdminDAO adminDAO = new AdminDAO();

    public static boolean login(String email, char[] password) throws SQLException {
        if (email == null || password == null || email.isBlank() || password.length == 0) {
            return false;
        }
        try {
            AdminModel admin = adminDAO.findbyemail(email);
            if (admin == null) {
                return false;
            }
            boolean valid = PasswordUtil.verifyPassword(password, admin.getAdminPassword(), admin.getSalt());
            return valid;
        } catch (Exception e) {
            throw new SQLException("Login Failed " + "\n" + e.getMessage());
        }
    }

    public static boolean register(String email, char[] password) throws SQLException {
        if (email == null || password == null || email.isBlank() || password.length == 0) {
            return false;
        }
        try {
            byte[] salt = PasswordUtil.generateSalt();
            String hashedpassword = PasswordUtil.hashPassword(password, salt);
            int result = adminDAO.insert(email, hashedpassword, Base64.getEncoder().encodeToString(salt));
            return result > 0;
        } catch (Exception e) {
            throw new SQLException("Registration Failed " + "\n" + e.getMessage());
        }
    }

}