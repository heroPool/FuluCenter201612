package com.example.administrator.fulishe201612.adapter;

/**
 * Created by Administrator on 2017/3/15.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.BoutiqueBean;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.ui.activity.BoutiqueAndList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by Administrator on 2017/3/15.
 */
public class RecyclerViewAdapterBou extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;
    ArrayList<BoutiqueBean> goodsList;




    public RecyclerViewAdapterBou(Context context, ArrayList<BoutiqueBean> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View inflate = View.inflate(context, R.layout.item_boutique, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final BoutiqueBean boutiqueBean = goodsList.get(position);
        viewHolder.textname.setText(boutiqueBean.getName());
        viewHolder.texttitle.setText(boutiqueBean.getTitle());
        viewHolder.textdescription.setText(boutiqueBean.getDescription());

        ImageLoader.downloadImg(context, viewHolder.imageGoods, boutiqueBean.getImageurl());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BoutiqueAndList.class)
                        .putExtra(I.NewAndBoutiqueGoods.CAT_ID, boutiqueBean.getId())
                        .putExtra("title",boutiqueBean.getTitle())
                );
            }
        });

}

    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @Override
    public int getItemCount() {

        return goodsList != null ? goodsList.size() : 1;

    }

    public void initContact(ArrayList<BoutiqueBean> goodsList) {
        this.goodsList.clear();
        this.goodsList.addAll(goodsList);
        notifyDataSetChanged();

    }

    public void addAllContact(ArrayList<BoutiqueBean> goodsList) {
        this.goodsList.addAll(goodsList);

        notifyDataSetChanged();

    }

    static class ViewHolder  extends RecyclerView.ViewHolder{
        @BindView(R.id.imageGoods)
        ImageView imageGoods;
        @BindView(R.id.texttitle)
        TextView texttitle;
        @BindView(R.id.textname)
        TextView textname;
        @BindView(R.id.textdescription)
        TextView textdescription;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}