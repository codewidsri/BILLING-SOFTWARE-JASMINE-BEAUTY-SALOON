package com.billingapp.util.eventmanagers;

import java.util.ArrayList;

import com.billingapp.util.listeners.UpdateChangeListener;

public class UpdateChangeEventManager {
    private static final ArrayList<UpdateChangeListener> listeners = new ArrayList<>();

    public static void addListener(UpdateChangeListener listener){
        listeners.add(listener);
    }

    public static void removeListener(UpdateChangeListener listener){
        listeners.remove(listener);
    }

    public static void notifyUpdated(){
        for (UpdateChangeListener l : listeners) {
            l.onUpdated();
        }
    }
}