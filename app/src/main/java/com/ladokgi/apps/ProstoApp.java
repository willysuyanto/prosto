package com.ladokgi.apps;

import android.app.Application;

import com.ladokgi.apps.data.SessionManager;

public class ProstoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.init(this);
    }
}
