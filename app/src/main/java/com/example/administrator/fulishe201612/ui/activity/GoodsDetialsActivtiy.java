package com.example.administrator.fulishe201612.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.AlbumsBean;
import com.example.administrator.fulishe201612.model.bean.GoodsDetailsBean;
import com.example.administrator.fulishe201612.model.bean.MessageBean;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.net.CartModel;
import com.example.administrator.fulishe201612.model.net.GoodsDetialsModel;
import com.example.administrator.fulishe201612.model.net.IGoodsDetialsModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.ShowToastUtils;
import com.example.administrator.fulishe201612.ui.view.FlowIndicator;
import com.example.administrator.fulishe201612.ui.view.SlideAutoLoopView;

import java.lang.reflect.Method;

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
    GoodsDetialsModel goodsDetialsModel;
    CartModel cartModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detials_activtiy);
        bind = ButterKnife.bind(this);
        goodsDetialsModel = new GoodsDetialsModel();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        cartModel = new CartModel();
        goodsId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            finish();
            return;
        }
        iGoodsDetial = new GoodsDetialsModel();
        initData();
    }


    boolean isCollects = false;
    int notLoginAction = 0;
    private static final int ACTION_ADDCOLLECT = 1;

    private static final int ACTION_ADDCART = 2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (FuLiCenterApplication.getInstance().getUtil().check())
            return super.onOptionsItemSelected(item);
        User user = FuLiCenterApplication.getUser();

        if (user == null) {
            if (item.getItemId() == R.id.menu_isCollect) {
                notLoginAction = ACTION_ADDCOLLECT;
            }
            if (item.getItemId() == R.id.menu_addCart) {
                notLoginAction = ACTION_ADDCART;
            }
            FuLiCenterApplication.getInstance().setIndex(0);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_isCollect://menu中收藏按钮
                if (isCollects) {
                    ActionGoodsToCollect(user, I.ACTION_DELETE_COLLECT);
                } else {
                    ActionGoodsToCollect(user, I.ACTION_ADD_COLLECT);
                }
//                this.item = item;       //添加item实例
                break;
            case R.id.menu_addCart:
                ActionGoodsToCart(user);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ActionGoodsToCollect(User user, final int action) {

        goodsDetialsModel.isCollect(this, action, user.getMuserName(), goodsId, new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    if (action == I.ACTION_ADD_COLLECT) {
                        isCollects = true;
                        ShowToastUtils.showToast(GoodsDetialsActivtiy.this, "收藏成功");
                        showImageConllect(true);
                    } else {
                        ShowToastUtils.showToast(GoodsDetialsActivtiy.this, "操作成功");
                    }
                    if (action == I.ACTION_DELETE_COLLECT) {
                        isCollects = false;
                        ShowToastUtils.showToast(GoodsDetialsActivtiy.this, "取消成功");
                        showImageConllect(false);
                    }
                } else {

                }
            }

            @Override
            public void onError(String error) {
                if (action == I.ACTION_ADD_COLLECT) {
                    isCollects = false;
                    showImageConllect(false);
                }
            }
        });
    }

    private void ActionGoodsToCart(User user) {
        cartModel.cartAction(this, I.ACTION_CART_ADD, null, String.valueOf(user.getMuserName()), goodsId + "", 1, new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    ShowToastUtils.showToast(GoodsDetialsActivtiy.this, getString(R.string.add_goods_success));
                } else {
                    ShowToastUtils.showToast(GoodsDetialsActivtiy.this, getString(R.string.add_goods_fail));
                }
            }

            @Override
            public void onError(String error) {
                ShowToastUtils.showToast(GoodsDetialsActivtiy.this, getString(R.string.add_goods_fail));
            }
        });
    }

    MenuItem item;
    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detials, menu);
        this.menu = menu;
        item = menu.getItem(0);

        return super.onCreateOptionsMenu(menu);
    }

    User user;

    @Override
    protected void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            goodsIsCollect();

        }
        if (FuLiCenterApplication.getUser() != null) {
            if (notLoginAction == ACTION_ADDCOLLECT) {
                ActionGoodsToCollect(user, I.ACTION_ADD_COLLECT);
            }
            if (notLoginAction == ACTION_ADDCART) {
                ActionGoodsToCart(user);
            }
        }


    }

    private void goodsIsCollect() {
        goodsDetialsModel.isCollect(this, I.ACTION_IS_COLLECT, user.getMuserName(), goodsId, new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    isCollects = result.isSuccess();
                    showImageConllect(true);
                } else {
                    isCollects = false;
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    private void showImageConllect(boolean iscollect) {
        if (iscollect) {
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.bg_collect_out);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            item.setIcon(drawable);
        } else {
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.bg_collect_in);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            item.setIcon(drawable);
        }
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
