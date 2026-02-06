package com.billingapp.model;

import java.util.ArrayList;

public class Bill {
    private int bill_id;
    private String invoice_number;
    private String customer_name;
    private String customer_mobile_number;
    private String customer_address;
    private String date;
    private ArrayList<BillItems> billitems;

    public Bill(int bill_id, String invoice_number, String customer_name, String customer_mobile_number,
            String customer_address, String date,
            ArrayList<BillItems> billtems) {
        this.bill_id = bill_id;
        this.invoice_number = invoice_number;
        this.customer_name = customer_name;
        this.customer_mobile_number = customer_mobile_number;
        this.customer_address = customer_address;
        this.date = date;
        this.billitems = billtems;
    }

    public int getBillId(){
        return bill_id;
    }

    public String getInvoiceNumber() {
        return invoice_number;
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

    public String getDate() {
        return date;
    }

    public ArrayList<BillItems> getBillItems() {
        return billitems;
    }
}
