package com.billingapp.model;

import java.math.BigDecimal;

public class BillsView {
    private int bill_id;
    private String invoice_number;
    private String service_name;
    private String subservice_name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;
    private String customer_name;
    private String customer_mobile_number;
    private String customer_address;
    private String bill_date;
    private String paidby;

    public BillsView(int bill_id, String invoice_number, String service_name, String subservice_name, int quantity,
            BigDecimal price, BigDecimal total, String customer_name, String customer_mobile_number,
            String customer_address, String bill_date,
            String paidby) {
        this.bill_id = bill_id;
        this.invoice_number = invoice_number;
        this.service_name = service_name;
        this.subservice_name = subservice_name;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.customer_name = customer_name;
        this.customer_mobile_number = customer_mobile_number;
        this.customer_address = customer_address;
        this.bill_date = bill_date;
        this.paidby = paidby;
    }

    public int getBillId() {
        return bill_id;
    }

    public String getInvoiceNumber() {
        return invoice_number;
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

    public String getCustomerName() {
        return customer_name;
    }

    public String getCustomerMobileNumber() {
        return customer_mobile_number;
    }

    public String getCustomerAddress() {
        return customer_address;
    }

    public String getBillDate() {
        return bill_date;
    }

    public String getPaidBy() {
        return paidby;
    }

}
