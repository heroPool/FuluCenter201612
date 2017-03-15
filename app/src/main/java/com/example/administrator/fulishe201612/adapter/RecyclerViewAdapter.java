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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_CONTACT = 1;
    public static final int TYPE_FOOTER = 2;

    Context context;
    ArrayList<NewGoodsBean> goodsList;

    String textFooter;
    boolean isMore = true;

    public String getTextFooter() {
        return textFooter;
    }

    public void setTextFooter(String textFooter) {
        this.textFooter = textFooter;
        notifyDataSetChanged();

    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public RecyclerViewAdapter(Context context, ArrayList<NewGoodsBean> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View inflate = View.inflate(context, R.layout.xinppin_footer, null);
            return new FooterHolder(inflate);
        }

        View inflate = View.inflate(context, R.layout.xinpin_detailinfo, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            FooterHolder footerHolder = (FooterHolder) holder;
            footerHolder.textviewfooter.setText(textFooter);
            return;
        }
        NewGoodsBean goodsBean = goodsList.get(position);

        ViewHolder contentHolder = (ViewHolder) holder;
        contentHolder.textGoodsPrice.setText(goodsBean.getCurrencyPrice());
        contentHolder.textGoodsmoreinfo.setText(goodsBean.getGoodsBrief());
        contentHolder.textGoodsinfo.setText(goodsBean.getGoodsName());
        ImageLoader.downloadImg(context, contentHolder.imageGoods, goodsBean.getGoodsImg());
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 1 == position) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTACT;

    }

    @Override
    public int getItemCount() {

        return goodsList != null ? goodsList.size() + 1 : 1;

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

//    class ContentHolder extends RecyclerView.ViewHolder {
//        ImageView imageGoods;
//        TextView textGoodsinfo, textGoodsmoreinfo, textGoodsprice;
//
//        public ContentHolder(View itemView) {
//            super(itemView);
//            imageGoods = (ImageView) itemView.findViewById(R.id.imageGoods);
//            textGoodsinfo = (TextView) itemView.findViewById(R.id.text_Goodsinfo);
//            textGoodsmoreinfo = (TextView) itemView.findViewById(R.id.text_goodsmoreinfo);
//            textGoodsprice = (TextView) itemView.findViewById(R.id.text_Goods_price);
//        }
//    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageGoods)
        ImageView imageGoods;
        @BindView(R.id.texttitle)
        TextView textGoodsinfo;
        @BindView(R.id.textname)
        TextView textGoodsmoreinfo;
        @BindView(R.id.text_Goods_price)
        TextView textGoodsPrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FooterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textfooter)
        TextView textviewfooter;

        FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}