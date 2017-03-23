package com.example.administrator.fulishe201612.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.AlbumsBean;
import com.example.administrator.fulishe201612.model.bean.GoodsDetailsBean;
import com.example.administrator.fulishe201612.model.net.GoodsDetialsModel;
import com.example.administrator.fulishe201612.model.net.IGoodsDetialsModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.ui.view.FlowIndicator;
import com.example.administrator.fulishe201612.ui.view.SlideAutoLoopView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoodsDetialsActivtiy extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.goodsname)
    TextView goodsname;
    @BindView(R.id.goodstitle)
    TextView goodstitle;
    @BindView(R.id.goodsprice)
    TextView goodsprice;

    IGoodsDetialsModel iGoodsDetial;
    @BindView(R.id.slideAutoLoopView)
    SlideAutoLoopView slideAutoLoopView;
    @BindView(R.id.flowindicator)
    FlowIndicator flowindicator;
    int goodsId;
    @BindView(R.id.webView)
    WebView webView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detials_activtiy);
        bind = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        goodsId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            finish();
            return;

        }
        iGoodsDetial = new GoodsDetialsModel();
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detials, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initData() {
        iGoodsDetial.loadData(this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                showInfo(result);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void showInfo(GoodsDetailsBean result) {
        goodsname.setText(result.getGoodsName());
        goodstitle.setText(result.getGoodsEnglishName());
        goodsprice.setText(result.getCurrencyPrice());
//        collapsingToolbarLayout.setTitle(result.getGoodsName());

        slideAutoLoopView.startPlayLoop(flowindicator, getAlbumImgUrl(result), getCount(result));
        webView.loadDataWithBaseURL(null, result.getGoodsBrief(), I.TEXT_HTML, "utf-8", null);
    }

    private int getCount(GoodsDetailsBean result) {

        if (result.getProperties() != null && result.getProperties().length > 0) {
            return result.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean result) {
        String[] s = new String[]{};
        AlbumsBean[] albums = result.getProperties()[0].getAlbums();
        int length = albums.length;
        s = new String[length];
        for (int i = 0; i < length; i++) {
            s[i] = albums[i].getImgUrl();
        }

        return s;
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
        bind.unbind();
    }
}
