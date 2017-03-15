package com.example.administrator.fulishe201612.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.adapter.RecyclerViewAdapter;
import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;
import com.example.administrator.fulishe201612.model.net.INewGoodsModel;
import com.example.administrator.fulishe201612.model.net.NewGoodsModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

import java.util.ArrayList;

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
    GridSpacingItemDecoration gridSpacingItemDecoration;

    TextView textHint;
    INewGoodsModel iNewGoodsModel;
    GridLayoutManager gridLayoutManager;

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
        iNewGoodsModel = new NewGoodsModel();
        downLoadContactList(ACTION_PULL_DOWN, pageId);
        setListener();
        return inflate;

    }

    private void setListener() {
        //下拉刷新
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ImageLoader.release();


                swiperefresh.setRefreshing(true);
                textHint.setVisibility(View.VISIBLE);
                pageId = 1;
                downLoadContactList(ACTION_PULL_DOWN, pageId);

            }
        });
        //上拉加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int position = gridLayoutManager.findLastVisibleItemPosition();
                if (recyclerViewAdapter.isMore() && recyclerViewAdapter.getItemCount() - 1 == position && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageId++;
                    downLoadContactList(ACTION_PULL_UP, pageId);
                }
            }


        });

    }

    private void downLoadContactList(final int action, int pageId) {
        final OkHttpUtils<NewGoodsBean[]> okHttpUtils = new OkHttpUtils<>(getActivity());
        iNewGoodsModel.loadData(getActivity(), pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {

                recyclerViewAdapter.setMore(result != null && result.length > 0);
                if (!recyclerViewAdapter.isMore()) {
                    if (action == ACTION_PULL_UP) {
                        recyclerViewAdapter.setTextFooter("没有更多数据...");
                    }
                    return;
                }

                ArrayList<NewGoodsBean> newGoodsBeen = okHttpUtils.array2List(result);
                switch (action) {
                    case ACTION_PULL_DOWN:


                        recyclerViewAdapter.initContact(newGoodsBeen);
                        swiperefresh.setRefreshing(false);
                        textHint.setVisibility(View.GONE);
                        recyclerViewAdapter.setTextFooter("加载更多");

                        break;
                    case ACTION_PULL_UP:
                        recyclerViewAdapter.addAllContact(newGoodsBeen);

                        break;
                }
            }

            @Override
            public void onError(String error) {

            }
        });
//        final OkHttpUtils<NewGoodsBean[]> okHttpUtils = new OkHttpUtils<>(getActivity());
//        okHttpUtils.url(I.SERVER_ROOT + I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
//                .addParam(I.NewAndBoutiqueGoods.CAT_ID, I.CAT_ID + "")
//                .addParam(I.PAGE_ID, pageId + "")
//                .addParam(I.PAGE_SIZE, "8")
//                .targetClass(NewGoodsBean[].class)
//                .execute(new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
//                    @Override
//                    public void onSuccess(NewGoodsBean[] result) {
//                        Log.i("mingYue", "onSuccess: " + Arrays.toString(result));
//
//                        ArrayList<NewGoodsBean> newGoodsBeen = okHttpUtils.array2List(result);
//                        switch (action) {
//                            case ACTION_PULL_DOWN:
//                                recyclerViewAdapter.initContact(newGoodsBeen);
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
//                    }
//                });

    }

    private void initView(View inflate) {
        textHint = (TextView) inflate.findViewById(R.id.textHint);
        swiperefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swiperefresh);
        newGoodsBeenList = new ArrayList<>();
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), newGoodsBeenList);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = recyclerViewAdapter.getItemViewType(position);
                if (viewType == RecyclerViewAdapter.TYPE_FOOTER) {
                    return 2;
                }
                return 1;
            }
        });

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
