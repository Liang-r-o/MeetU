package com.example.meet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.example.framework.base.BaseActivity;
import com.example.framework.bomb.BmobManager;
import com.example.framework.bomb.MUser;
import com.example.framework.entry.Constants;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.framework.view.DialogView;
import com.example.framework.view.LoadingView;
import com.example.framework.view.TouchPictureV;
import com.example.meet.MainActivity;
import com.example.meet.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 1.点击发送的按钮，弹出一个提示框，图片验证码，验证通过之后
     * 2.发送验证码，@同时按钮变成不可点击，@按钮开始倒计时。倒计时结束后，按钮可点击，文字变成“发送”
     * 3.通过手机号码和验证码进行登录
     * 4.登录成功之后获取本地对象
     */
    private EditText mEtPhone;

    private EditText mEtCode;
    private Button mBtnSendCode;
    private Button mBtnLogin;
    private TextView mTvTestLogin;
    private TextView mTvUserAgreement;

    private static final int H_TIME = 1001;

    private static int TIME = 60;

    private DialogView mCodeView;
    private TouchPictureV mPictureV;

    private LoadingView mLoadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setTheme(androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_NoActionBar);
        setContentView(R.layout.activity_login);
        initView();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case H_TIME:
                    TIME--;
                    mBtnSendCode.setText(TIME + "s");
                    if (TIME > 0){
                        mHandler.sendEmptyMessageDelayed(H_TIME,1000);
                    }else {
                        mBtnSendCode.setEnabled(true);
                        mBtnSendCode.setText(getString(R.string.text_login_send));
                    }
                    break;
            }
        return false;
        }
    });

    /**
     * 点击事件
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send_code){
            DialogManager.getInstance().show(mCodeView);
        }else if (id == R.id.btn_login){
            login();
        }
    }

    /**
     * 发送短信验证码
     */
    private void sendMSM() {
        //1获取手机号
        String phone = mEtPhone.getText().toString().trim();
//        LogUtils.i(phone);
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, getString(R.string.text_login_phone_null), Toast.LENGTH_SHORT).show();
            return;
        }
//        2、请求短信验证码
        BmobManager.getInstance().requestSMS(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null){
                    mBtnSendCode.setEnabled(false);
                    // todo
                    mHandler.sendEmptyMessage(H_TIME);
                    Toast.makeText(LoginActivity.this, "短信验证码发送成功",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "短信验证码发送失败",
                            Toast.LENGTH_SHORT).show();
//                    ViewGroup
                }
            }
        });
    }




    private void login() {
        //1判断手机号和验证码不为空
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, getString(R.string.text_login_phone_null), Toast.LENGTH_SHORT).show();
            return;
        }

        String code = mEtCode.getText().toString().trim();
        // todo
        if (TextUtils.isEmpty(code)){
            Toast.makeText(this, getString(R.string.text_login_code_null), Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * 显示Loading
         */

        mLoadingView.show("正在登陆");

        BmobManager.getInstance().signOrLoginByMobilePhone(phone, code, new LogInListener<MUser>() {
            @Override
            public void done(MUser mUser, BmobException e) {
                if (e == null){
                    mLoadingView.hide();
//                    登录成功
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    把手机号码保存下来，用户下次登录可以不用重新输入
                    SpUtils.getInstance().putString(Constants.SP_PHONE,phone);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "ERROR:" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

//                    登录成功
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    把手机号码保存下来，用户下次登录可以不用重新输入
        SpUtils.getInstance().putString(Constants.SP_PHONE,phone);
        finish();
    }

    private void initView() {

        initDialogView();

        mEtCode = findViewById(R.id.et_code);
        mEtPhone = findViewById(R.id.et_phone);
        mBtnSendCode = findViewById(R.id.btn_send_code);
        mBtnLogin = findViewById(R.id.btn_login);
        //mTvTestLogin = findViewById(R.id.tv_test_login);
        mTvUserAgreement = findViewById(R.id.tv_user_agreement);

        mBtnSendCode.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        String phone = SpUtils.getInstance().getString(Constants.SP_PHONE,"");
        if (!TextUtils.isEmpty(phone)){
            mEtPhone.setText(phone);
        }
    }

    private void initDialogView() {
        mLoadingView = new LoadingView(this);
         mCodeView = DialogManager.getInstance().initView(this, R.layout.dialog_code_view);
         mPictureV  = mCodeView.findViewById(R.id.mPictureV);
         mPictureV.setViewResultListener(new TouchPictureV.OnViewResultListener() {
             @Override
             public void onResult() {
                 DialogManager.getInstance().hide(mCodeView);
                 sendMSM();
             }
         });

    }
}