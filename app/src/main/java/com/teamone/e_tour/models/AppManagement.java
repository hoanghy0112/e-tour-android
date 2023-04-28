package com.teamone.e_tour.models;

import android.content.Context;

import com.teamone.e_tour.utils.MySharedPreferences;

public class AppManagement {
    private Context context;
    static AppManagement instance;
    private Boolean isFirstTime;


    public AppManagement(Context context) {
        this.context = context;
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        this.isFirstTime = mySharedPreferences.getSharedPreferences().getBoolean("isFirstTime", true);
    }

    public void setFirstTime(Boolean firstTime) {
        isFirstTime = firstTime;
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        mySharedPreferences.getEditor().putBoolean("isFirstTime", firstTime);
    }

    public static AppManagement getInstance(Context context) {
        if (instance == null) {
            instance = new AppManagement(context);
        }
        return instance;
    }

    public Boolean getFirstTime() {
        return isFirstTime;
    }
}
