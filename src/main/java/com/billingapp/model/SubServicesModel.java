package com.billingapp.model;

public class SubServicesModel {
    private final int service_id;
    private final int subservice_id;
    private final String subservice_name;
    private final double subservice_price;
    private final boolean is_active;

    public SubServicesModel(int subservice_id, String subservice_name, double subservice_price, int service_id, boolean is_active) {
        this.subservice_id = subservice_id;
        this.subservice_name = subservice_name;
        this.subservice_price = subservice_price;
        this.service_id = service_id;
        this.is_active = is_active;
    }

    public boolean getIsActive() {
        return is_active;
    }

    public int getSubserviceId() {
        return subservice_id;
    }

    public int getServiceId() {
        return service_id;
    }

    public String getSubserviceName() {
        return subservice_name;
    }

    public double getSubservicePrice() {
        return subservice_price;
    }

}