package com.billingapp.model;

public class SubServicesView {
    private final int subservice_id;
    private final String subservice_name;
    private final double subservice_price;
    private final int service_id;
    private final String service_name;
    private final boolean is_active;

    public SubServicesView(int subservice_id,String subservice_name, double subservice_price, int service_id, String service_name, boolean is_active){
        this.subservice_id = subservice_id;
        this.subservice_name = subservice_name;
        this.subservice_price = subservice_price;
        this.service_id = service_id;
        this.service_name = service_name;
        this.is_active = is_active;
    }

    public int getSubServiceId(){
        return subservice_id;
    }

    public String getSubServiceName(){
        return subservice_name;
    }

    public double getSubServicePrice(){
        return subservice_price;
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