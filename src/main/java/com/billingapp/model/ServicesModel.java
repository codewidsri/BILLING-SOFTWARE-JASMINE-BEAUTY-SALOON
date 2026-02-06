package com.billingapp.model;

public class ServicesModel {
    private final int service_id;
    private final String service_name;
    private final boolean is_active;

    public ServicesModel(int service_id, String service_name, boolean is_active){
        this.service_id = service_id;
        this.service_name = service_name;
        this.is_active = is_active;
    }

    public int getServiceId(){
        return service_id;
    }

    public String getServiceName(){
        return service_name;
    }

    public boolean getIsActive(){
        return is_active;
    }

}