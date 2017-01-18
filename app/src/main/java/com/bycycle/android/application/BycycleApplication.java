package com.bycycle.android.application;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Ashish Kumar Khatri on 19/12/16.
 */

public class BycycleApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
        mContext= getApplicationContext();
    }
}
