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
import com.example.administrator.fulishe201612.model.bean.MessageBean;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.net.UserModel;
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.ui.activity.SettingsActivity;
import com.example.administrator.fulishe201612.ui.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.example.administrator.fulishe201612.R.id.text_shoucangbaobei_num;


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
    @BindView(text_shoucangbaobei_num)
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


    User user;
    UserModel userModel;

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
        userModel = new UserModel();
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarPersion);
        initView(view);
        setListener();
        return view;
    }


    private void setListener() {

        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    boolean login_result = data.getBooleanExtra("login_result", false);
                    if (login_result) {
                        showInfo();

                    }
                    break;
            }
        }
    }

    private void initView(View view) {


        showInfo();
    }

    private void showInfo() {

        user = FuLiCenterApplication.getUser();
        if (user != null) {
            toolbarPersion.setTitle(user.getMuserNick());
            ImageLoader.setAvatar(user.getAvatar(), getContext(), imageAvatar);
            userModel.isCollectGoods(getContext(), user.getMuserName(), new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        textShoucangbaobeiNum.setText(result.getMsg());
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }


}
