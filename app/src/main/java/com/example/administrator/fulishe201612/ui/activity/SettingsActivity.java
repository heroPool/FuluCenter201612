package com.example.administrator.fulishe201612.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.administrator.fulishe201612.model.utils.ImageLoader;
import com.example.administrator.fulishe201612.model.utils.L;
import com.example.administrator.fulishe201612.model.utils.ResultUtils;
import com.example.administrator.fulishe201612.ui.utils.ClipImageActivity;
import com.example.administrator.fulishe201612.ui.utils.view.CircleImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.type;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    @BindView(R.id.imgae_insert_username)
    ImageView imgaeInsertUsername;
    @BindView(R.id.text_setting_username)
    TextView textSettingUsername;
    @BindView(R.id.layout_Username)
    RelativeLayout layoutUsername;
    @BindView(R.id.image_insert_nick)
    ImageView imageInsertNick;
    @BindView(R.id.text_setting_nick)
    TextView textSettingNick;
    @BindView(R.id.layout_Nick)
    RelativeLayout layoutNick;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.image_insert)
    ImageView imageInsert;
    @BindView(R.id.imageView2)
    de.hdodenhof.circleimageview.CircleImageView imageView2;
    @BindView(R.id.layout_myAvatar)
    RelativeLayout layoutMyAvatar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String avatarName;
    User user;
    Bundle mBundle;

    ProgressDialog pd;
    IUserModel model;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        initView();
        initData();
        bundle = savedInstanceState;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

    }

    private void initView() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("设置");
        }

    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            showInfo(user);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfo(User user) {
        textSettingUsername.setText(user.getMuserName());
        textSettingNick.setText(user.getMuserNick());
        ImageLoader.downloadImg(this, imageView2, user.getAvatar());

    }

    @OnClick({R.id.layout_Username, R.id.layout_Nick, R.id.btn_logout, R.id.layout_myAvatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_Username:
                Toast.makeText(this, "用户名不可以修改哦~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_Nick:
                startActivity(new Intent(this, UpdateNickActivity.class));
                break;
            case R.id.btn_logout:
                UserDao.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.layout_myAvatar:
                createCameraTempFile(bundle);
                uploadHeadImage();
                break;
        }
    }


    private String getAvatarName() {
        avatarName = user.getMuserName() + System.currentTimeMillis();
        L.e(TAG, "avatarname=" + avatarName);
        return avatarName;
    }

    private void showDialog() {
        pd = new ProgressDialog(SettingsActivity.this);
        pd.setMessage(getString(R.string.update_user_avatar));
        pd.show();
    }

    private void updateSuccess(final User u) {
        L.e(TAG, "updateSuccess,user=" + u);
        CommonUtils.showShortToast(R.string.update_user_avatar_success);
        FuLiCenterApplication.setUser(u);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(SettingsActivity.this).saveUser(u);
                L.e(TAG, "updateSuccess,b=" + b);
            }
        }).start();
//        setResult(RESULT_OK);
        initData();
    }

    private void uploadAvatar(File file) {
        showDialog();
        model = new UserModel();
        model.uploadAvatar(SettingsActivity.this, user.getMuserName(), file,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = ResultUtils.getResultFromJson(s, User.class);
                        if (result != null) {
                            if (result.isRetMsg()) {
                                User u = (User) result.getRetData();
                                updateSuccess(u);
                            } else {
                                if (result.getRetCode() == I.MSG_UPLOAD_AVATAR_FAIL) {
                                    CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                                }
                            }
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                    }
                });

    }

    //headImage begin
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像1
    private CircleImageView headImage1;
    //头像2
    private ImageView headImage2;
    //调用照相机返回图片临时文件
    private File tempFile;

    /**
     * 外部存储权限申请返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCarema();
            } else {
                // Permission Denied
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            } else {
                // Permission Denied
            }
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCarema();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
//            String path = getAvatarPath(SettingsActivity.this,I.AVATAR_TYPE)+"/"+getAvatarName()+".jpg";
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
//            tempFile = new File(path);
        }
        L.e(TAG, "file=" + tempFile.getAbsolutePath());
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    L.e(TAG, "cropImagePath=" + cropImagePath);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    L.e(TAG, "bitMap=" + bitMap);
                    imageView2.setImageBitmap(bitMap);
                    L.e(TAG, "set avatar bitmap over...");
                    //此处后面可以将bitMap转为二进制上传后台网络
                    File file = saveBitmapFile(bitMap);
                    L.e(TAG, "file=" + file.getAbsolutePath());
                    uploadAvatar(file);
                }
                break;
        }
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 返回头像保存在sd卡的位置:
     * Android/data/cn.ucai.superwechat/files/pictures/user_avatar
     *
     * @param context
     * @param path
     * @return
     */
    public static String getAvatarPath(Context context, String path) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(dir, path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }

    private File saveBitmapFile(Bitmap bitmap) {
        if (bitmap != null) {
            String imagePath = getAvatarPath(SettingsActivity.this, I.AVATAR_TYPE) + "/" + getAvatarName() + ".jpg";
            File file = new File(imagePath);//将要保存图片的路径
            L.e("file path=" + file.getAbsolutePath());
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }
    //headImage end

}
