package com.example.administrator.fulishe201612.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.ui.activity.CircleImageView;
import com.example.administrator.fulishe201612.ui.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Wo extends Fragment {


    @BindView(R.id.toolbar_persion)
    Toolbar toolbarPersion;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.image_avatar)
    CircleImageView imageAvatar;
    @BindView(R.id.layoutImage)
    RelativeLayout layoutImage;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.text_shoucangbaobei_num)
    TextView textShoucangbaobeiNum;
    @BindView(R.id.layout_center_collect)
    LinearLayout layoutCenterCollect;
    @BindView(R.id.text_shoucangdianpu)
    TextView textShoucangdianpu;
    @BindView(R.id.text_zuji)
    TextView textZuji;
    @BindView(R.id.image_insert)
    ImageView imageInsert;
    @BindView(R.id.layout_myorder)
    RelativeLayout layoutMyorder;
    @BindView(R.id.image_daifukuan)
    ImageView imageDaifukuan;
    @BindView(R.id.image_daifahuo)
    ImageView imageDaifahuo;
    @BindView(R.id.image_daishouhuo)
    ImageView imageDaishouhuo;
    @BindView(R.id.image_daipingjia)
    ImageView imageDaipingjia;
    @BindView(R.id.image_fukuanshouhou)
    ImageView imageFukuanshouhou;

    public Toolbar getToolbarPersion() {
        return toolbarPersion;
    }

    public Wo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wo, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarPersion);
        initView(view);

        return view;
    }

    User user;

    private void initView(View view) {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));

        } else {
            showInfo();
        }
    }

    private void showInfo() {
        toolbarPersion.setTitle(user.getMuserNick());
        ImageLoader.downloadImg(getContext(), imageAvatar, user.getAvatar());
    }


}
