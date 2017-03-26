package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.CartBean;
import com.example.administrator.fulishe201612.model.bean.MessageBean;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/26.
 */

public class CartModel implements ICartModel {
    @Override
    public void loadData(Context context, String username, OnCompleteListener<CartBean[]> listener) {
        OkHttpUtils<CartBean[]> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME, username)
                .targetClass(CartBean[].class).execute(listener);

    }

    @Override
    public void cartAction(Context context, int action, String cartId,
                           String username, String goodsId, int count, OnCompleteListener<MessageBean> listener) {

        OkHttpUtils<MessageBean> okHttpUtils = new OkHttpUtils<>(context);
        if (action == I.ACTION_CART_ADD) {
            addCart(okHttpUtils, username, goodsId, listener);
        }
        if (action == I.ACTION_CART_DEL) {
            delCart(okHttpUtils, cartId, listener);
        }
        if (action == I.ACTION_CART_UPDATA) {
            updaeCart(okHttpUtils, cartId, count, listener);
        }
    }

    private void updaeCart(OkHttpUtils<MessageBean> okHttpUtils, String cartId, int count, OnCompleteListener<MessageBean> listener) {
        okHttpUtils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID, cartId)
                .addParam(I.Cart.COUNT, String.valueOf(count))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    private void delCart(OkHttpUtils<MessageBean> okHttpUtils, String cartId, OnCompleteListener<MessageBean> listener) {
        okHttpUtils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID, cartId)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    private void addCart(OkHttpUtils<MessageBean> okHttpUtils, String username, String goodsId, OnCompleteListener<MessageBean> listener) {
        okHttpUtils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.USER_NAME, username)
                .addParam(I.Cart.GOODS_ID, goodsId)
                .addParam(I.Cart.COUNT, String.valueOf(1))
                .addParam(I.Cart.IS_CHECKED, String.valueOf(0))
                .targetClass(MessageBean.class)
                .execute(listener);

    }
}
