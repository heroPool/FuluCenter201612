package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.GoodsDetailsBean;
import com.example.administrator.fulishe201612.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface IGoodsDetialsModel {
    void loadData(Context context, int goodsId, OnCompleteListener<GoodsDetailsBean> listener);

    void isCollect(Context context, int action, String username, int goodsId, OnCompleteListener<MessageBean> listener);
}
