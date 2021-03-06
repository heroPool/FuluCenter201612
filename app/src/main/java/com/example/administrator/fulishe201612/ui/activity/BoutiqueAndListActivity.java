package com.example.administrator.fulishe201612.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.adapter.PopuwindowListaAdapter;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.fragments.Xinpin;
import com.example.administrator.fulishe201612.model.bean.CategoryChildBean;
import com.example.administrator.fulishe201612.model.bean.NewGoodsBean;
import com.example.administrator.fulishe201612.model.net.FindGoodsDetails;
import com.example.administrator.fulishe201612.model.net.INewGoodsModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.model.utils.OkHttpUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BoutiqueAndListActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.imageback)
    ImageView imageback;
    @BindView(R.id.textTitle)
    TextView textTitle;
    String title;

    Unbinder bind;
    int listid;
    @BindView(R.id.layout_Sort)
    LinearLayout layoutSort;
    @BindView(R.id.priceSort)
    Button priceSort;
    @BindView(R.id.timeSort)
    Button timeSort;

    Xinpin xinpin;

    @BindView(R.id.textTitleFenlei)
    TextView textTitleFenlei;
    GridView GridView;
    PopupWindow popupWindow;

    PopuwindowListaAdapter popuwindowAdapter;

    ArrayList<CategoryChildBean> categoryChildBeen;
    @BindView(R.id.layout_Title)
    RelativeLayout layoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_and_list);
        bind = ButterKnife.bind(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        listid = intent.getIntExtra(I.NewAndBoutiqueGoods.CAT_ID, 0);
        String boolea = intent.getStringExtra("boolea");




        if (boolea != null) {
            //从分类跳过来
            Serializable childList = intent.getSerializableExtra("childList");
            categoryChildBeen = (ArrayList<CategoryChildBean>) childList;
            Log.i("main", "categoryChildBeen:" + categoryChildBeen.toString());
            String groupItem = intent.getStringExtra("groupItem");
            layoutSort.setVisibility(View.VISIBLE);

            textTitleFenlei.setText(groupItem);
            textTitleFenlei.setVisibility(View.VISIBLE);
            initPopuWindow();
        }
        xinpin = new Xinpin();
        textTitle.setText(this.title);

        priceSort.setOnClickListener(this);
        timeSort.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_cantai,
                xinpin).commit();


    }

    private void initPopuWindow() {

        popuwindowAdapter = new PopuwindowListaAdapter(this, categoryChildBeen);
        View layout = View.inflate(this, R.layout.popu_window, null);
//        LayoutInflater from = getLayoutInflater().from(this);
//        View inflate = from.inflate(R.layout.popu_window, null);

        popupWindow = new PopupWindow(layout, 900, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e0f1f1f1")));
        popupWindow.setOutsideTouchable(true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //悬浮窗口被关闭事件处理
                Drawable drawable = getResources().getDrawable(R.drawable.arrow2_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textTitleFenlei.setCompoundDrawables(null, null, drawable, null);
            }
        });
        GridView = (GridView) layout.findViewById(R.id.popWindow_list);

        GridView.setNumColumns(2);

        GridView.setAdapter(popuwindowAdapter);

        GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int cat_id = categoryChildBeen.get(position).getId();
                xinpin.setCat_id(cat_id);

                progressDialog = new ProgressDialog(BoutiqueAndListActivity.this);
                progressDialog.setMessage("loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                downLoadContactList(cat_id);
                popupWindow.dismiss();
                textTitle.setText(categoryChildBeen.get(position).getName());
            }
        });
    }

    ProgressDialog progressDialog;

    private void downLoadContactList(int cat_id) {
        INewGoodsModel iNewGoodsModel = new FindGoodsDetails();
        final OkHttpUtils<NewGoodsBean[]> okHttpUtils = new OkHttpUtils<>(this);

        iNewGoodsModel.loadData(this, cat_id, 1, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                ArrayList<NewGoodsBean> newGoodsBeen = okHttpUtils.array2List(result);

                ImageLoader.release();
                xinpin.getRecyclerViewAdapter().initContact(newGoodsBeen);
                progressDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(BoutiqueAndListActivity.this, "请求失败!:" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    int sortBy;
    boolean sortPrice = false;
    boolean sortAddTime = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageback:
                finish();
                break;
            case R.id.priceSort:

                sortBy = sortPrice ? I.SORT_BY_PRICE_ASC : I.SORT_BY_PRICE_DESC;
                sortPrice = !sortPrice;
                if (sortPrice) {
                    Drawable drawable = getResources().getDrawable(R.drawable.arrow_order_down);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    priceSort.setCompoundDrawables(null, null, drawable, null);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.arrow_order_up);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    priceSort.setCompoundDrawables(null, null, drawable, null);
                }
                setSortBy(sortBy);
                break;
            case R.id.timeSort:
                sortBy = sortAddTime ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                sortAddTime = !sortAddTime;
                if (sortAddTime) {
                    Drawable drawable = getResources().getDrawable(R.drawable.arrow_order_down);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    timeSort.setCompoundDrawables(null, null, drawable, null);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.arrow_order_up);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    timeSort.setCompoundDrawables(null, null, drawable, null);
                }
                setSortBy(sortBy);
                break;
            case R.id.textTitleFenlei:
                Log.i("popu", "执行");
                popupWindow.showAsDropDown(layoutTitle, layoutTitle.getWidth() - popupWindow.getWidth(), 0);
                if (popupWindow.isShowing()) {
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.arrow2_up);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    textTitleFenlei.setCompoundDrawables(null, null, drawable, null);
                }

        }

    }

    private void setSortBy(int sortBy) {

        xinpin.getRecyclerViewAdapter().setSortBy(sortBy);
        GridView gridView = new GridView(this);


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        bind.unbind();
    }


}
