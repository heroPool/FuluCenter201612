package com.example.administrator.fulishe201612.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.dao.SharePreferenceUtils;
import com.example.administrator.fulishe201612.model.dao.UserDao;

public class SplashActivity extends AppCompatActivity {
    SplashActivity splashActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashActivity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                User user = FuLiCenterApplication.getUser();
                String userName = SharePreferenceUtils.getinstance().getUserName();
                if (user == null && userName != null) {
                    UserDao userDao = new UserDao(SplashActivity.this);
                    user = userDao.getUser(userName);
                    if (user != null) {
                        FuLiCenterApplication.setUser(user);

                    }
                }

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 2000);
    }
}
