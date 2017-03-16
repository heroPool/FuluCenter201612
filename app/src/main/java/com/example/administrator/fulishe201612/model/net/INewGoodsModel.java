package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface INewGoodsModel{
    void loadData(Context context, int cat_id,int pageId, OnCompleteListener<NewGoodsBean[]> listener);

}
