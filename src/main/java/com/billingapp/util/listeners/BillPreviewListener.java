package com.billingapp.util.listeners;

import com.billingapp.model.BillPreviewDTO;

public interface BillPreviewListener {
    void onAddPreviewBill(BillPreviewDTO dto);
}
