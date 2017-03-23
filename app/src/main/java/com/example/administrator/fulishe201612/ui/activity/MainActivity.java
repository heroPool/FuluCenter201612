package com.example.administrator.fulishe201612.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.fragments.Fenlei;
import com.example.administrator.fulishe201612.fragments.Gouwuche;
import com.example.administrator.fulishe201612.fragments.Jingxuan;
import com.example.administrator.fulishe201612.fragments.Wo;
import com.example.administrator.fulishe201612.fragments.Xinpin;
import com.example.administrator.fulishe201612.model.utils.ShowToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPagerAdapter viewPagerAdapter;
    Fragment[] mFragments;

    @BindView(R.id.btnXinpin)
    RadioButton btnXinpin;
    @BindView(R.id.btnJingxuan)
    RadioButton btnJingxuan;
    @BindView(R.id.btnFenlei)
    RadioButton btnFenlei;
    @BindView(R.id.btnGouwuche)
    RadioButton btnGouwuche;
    @BindView(R.id.btnGeren)
    RadioButton btnGeren;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initData();
        initView();


    }

    Wo wo;

    private void initView() {
        mFragments = new Fragment[5];
        mFragments[0] = new Xinpin();
        mFragments[1] = new Jingxuan();
        mFragments[2] = new Fenlei();
        mFragments[3] = new Gouwuche();
        wo = new Wo();
        mFragments[4] = wo;

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(viewPagerAdapter);
        setListener();
    }


    private void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: " + index);
        if (index == 2) {
            getMenuInflater().inflate(R.menu.menu_fenlei, menu);
        }
        if (index == 4) {
            getMenuInflater().inflate(R.menu.menu_persionlcenter, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.message:

                break;
            case R.id.erweima:
                if (FuLiCenterApplication.getInstance().getUtil().check()) break;
                Log.i(TAG, "点击次数：" + 1);
                ShowToastUtils.showToast(this, "点击二维码");
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setListener() {
        btnXinpin.setOnClickListener(this);
        btnJingxuan.setOnClickListener(this);
        btnFenlei.setOnClickListener(this);
        btnGouwuche.setOnClickListener(this);
        btnGeren.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        btnXinpin.setChecked(true);
                        viewPager.setCurrentItem(0);

                        break;
                    case 1:
                        btnJingxuan.setChecked(true);
                        viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        btnFenlei.setChecked(true);
                        viewPager.setCurrentItem(2);
                        break;
                    case 3:
                        if (FuLiCenterApplication.getUser() == null) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, I.REQUEST_CODE_LOGIN_FROM_CART);

                        }
                        btnGouwuche.setChecked(true);
                        viewPager.setCurrentItem(3);
                        break;
                    case 4:
                        if (FuLiCenterApplication.getUser() == null) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, I.REQUEST_CODE_LOGIN);
                        }
                        btnGeren.setChecked(true);
                        viewPager.setCurrentItem(4);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    int index = 0;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnXinpin:
                index = 0;
                viewPager.setCurrentItem(0);
                break;
            case R.id.btnJingxuan:
                index = 1;
                viewPager.setCurrentItem(1);
                break;
            case R.id.btnFenlei:
                index = 2;
                viewPager.setCurrentItem(2);
                break;
            case R.id.btnGouwuche:
                index = 3;
                viewPager.setCurrentItem(3);
                break;
            case R.id.btnGeren:
                index = 4;
                viewPager.setCurrentItem(4);
                break;
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        Fragment[] fragments;

        public ViewPagerAdapter(FragmentManager fm, Fragment[] fragments) {

            super(fm);
            this.fragments = fragments;
        }

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

    }


    private static final String TAG = "MainActivity:";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == I.REQUEST_CODE_LOGIN) {
                btnGeren.setChecked(true);
                viewPager.setCurrentItem(4);
            }
            if (requestCode == I.REQUEST_CODE_LOGIN_FROM_CART) {
                btnGouwuche.setChecked(true);
                viewPager.setCurrentItem(3);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        btnXinpin.setChecked(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }

    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if(index == 4 && FuLiCenterApplication.getUser()==null){
//            index = 0;
//        }
//        setFragment();
//    }
}
