package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.adapter.ExpandableAdapter;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.CategoryChildBean;
import com.example.administrator.fulishe201612.model.bean.CategoryGroupBean;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;


/**
 * Created by Administrator on 2017/3/16.
 */
public class CateListModel implements ICateListModel {
    ExpandableAdapter expandableAdapter;


    @Override
    public void loadData(Context context, OnCompleteListener<CategoryGroupBean[]> listener) {
        OkHttpUtils<CategoryGroupBean[]> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);

    }

    @Override
    public void loadData(Context context, int parentId, OnCompleteListener<CategoryChildBean[]> listener) {
        OkHttpUtils<CategoryChildBean[]> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, parentId + "")
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }
}
