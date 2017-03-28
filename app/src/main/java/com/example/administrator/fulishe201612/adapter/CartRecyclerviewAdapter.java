package com.example.administrator.fulishe201612.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.model.bean.CartBean;
import com.example.administrator.fulishe201612.model.bean.GoodsDetailsBean;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/24.
 */

public class CartRecyclerviewAdapter extends RecyclerView.Adapter {


    Context context;
    ArrayList<CartBean> cartBeanArrayList;
    CompoundButton.OnCheckedChangeListener listener;
    View.OnClickListener cartUpdatelistener;

    public void setCartUpdatelistener(View.OnClickListener cartUpdatelistener) {
        this.cartUpdatelistener = cartUpdatelistener;
    }

    public CartRecyclerviewAdapter(Context context, ArrayList<CartBean> cartBeanArrayList) {
        this.context = context;
        this.cartBeanArrayList = cartBeanArrayList;
    }

    public void setListener(CompoundButton.OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(context).inflate(R.layout.item_cart, null);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartBeanArrayList != null ? cartBeanArrayList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkBox_cart_isCheck)
        CheckBox checkBoxCartIsCheck;
        @BindView(R.id.image_cart_goods)
        ImageView imageCartGoods;
        @BindView(R.id.text_cart_goodsinfo)
        TextView textCartGoodsinfo;
        @BindView(R.id.image_cart_add)
        ImageView imageCartAdd;
        @BindView(R.id.edit_cart_goods_num)
        EditText editCartGoodsNum;
        @BindView(R.id.image_cart_sub)
        ImageView imageCartSub;
        @BindView(R.id.text_cart_item_price)
        TextView textCartItemPrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            CartBean cartBean = cartBeanArrayList.get(position);
            checkBoxCartIsCheck.setChecked(cartBean.isChecked());
            GoodsDetailsBean goods = cartBean.getGoods();
            editCartGoodsNum.setText(String.valueOf(cartBean.getCount()));
            if (goods != null) {
                textCartItemPrice.setText(goods.getCurrencyPrice());
                textCartGoodsinfo.setText(goods.getGoodsName());
                ImageLoader.downloadImg(context, imageCartGoods, goods.getGoodsThumb());
//                Glide.with(context).load(goods.getGoodsThumb()).into(imageCartGoods);
            }
            checkBoxCartIsCheck.setTag(position);
            checkBoxCartIsCheck.setOnCheckedChangeListener(listener);

            imageCartAdd.setTag(position);
            imageCartAdd.setTag(R.id.action_add_cart, 1);
            imageCartAdd.setOnClickListener(cartUpdatelistener);

            imageCartSub.setTag(position);
            imageCartSub.setTag(R.id.action_del_cart,-1);
            imageCartSub.setOnClickListener(cartUpdatelistener);

        }
    }
}
