package com.example.administrator.fulishe201612.model.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.fulishe201612.application.FuLiCenterApplication;

/**
 * Created by Administrator on 2017/3/21.
 */

public class SharePreferenceUtils {
    public static final String SHAREPREFERENCE_NAME = "com.example.administrator.fulishe201612_save_userinfo";
    public static final String SAVE_USERINFO_USERNAME = "m_user_username";
    static SharePreferenceUtils instance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharePreferenceUtils() {


        sharedPreferences = FuLiCenterApplication.getInstance().getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharePreferenceUtils getinstance() {
        if (instance == null) {
            instance = new SharePreferenceUtils();

        }
        return instance;
    }

    public void setUserName(String username) {
        editor.putString(SAVE_USERINFO_USERNAME, username).commit();
    }

    public String getUserName() {
        String string = sharedPreferences.getString(SAVE_USERINFO_USERNAME, "0");
        return string;

    }

    public void removeuser() {
        editor.remove(SAVE_USERINFO_USERNAME).commit();
    }


}
