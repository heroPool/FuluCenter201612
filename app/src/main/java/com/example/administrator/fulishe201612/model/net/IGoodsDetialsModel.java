package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.BoutiqueBean;
import com.example.administrator.fulishe201612.model.bean.GoodsDetailsBean;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface IGoodsDetialsModel {
    void loadData(Context context,int goodsId, OnCompleteListener<GoodsDetailsBean> listener);
}
