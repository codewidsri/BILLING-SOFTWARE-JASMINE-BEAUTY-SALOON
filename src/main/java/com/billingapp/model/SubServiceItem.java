package com.billingapp.model;

public class SubServiceItem {
    private int subservice_id;
    private String subservice_name;
    private double subservice_price;

    public SubServiceItem(int subservice_id, String subservice_name, double subservice_price) {
        this.subservice_id = subservice_id;
        this.subservice_name = subservice_name;
        this.subservice_price = subservice_price;
    }

    public int getSubServiceId() {
        return subservice_id;
    }

    public double getSubServicePrice(){
        return subservice_price;
    }

    @Override
    public String toString() {
        return subservice_name;
    }
}
