package com.easydb.mobile.easydb;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by liwy on 2017/7/14.
 */

public class LiwyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
