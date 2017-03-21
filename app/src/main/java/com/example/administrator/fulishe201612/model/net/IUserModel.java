package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;
import com.example.administrator.fulishe201612.model.bean.Result;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface IUserModel {
    void login(Context context, String userName, String passWord, OnCompleteListener<String> listener);

    void register(Context context, String userName, String nickName, String passWord, OnCompleteListener<Result> listener);
}
