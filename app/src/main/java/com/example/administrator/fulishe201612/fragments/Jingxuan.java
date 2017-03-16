package com.example.administrator.fulishe201612.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.adapter.RecyclerViewAdapter;
import com.example.administrator.fulishe201612.adapter.RecyclerViewAdapterBou;
import com.example.administrator.fulishe201612.model.bean.BoutiqueBean;
import com.example.administrator.fulishe201612.model.net.BoutiqueModel;
import com.example.administrator.fulishe201612.model.net.IBoutiqueModel;
import com.example.administrator.fulishe201612.model.net.INewGoodsModel;
import com.example.administrator.fulishe201612.model.net.NewGoodsModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Jingxuan extends Fragment {

    private static final int ACTION_DOWNLOAD = 1;
    private static final int ACTION_PULL_DOWN = 2;
    private static final int ACTION_PULL_UP = 3;

    RecyclerView recyclerView;
    RecyclerViewAdapterBou recyclerViewAdapter;
    ArrayList<BoutiqueBean> newGoodsBeenList;
    SwipeRefreshLayout swiperefresh;
    TextView textHint;
    IBoutiqueModel iBoutiqueModel;
    LinearLayoutManager linearLayoutManager;
    int pageId = 1;

    public Jingxuan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_jingxuan, container, false);
        initView(inflate);
        iBoutiqueModel = new BoutiqueModel();

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
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                int position = linearLayoutManager.findLastVisibleItemPosition();
//                if (recyclerViewAdapter.getItemCount() - 1 == position && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    pageId++;
//                    downLoadContactList(ACTION_PULL_UP, pageId);
//                }
//            }
//
//
//        });

    }

    private void downLoadContactList(final int action, int pageId) {
        final OkHttpUtils<BoutiqueBean[]> okHttpUtils = new OkHttpUtils<>(getActivity());
        iBoutiqueModel.loadData(getActivity(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {

//                if (!recyclerViewAdapter.isMore()) {
//                    if (action == ACTION_PULL_UP) {
//                        recyclerViewAdapter.setTextFooter("没有更多数据...");
//                    }
//                    return;
//                }

                ArrayList<BoutiqueBean> newGoodsBeen = okHttpUtils.array2List(result);
                switch (action) {
                    case ACTION_PULL_DOWN:


                        recyclerViewAdapter.initContact(newGoodsBeen);
                        swiperefresh.setRefreshing(false);
                        textHint.setVisibility(View.GONE);


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

    }

    private void initView(View inflate) {
        textHint = (TextView) inflate.findViewById(R.id.textHint);
        swiperefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.swiperefresh);
        newGoodsBeenList = new ArrayList<>();
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapterBou(getActivity(), newGoodsBeenList);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 20
                , false));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);


    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
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
