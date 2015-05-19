package com.example.marco.kamcordchallenge.context;

import android.app.Application;
import android.content.Context;

/**
 * Created by Marco on 2/9/2015.
 */
//Just a class to retrieve the ApplicationContext for our layoutInflater.
public class MyApplication extends Application{
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
