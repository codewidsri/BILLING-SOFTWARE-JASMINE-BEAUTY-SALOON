package com.billingapp.model;

public class ServiceItem{
    private int service_id;
    private String service_name;

    public ServiceItem(int service_id, String service_name) {
        this.service_id = service_id;
        this.service_name = service_name;
    }

    public int getServiceId() {
        return service_id;
    }

    @Override
    public String toString() {
        return service_name;
    }

}
