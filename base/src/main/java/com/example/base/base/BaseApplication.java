package com.example.base.base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * @User Xiahangli
 * @Date 2019-12-04  17:22
 * @Email henryatxia@gmail.com
 * @Descrip
 */
public class BaseApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
