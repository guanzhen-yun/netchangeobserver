package com.inke.netchange;

import android.app.Application;

import com.inke.library.NetworkManager;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NetworkManager.getDefault().init(this);
    }
}
