package com.billingapp.model;

import java.math.BigDecimal;

public class BillItems {
    private String service_name;
    private String subservice_name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;

    public BillItems(String service_name, String subservice_name, int quantity, BigDecimal price, BigDecimal total) {
        this.service_name = service_name;
        this.subservice_name = subservice_name;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getServiceName() {
        return service_name;
    }

    public String getSubServiceName() {
        return subservice_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotal() {
        return total;
    }
}