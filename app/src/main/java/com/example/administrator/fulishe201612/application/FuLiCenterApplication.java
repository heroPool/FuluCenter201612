package com.example.administrator.fulishe201612.application;

import android.app.Application;
import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.User;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FuLiCenterApplication extends Application {


    private static FuLiCenterApplication instance;


    private static User user;

    public static FuLiCenterApplication getInstance() {
        if (instance == null) {
            instance = new FuLiCenterApplication();
        }
        return instance;
    }


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        instance = this;
    }


}
