package com.example.administrator.fulishe201612.application;

import android.app.Application;
import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.User;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FuLiCenterApplication extends Application {

    public static FuLiCenterApplication application;
    private static FuLiCenterApplication instance;


    private static String userName;
    private static User user;

    public static FuLiCenterApplication getInstance() {
        if (instance == null) {
            instance = new FuLiCenterApplication();
        }
        return instance;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        FuLiCenterApplication.userName = userName;
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

        application = this;
        instance = this;
    }


}
