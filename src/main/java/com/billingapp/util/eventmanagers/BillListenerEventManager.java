package com.billingapp.util.eventmanagers;

import java.util.ArrayList;

import com.billingapp.model.BillsView;
import com.billingapp.util.listeners.BillListener;

public class BillListenerEventManager {
    public static final ArrayList<BillListener> listeners = new ArrayList<>();

    public static void addListener(BillListener listener){
        listeners.add(listener);
    }

    public static void removeListener(BillListener listener){
        listeners.remove(listener);
    }

    public static void notifyBilled(){
        for (BillListener billListener : listeners) {
            billListener.onBilled();
        }
    }

    public static void notifyBillFiltered(ArrayList<BillsView> list){
        for (BillListener billListener : listeners) {
            billListener.onBillFiltered(list);
        }
    }
}
