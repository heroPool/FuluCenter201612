package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/17.
 */

public class FindGoodsDetails implements INewGoodsModel {
    @Override
    public void loadData(Context context, int cat_id, int pageId, OnCompleteListener listener) {
        OkHttpUtils<NewGoodsBean[]> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.SERVER_ROOT + I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(cat_id))
                .addParam(I.PAGE_ID, pageId + "")
                .addParam(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT + "")
                .targetClass(NewGoodsBean[].class)
                .execute(listener);

    }
}
