package com.example.administrator.fulishe201612.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;

import com.example.administrator.fulishe201612.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {


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
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
