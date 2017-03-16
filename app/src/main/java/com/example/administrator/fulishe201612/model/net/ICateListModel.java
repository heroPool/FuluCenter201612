package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.CategoryChildBean;
import com.example.administrator.fulishe201612.model.bean.CategoryGroupBean;
import com.example.administrator.fulishe201612.model.bean.GoodsDetailsBean;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface ICateListModel {
    void loadData(Context context, OnCompleteListener<CategoryGroupBean[]> listener);
    void loadData(Context context, int parentId, OnCompleteListener<CategoryChildBean[]> listener);
}
