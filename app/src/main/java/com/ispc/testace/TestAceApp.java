package com.ispc.testace;

import android.app.Application;

import com.ispc.testace.data.local.TokenManager;

public class TestAceApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TokenManager.init(this);
    }
}
