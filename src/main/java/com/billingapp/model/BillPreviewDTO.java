package com.billingapp.model;

public class BillPreviewDTO {
    private ServiceItem serviceItem;
    private SubServiceItem subServiceItem;
    private int quantity;
    private double total;
    private String paidby;
    private String customer_name;
    private String customer_address;
    private String customer_mobile_number;

    public BillPreviewDTO(ServiceItem serviceItem, SubServiceItem subServiceItem, int quantity, double total, String paidby, String customer_name,String customer_mobile_number, String customer_address) {
        this.serviceItem = serviceItem;
        this.subServiceItem = subServiceItem;
        this.quantity = quantity;
        this.total = total;
        this.paidby = paidby;
        this.customer_name = customer_name;
        this.customer_mobile_number = customer_mobile_number;
        this.customer_address = customer_address;
    }

    public int getServiceId() {
        return serviceItem.getServiceId();
    }

    public String getServiceName() {
        return serviceItem.toString();
    }

    public int getSubserviceId() {
        return subServiceItem.getSubServiceId();
    }

    public String getSubserviceName() {
        return subServiceItem.toString();
    }

    public double getSubservicePrice() {
        return subServiceItem.getSubServicePrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public String getPaidby() {
        return paidby;
    }

    public String getCustomerName() {
        return customer_name;
    }

    public String getCustomerAddress() {
        return customer_address;
    }

    public String getCustomerMobileNumber() {
        return customer_mobile_number;
    }

}