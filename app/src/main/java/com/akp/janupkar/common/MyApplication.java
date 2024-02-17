package com.akp.janupkar.common;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {

    private static MyApplication mainApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;



        //Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        //MultiDex.install(this);
    }


    public static synchronized MyApplication getInstance() {
        return mainApplication;
    }
}
