package com.example.meet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.framework.base.BaseBackActivity;
import com.example.framework.manager.DialogManager;
import com.example.framework.view.DialogView;
import com.example.meet.R;

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
        initPhotoView();
        mIvPhoto = findViewById(R.id.iv_photo);

        mEtNickname = findViewById(R.id.et_nickname);
        mBtnUpload = findViewById(R.id.btn_upload);

        mIvPhoto.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);



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
        }else if (id == R.id.tv_ablum){
            DialogManager.getInstance().hide(mPhoeoDialogView);
//            跳转到相册
        }else if(id == R.id.tv_cancel){
            DialogManager.getInstance().hide(mPhoeoDialogView);
        }else if(id == R.id.iv_photo) {
//            显示选择提示框
            DialogManager.getInstance().show(mPhoeoDialogView);
        }

    }
}
