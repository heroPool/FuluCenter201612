package com.example.administrator.fulishe201612.model.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ShowToastUtils {
    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void showToast(Context context,
                                 String content, int showTimeLength) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    showTimeLength);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
