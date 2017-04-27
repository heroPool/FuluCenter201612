package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.MessageBean;
import com.example.administrator.fulishe201612.model.bean.Result;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/3/20.
 */

public class UserModel implements IUserModel {
    @Override
    public void login(Context context, String userName, String passWord, OnCompleteListener<String> listener) {
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME, userName)
                .addParam(I.User.PASSWORD, passWord)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void register(Context context, String userName, String nickName, String passWord, OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME, userName)
                .addParam(I.User.PASSWORD, passWord)
                .addParam(I.User.NICK, nickName)
                .targetClass(Result.class)
                .post()

                .execute(listener);
    }

    @Override
    public void updateNick(Context context, String username, String newnick, OnCompleteListener<String> listener) {
        OkHttpUtils<String> okhttputils = new OkHttpUtils<>(context);
        okhttputils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.NICK, newnick)
                .targetClass(String.class)
                .execute(listener);

    }

    @Override
    public void uploadAvatar(Context context, String username, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID, username)
                .addParam(I.AVATAR_TYPE, I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    @Override
    public void isCollectGoods(Context context, String username, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> okHttp = new OkHttpUtils<>(context);
        okHttp.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME, username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

}
