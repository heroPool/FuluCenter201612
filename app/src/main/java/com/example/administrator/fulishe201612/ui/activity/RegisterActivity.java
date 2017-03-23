package com.example.administrator.fulishe201612.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.Result;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.net.IUserModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.net.UserModel;
import com.example.administrator.fulishe201612.model.utils.CommonUtils;
import com.example.administrator.fulishe201612.model.utils.MD5;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.imageAvatar)
    ImageView imageAvatar;
    @BindView(R.id.edit_Nick)
    EditText editNick;
    @BindView(R.id.editUsername)
    EditText editUsername;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.editPassword_check)
    EditText editPasswordCheck;
    @BindView(R.id.btn_register)
    Button btnRegister;

    IUserModel iUserModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        iUserModel = new UserModel();
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        String userNick = editNick.getText().toString().trim();
        if (TextUtils.isEmpty(userNick)) {
            editNick.requestFocus();
            editNick.setError("昵称为空");
            return;
        }
        String userName = editUsername.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            editUsername.requestFocus();
            editUsername.setError("用户名为空");
            return;

        } else if (!userName.matches("[a-zA-Z]\\w{5,15}")) {
            CommonUtils.showShortToast(R.string.illegal_user_name);
            editUsername.requestFocus();
            return;
        }
        String passWord = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(passWord)) {
            editPassword.requestFocus();
            editPassword.setError("密码为空");
            return;

        }
        String PasswordCheck = editPasswordCheck.getText().toString().trim();
        if (TextUtils.isEmpty(PasswordCheck)) {
            editPasswordCheck.requestFocus();
            editPasswordCheck.setError("确认密码为空");
            return;
        }
        if (!passWord.equals(PasswordCheck)) {
            Toast.makeText(this, "密码输入不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.registering));
        progressDialog.show();
        register(userNick, userName, passWord);


    }

    ProgressDialog progressDialog;

    private void register(String userNick, final String userName, String passWord) {
        iUserModel.register(this, userName, userNick, MD5.getMessageDigest(passWord), new OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                Log.i("main", result.toString());
                if (result == null) {
                    CommonUtils.showShortToast(R.string.register_fail);
                } else {
                    if (result.isRetMsg()) {
                        //返回数据到登录界面
                        setResult(RESULT_OK, new Intent().putExtra(I.User.USER_NAME, userName));
//                        Snackbar.make()

                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        CommonUtils.showLongToast(R.string.register_fail_exists);
                        editUsername.requestFocus();
                        progressDialog.dismiss();
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
