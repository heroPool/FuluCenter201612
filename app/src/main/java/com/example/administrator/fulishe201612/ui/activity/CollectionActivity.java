package com.example.administrator.fulishe201612.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.adapter.CollectrecyclerViewAdapter;
import com.example.administrator.fulishe201612.adapter.RecyclerViewAdapter;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.model.bean.CollectBean;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.net.CollectModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;
import com.example.administrator.fulishe201612.model.utils.ShowToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


//收藏页
public class CollectionActivity extends AppCompatActivity {

    @BindView(R.id.toobar_collect)
    Toolbar toobarCollect;
    @BindView(R.id.recyclerView_collect)
    RecyclerView recyclerViewCollect;
    @BindView(R.id.textHint)
    TextView textHint;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;

    private static final int ACTION_DOWNLOAD = 1;
    private static final int ACTION_PULL_DOWN = 2;
    private static final int ACTION_PULL_UP = 3;

    ArrayList<CollectBean> arrayListCollect;


    GridSpacingItemDecoration gridSpacingItemDecoration;
    CollectrecyclerViewAdapter collectrecyclerViewAdapter;
    CollectModel collectModel;
    GridLayoutManager gridLayoutManager;
    int pageId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        setSupportActionBar(toobarCollect);

        collectModel = new CollectModel();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        downData();
        initView();
        setListener();

        downLoadContactList(ACTION_PULL_DOWN, pageId);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListener() {



        //下拉刷新

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(true);

                textHint.setVisibility(View.VISIBLE);
                pageId = 1;
                downLoadContactList(ACTION_PULL_DOWN, pageId);

            }
        });
        //上拉加载

        recyclerViewCollect.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int position = gridLayoutManager.findLastVisibleItemPosition();
                if (collectrecyclerViewAdapter.isMore() && collectrecyclerViewAdapter.getItemCount() - 1 == position && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageId++;
                    downLoadContactList(ACTION_PULL_UP, pageId);
                }
            }


        });

    }

    Handler handler;
    ProgressDialog progressDialog;

    private void downData() {
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("玩命加载中...");
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        progressDialog.show();
                        break;
                    case 2:
                        progressDialog.dismiss();
                        break;
                }
            }
        };

    }

    private static final String TAG = "CollectionActivity";

    private void downLoadContactList(final int action, int pageId) {


        handler.sendEmptyMessage(1);
        User user = FuLiCenterApplication.getUser();

        collectModel.loadCollectGoods(this, user.getMuserName(), pageId, 10, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                handler.sendEmptyMessage(2);
                Log.i(TAG, "result:" + result.toString());

                collectrecyclerViewAdapter.setMore(result != null && result.length > 0);
                if (!collectrecyclerViewAdapter.isMore()) {
                    if (action == ACTION_PULL_UP) {
                        collectrecyclerViewAdapter.setTextFooter("没有更多数据...");
                    }
                    return;
                }

                ArrayList<CollectBean> collectBeen = OkHttpUtils.array2List(result);
                switch (action) {
                    case ACTION_PULL_DOWN:
                        ImageLoader.release();
                        collectrecyclerViewAdapter.initContact(collectBeen);
                        swiperefresh.setRefreshing(false);
                        textHint.setVisibility(View.GONE);
                        collectrecyclerViewAdapter.setTextFooter("加载更多");
                        break;
                    case ACTION_PULL_UP:
                        collectrecyclerViewAdapter.addAllContact(collectBeen);
                        break;
                }
            }

            @Override
            public void onError(String error) {
                ShowToastUtils.showToast(CollectionActivity.this, "请求失败" + error.toString());

            }
        });


    }

    private void initView() {

        arrayListCollect = new ArrayList<>();
        collectrecyclerViewAdapter = new CollectrecyclerViewAdapter(this, arrayListCollect);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewCollect.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));
        recyclerViewCollect.setLayoutManager(gridLayoutManager);
        recyclerViewCollect.setHasFixedSize(true);
        recyclerViewCollect.setAdapter(collectrecyclerViewAdapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = collectrecyclerViewAdapter.getItemViewType(position);
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
