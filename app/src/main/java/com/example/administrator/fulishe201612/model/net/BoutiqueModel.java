package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.BoutiqueBean;
import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/15.
 */

public class BoutiqueModel implements IBoutiqueModel {
    @Override
    public void loadData(Context context, OnCompleteListener<BoutiqueBean[]> listener) {
        OkHttpUtils<BoutiqueBean[]> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }
}
