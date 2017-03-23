package com.example.administrator.fulishe201612.model.net;

import android.content.Context;

import com.example.administrator.fulishe201612.model.bean.CollectBean;
import com.example.administrator.fulishe201612.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/3/23.
 */

public interface ICollectModel {
    void loadCollectGoods(Context context, String username, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener);

    void deleteCollect(Context context, String username, int goodsId, OnCompleteListener<MessageBean> listener);
}
