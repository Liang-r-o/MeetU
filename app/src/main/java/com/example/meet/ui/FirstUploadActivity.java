package com.example.meet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.framework.base.BaseBackActivity;
import com.example.meet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstUploadActivity extends BaseBackActivity implements View.OnClickListener {

    /**
     * 跳转
     * @param mActivity
     * @param requestCode
     */
    public static void startActivity(Activity mActivity, int requestCode){
        Intent intent = new Intent(mActivity,FirstUploadActivity.class);
        mActivity.startActivityForResult(intent,requestCode);
    }

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

    @Override
    public void onClick(View v) {


    }

    private void initView() {
        mIvPhoto = findViewById(R.id.iv_photo);
        mEtNickname = findViewById(R.id.et_nickname);
        mBtnUpload = findViewById(R.id.btn_upload);
    }
}
