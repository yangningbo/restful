package com.hello1987.restful;

import android.app.Application;
import android.content.Context;

public class RESTful extends Application {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mAppContext;
    }

}
