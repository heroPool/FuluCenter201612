package com.example.administrator.fulishe201612.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.fulishe201612.R;
import com.example.administrator.fulishe201612.application.FuLiCenterApplication;
import com.example.administrator.fulishe201612.application.I;
import com.example.administrator.fulishe201612.model.bean.Result;
import com.example.administrator.fulishe201612.model.bean.User;
import com.example.administrator.fulishe201612.model.dao.UserDao;
import com.example.administrator.fulishe201612.model.net.IUserModel;
import com.example.administrator.fulishe201612.model.net.OnCompleteListener;
import com.example.administrator.fulishe201612.model.net.UserModel;
import com.example.administrator.fulishe201612.model.utils.CommonUtils;
import com.example.administrator.fulishe201612.model.utils.ResultUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateNickActivity extends AppCompatActivity {

    @BindView(R.id.et_update_usernick)
    EditText etUpdateUsernick;
    @BindView(R.id.button)
    Button button;


    User user;
    IUserModel model;
    String newnick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        model = new UserModel();
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        } else {
            etUpdateUsernick.setText(user.getMuserNick());
            etUpdateUsernick.selectAll();
        }

    }


    ProgressDialog progressDialog;

    @OnClick(R.id.button)

    public void onClick() {

        if (checkInput()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.update_user_nick));
            progressDialog.show();

            model.updateNick(UpdateNickActivity.this, user.getMuserName(), newnick, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String result) {
                    Result resul = ResultUtils.getResultFromJson(result, User.class);
                    if (resul != null) {
                        if (resul.isRetMsg()) {
                            User u = (User) resul.getRetData();
                            updateSuccess(u);

                        } else {
                            if (resul.getRetCode() == I.MSG_USER_SAME_NICK) {
                                CommonUtils.showShortToast(R.string.update_nick_fail_unmodify);
                            }
                            if (resul.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                                CommonUtils.showShortToast(R.string.update_fail);
                            }
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast(R.string.update_fail);
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void updateSuccess(final User u) {
        FuLiCenterApplication.setUser(u);
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDao.getInstance(UpdateNickActivity.this).saveUser(u);

            }
        }).start();
        finish();

    }

    private boolean checkInput() {
        newnick = etUpdateUsernick.getText().toString().trim();
        if (TextUtils.isEmpty(newnick)) {
            etUpdateUsernick.requestFocus();
            etUpdateUsernick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if (newnick.equals(user.getMuserNick())) {
            etUpdateUsernick.requestFocus();
            etUpdateUsernick.setError(getString(R.string.update_nick_fail_unmodify));
            return false;
        }
        return true;
    }
}
