package com.example.administrator.fulishe201612.model.utils;

import android.widget.Toast;

import com.example.administrator.fulishe201612.application.FuLiCenterApplication;


public class CommonUtils {
    public static void showLongToast(String msg){
        Toast.makeText(FuLiCenterApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
    }
    public static void showShortToast(String msg){
        Toast.makeText(FuLiCenterApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(int rId){
        showLongToast(FuLiCenterApplication.getInstance().getString(rId));
    }
    public static void showShortToast(int rId){
        showShortToast(FuLiCenterApplication.getInstance().getString(rId));
    }
}
