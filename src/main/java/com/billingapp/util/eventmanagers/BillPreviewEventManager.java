package com.billingapp.util.eventmanagers;

import java.util.ArrayList;

import com.billingapp.model.BillPreviewDTO;
import com.billingapp.util.listeners.BillPreviewListener;

public class BillPreviewEventManager {
    private static final ArrayList<BillPreviewListener> listeners = new ArrayList<>();

    public static void addListener(BillPreviewListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(BillPreviewListener listener) {
        listeners.remove(listener);
    }

    public static void notifyAdd(BillPreviewDTO dto){
        for (BillPreviewListener listener : listeners) {
            listener.onAddPreviewBill(dto);
        }
    }
}