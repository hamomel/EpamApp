package com.hamom.epamapp.utils;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by hamom on 12.11.17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
