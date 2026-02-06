package com.billingapp.model;

public class AdminModel {
    private final int admin_id;
    private final String admin_email;
    private final String admin_password;
    private final String salt;

    public AdminModel(int admin_id,String admin_email, String admin_password,String salt){
        this.admin_id = admin_id;
        this.admin_email = admin_email;
        this.admin_password = admin_password;
        this.salt = salt;
    }

    public int getAdminId(){
        return admin_id;
    }

    public String getAdminEmail(){
        return admin_email;
    }

    public String getAdminPassword(){
        return admin_password;
    }

    public String getSalt(){
        return salt;
    }
}