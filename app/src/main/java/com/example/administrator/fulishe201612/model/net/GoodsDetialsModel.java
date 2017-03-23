package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.GoodsDetailsBean;
import com.example.administrator.fulishe201612.model.bean.MessageBean;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/16.
 */

public class GoodsDetialsModel implements IGoodsDetialsModel {
    @Override
    public void loadData(Context context, int goodsId, OnCompleteListener<GoodsDetailsBean> listener) {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Goods.KEY_GOODS_ID, goodsId + "")
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    @Override
    public void isCollect(Context context, String username, int goodsId, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.USER_NAME, username)
                .addParam(I.Goods.KEY_GOODS_ID, goodsId + "")
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
