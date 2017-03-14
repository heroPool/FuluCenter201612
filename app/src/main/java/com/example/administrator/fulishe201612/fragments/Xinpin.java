package com.example.administrator.fulishe201612.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class Xinpin extends Fragment {
    private static final int ACTION_DOWNLOAD = 1;
    private static final int ACTION_PULL_DOWN = 2;
    private static final int ACTION_PULL_UP = 3;

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<NewGoodsBean> newGoodsBeenList;
    SwipeRefreshLayout swiperefresh;

    public Xinpin() {
        // Required empty public constructor
    }

    int pageId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_xinpin, container, false);
        initView(inflate);
        downLoadContactList(ACTION_PULL_DOWN, pageId);
        setListener();
        return inflate;

    }

    private void setListener() {
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ImageLoader.release();
                swiperefresh.setRefreshing(true);
                pageId = 1;
                downLoadContactList(ACTION_PULL_DOWN, pageId);

            }
        });
    }

    private void downLoadContactList(final int action, int pageId) {
        final OkHttpUtils<NewGoodsBean[]> okHttpUtils = new OkHttpUtils<>(getActivity());
        okHttpUtils.url(I.SERVER_ROOT + I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, I.CAT_ID + "")
                .addParam(I.PAGE_ID, pageId + "")
                .addParam(I.PAGE_SIZE, "8")
                .targetClass(NewGoodsBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        Log.i("mingYue", "onSuccess: " + Arrays.toString(result));

                        ArrayList<NewGoodsBean> newGoodsBeen = okHttpUtils.array2List(result);
                        switch (action) {
                            case ACTION_PULL_DOWN:
                                recyclerViewAdapter.initContact(newGoodsBeen);
                        }


                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void initView(View inflate) {
        swiperefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swiperefresh);
        newGoodsBeenList = new ArrayList<>();
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), newGoodsBeenList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
