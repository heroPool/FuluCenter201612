package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.MessageBean;
import com.example.administrator.fulishe201612.model.bean.Result;

import java.io.File;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface IUserModel {
    void login(Context context, String userName, String passWord, OnCompleteListener<String> listener);

    void register(Context context, String userName, String nickName, String passWord, OnCompleteListener<Result> listener);
    void updateNick(Context context, String username, String newnick, OnCompleteListener<String> listener);
    void uploadAvatar(Context context, String username, File file, OnCompleteListener<String> listener);

    void isCollectGoods(Context context, String username, OnCompleteListener<MessageBean> listener);
}
