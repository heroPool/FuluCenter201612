package com.example.administrator.fulishe201612.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.Result;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.dao.SharePreferenceUtils;
import com.example.administrator.fulishe201612.model.dao.UserDao;
import com.example.administrator.fulishe201612.model.net.IUserModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.net.UserModel;
import com.example.administrator.fulishe201612.model.utils.CommonUtils;
import com.example.administrator.fulishe201612.model.utils.MD5;
import com.example.administrator.fulishe201612.model.utils.ResultUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.editUserName)
    EditText editUserName;
    @BindView(R.id.editText6)
    EditText editPassword;
    @BindView(R.id.button2)
    Button btnLogin;

    @BindView(R.id.imageView)
    ImageView imageView;

    LoginActivity mContext;


    IUserModel iUserModel;
    @BindView(R.id.text_toRegister)
    TextView textToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        iUserModel = new UserModel();
    }

    public void login(View view) {
        checkInput();
    }

    private void checkInput() {
        String username = editUserName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            editUserName.requestFocus();
            editUserName.setError("用户名为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.requestFocus();
            editPassword.setError("密码为空");
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.logining));
        progressDialog.show();
        login(username, password);


    }

    ProgressDialog progressDialog;

    private void login(String username, String password) {
        iUserModel.login(this, username, MD5.getMessageDigest(password), new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String resu) {
                Result result = ResultUtils.getResultFromJson(resu, User.class);
                if (result == null) {
                    CommonUtils.showLongToast(R.string.login_fail);

                } else {
                    if (result.isRetMsg()) {
                        final User user = (User) result.getRetData();

                        FuLiCenterApplication.setUser(user);//将登录的用户写到application里面

                        SharePreferenceUtils.getinstance().setUserName(user.getMuserName());//首选项保存数据

                        new Thread(new Runnable() {
                            @Override
                            public void run() {//将数据保存到本地数据库
                                UserDao.getInstance(LoginActivity.this).saveUser(user);
                            }
                        }).start();
                        progressDialog.dismiss();
                        finish();
                    } else {
                        if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                            CommonUtils.showLongToast(R.string.login_fail_unknow_user);

                        } else if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                            CommonUtils.showLongToast(R.string.login_fail_error_password);
                        } else {
                            CommonUtils.showLongToast(R.string.login_fail);
                        }
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
            }
        });
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_REGISTER) {
            String name = data.getStringExtra(I.User.USER_NAME);
            editUserName.setText(name);
        }
    }

    @OnClick(R.id.text_toRegister)
    public void onClick() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, I.REQUEST_CODE_REGISTER);
    }
}
