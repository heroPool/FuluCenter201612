package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.CartBean;
import com.example.administrator.fulishe201612.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/3/26.
 */

public interface ICartModel {
    void loadData(Context context, String username, OnCompleteListener<CartBean[]> listener);

    void cartAction(Context context, int action, String cartId, String username,
                    String goodsId, int count, OnCompleteListener<MessageBean> listener);
}
