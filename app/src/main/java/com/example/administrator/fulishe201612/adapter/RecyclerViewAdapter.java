package com.example.administrator.fulishe201612.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/15.
 */
 public  class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<NewGoodsBean> goodsList;

    public RecyclerViewAdapter(Context context, ArrayList<NewGoodsBean> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.xinpin_detailinfo, null);


        return new ContentHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewGoodsBean goodsBean = goodsList.get(position);

       ContentHolder contentHolder = (ContentHolder) holder;
        contentHolder.textGoodsprice.setText(goodsBean.getCurrencyPrice());
        contentHolder.textGoodsmoreinfo.setText(goodsBean.getGoodsBrief());
        contentHolder.textGoodsinfo.setText(goodsBean.getGoodsName());
        ImageLoader.downloadImg(context, contentHolder.imageGoods, goodsBean.getGoodsImg());
    }

    @Override
    public int getItemCount() {
        return goodsList.size();

    }

    public void initContact(ArrayList<NewGoodsBean> goodsList) {
        this.goodsList.clear();
        this.goodsList.addAll(goodsList);
        notifyDataSetChanged();

    }

    public void addAllContact(ArrayList<NewGoodsBean> goodsList) {
        this.goodsList.addAll(goodsList);

        notifyDataSetChanged();

    }

    class ContentHolder extends RecyclerView.ViewHolder {
        ImageView imageGoods;
        TextView textGoodsinfo, textGoodsmoreinfo, textGoodsprice;

        public ContentHolder(View itemView) {
            super(itemView);
            imageGoods = (ImageView) itemView.findViewById(R.id.imageGoods);
            textGoodsinfo = (TextView) itemView.findViewById(R.id.text_Goodsinfo);
            textGoodsmoreinfo = (TextView) itemView.findViewById(R.id.text_goodsmoreinfo);
            textGoodsprice = (TextView) itemView.findViewById(R.id.text_Goods_price);
        }
    }
}