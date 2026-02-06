package com.billingapp.model;

public class CustomersModel {
    private int customer_id;
    private String customer_name;
    private String customer_mobile_number;
    private String customer_address;

    public CustomersModel(int customer_id, String customer_name, String customer_mobile_number, String customer_address) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_mobile_number = customer_mobile_number;
        this.customer_address = customer_address;
    }

    public int getCustomer_id(){
        return customer_id;
    }

    public String getCustomer_name(){
        return customer_name;
    }

    public String getCustomer_mobile_number(){
        return customer_mobile_number;
    }

    public String getCustomer_address(){
        return customer_address;
    }
}
