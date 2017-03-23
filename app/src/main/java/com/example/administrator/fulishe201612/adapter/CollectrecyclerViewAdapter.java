package com.example.administrator.fulishe201612.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.CollectBean;
import com.example.administrator.fulishe201612.model.bean.MessageBean;
import com.example.administrator.fulishe201612.model.net.CollectModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.ui.activity.GoodsDetialsActivtiy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/23.
 */

public class CollectrecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    public static final int TYPE_CONTACT = 1;
    public static final int TYPE_FOOTER = 2;
    CollectModel collectModel;
    Context context;
    ArrayList<CollectBean> collectBeanArrayList;
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

    public CollectrecyclerViewAdapter(Context context, ArrayList<CollectBean> collectBeanArrayList) {
        this.context = context;
        this.collectBeanArrayList = collectBeanArrayList;
        collectModel = new CollectModel();
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View inflate = View.inflate(context, R.layout.xinppin_footer, null);
            return new ViewHolderFooter(inflate);
        }
        View inflate = View.inflate(context, R.layout.layout_collect_item, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            ViewHolderFooter footerHolder = (ViewHolderFooter) holder;
            footerHolder.textfooter.setText(textFooter);
            return;
        }
        final CollectBean goodsBean = collectBeanArrayList.get(position);

        ViewHolder contentHolder = (ViewHolder) holder;

        contentHolder.textname.setText(goodsBean.getGoodsName());

        ImageLoader.downloadImg(context, contentHolder.imageGoods, goodsBean.getGoodsImg());
        contentHolder.imageCollectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int goodsId = goodsBean.getGoodsId();
                String muserName = FuLiCenterApplication.getUser().getMuserName();
                collectModel.deleteCollect(context, muserName, goodsId, new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            collectBeanArrayList.remove(position);
                            notifyDataSetChanged();

//                            notifyItemRemoved(position);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });
        contentHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GoodsDetialsActivtiy.class)
                        .putExtra(I.Goods.KEY_GOODS_ID, goodsBean.getGoodsId())
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectBeanArrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (getItemCount() - 1 == position) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTACT;

    }

    public void initContact(ArrayList<CollectBean> goodsList) {
        this.collectBeanArrayList.clear();
        this.collectBeanArrayList.addAll(goodsList);
        notifyDataSetChanged();
    }

    public void addAllContact(ArrayList<CollectBean> goodsList) {
        this.collectBeanArrayList.addAll(goodsList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_collect_delete)
        ImageView imageCollectDelete;
        @BindView(R.id.imageGoods)
        ImageView imageGoods;
        @BindView(R.id.textname)
        TextView textname;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class ViewHolderFooter extends RecyclerView.ViewHolder {

        @BindView(R.id.textfooter)
        TextView textfooter;

        ViewHolderFooter(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
