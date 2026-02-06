package com.billingapp.util.listeners;

import java.util.ArrayList;

import com.billingapp.model.BillsView;

public interface BillListener {
    void onBilled();
    void onBillFiltered(ArrayList<BillsView> list);
}
