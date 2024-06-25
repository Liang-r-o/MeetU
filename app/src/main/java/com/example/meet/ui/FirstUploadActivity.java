package com.example.meet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;

import com.example.framework.base.BaseBackActivity;
import com.example.framework.bomb.BmobManager;
import com.example.framework.helper.FileHelper;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.view.DialogView;
import com.example.framework.view.LoadingView;
import com.example.meet.R;

import java.io.File;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

public class FirstUploadActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView tvCamera;
    private TextView tvAblum;
    private TextView tvCancel;

    private DialogView mPhoeoDialogView;

    /**
     * 跳转
     *
     * @param mActivity
     * @param requestCode
     */
    public static void startActivity(Activity mActivity, int requestCode) {
        Intent intent = new Intent(mActivity, FirstUploadActivity.class);
        mActivity.startActivityForResult(intent, requestCode);
    }


    //    圆形头像
    private CircleImageView mIvPhoto;
    private EditText mEtNickname;
    private Button mBtnUpload;

    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_NoActionBar);

        setContentView(R.layout.activity_first_upload);

        initView();

    }


    /**
     * 初始化View
     */

    private void initView() {

        mLoadingView = new LoadingView(this);
        mLoadingView.setLoadingText("正在上传头像...");
        initPhotoView();
        mIvPhoto = findViewById(R.id.iv_photo);

        mEtNickname = findViewById(R.id.et_nickname);
        mBtnUpload = findViewById(R.id.btn_upload);

        mIvPhoto.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);

        mBtnUpload.setEnabled(false);
        mEtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    mBtnUpload.setEnabled(uploadFile != null);
                }else {
                    mBtnUpload.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    private void initPhotoView() {
        mPhoeoDialogView = DialogManager.getInstance().
                initView(this, R.layout.dialog_select_photo, Gravity.BOTTOM);

        tvCamera = (TextView) mPhoeoDialogView.findViewById(R.id.tv_camera);
        tvCamera.setOnClickListener(this);
        tvAblum = (TextView) mPhoeoDialogView.findViewById(R.id.tv_ablum);
        tvAblum.setOnClickListener(this);
        tvCancel = (TextView) mPhoeoDialogView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_camera){
            DialogManager.getInstance().hide(mPhoeoDialogView);
            //            跳转到相机
            FileHelper.getInstance().toCamera(this);

        }else if (id == R.id.tv_ablum){
            DialogManager.getInstance().hide(mPhoeoDialogView);
//            跳转到相册
            FileHelper.getInstance().toAlbum(this);
        }else if(id == R.id.tv_cancel){
            DialogManager.getInstance().hide(mPhoeoDialogView);
        }else if(id == R.id.iv_photo) {
//            显示选择提示框
            DialogManager.getInstance().show(mPhoeoDialogView);
        }
        else if (id == R.id.btn_upload){
            uploadPhoto();
        }

    }

//    s上传头像
    private void uploadPhoto() {
        mLoadingView.show();
//        如果条件没满足，走不到这一步
        String nickName = mEtNickname.getText().toString().trim();
        BmobManager.getInstance().uploadFirstPhoto(nickName,uploadFile, new BmobManager.OnUploadPhotoListener() {
            @Override
            public void OnUpdateDone() {
                mLoadingView.hide();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void OnUpdateFail(BmobException e) {
                mLoadingView.hide();
                LogUtils.e("e"+ e.toString());
                Toast.makeText(FirstUploadActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    private File uploadFile = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        LogUtils.i("requestCode:" +resultCode);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == FileHelper.CAMERA_REQUEST_CODE){
                uploadFile = FileHelper.getInstance().getTempFile();

            }else if (requestCode == FileHelper.ALBUM_REQUEST_CODE){
                Uri uri = data.getData();
                if (uri != null){
//                    String path = uri.getPath();  //path:/external_primary/images/media/1000083320
//                    LogUtils.e("path:"+path);
//                      获取真实的地址 path:/storage/emulated/0/Pictures/WeiXin/mmexport1718947192917.jpg
                    String path = FileHelper.getInstance().getRealPathFromURI(this, uri);
                    if (!TextUtils.isEmpty(path)){
                        uploadFile = new File(path);
                    }
                 }
            }
            //        设置头像
            if (uploadFile != null){
                Bitmap mBitmap = BitmapFactory.decodeFile(uploadFile.getPath());
                mIvPhoto.setImageBitmap(mBitmap);

//                判断当前输入框
                String nickName = mEtNickname.getText().toString().trim();
                mBtnUpload.setEnabled(!TextUtils.isEmpty(nickName));
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }



}
