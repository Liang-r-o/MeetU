package com.example.meet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.base.BaseActivity;
import com.example.framework.bomb.BmobManager;
import com.example.framework.entry.Constants;
import com.example.framework.utils.SpUtils;
import com.example.meet.MainActivity;
import com.example.meet.R;
import com.example.meet.base.BaseApp;

/**
 * 启动页
 */

public class IndexActivity extends BaseActivity {
    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *1.把启动页全屏
     *2.延迟进入主页
     *3.根据具体逻辑是进入引导页还是登录页
     *4.适配刘海屏
     */

    private static final int SKIP_MAIN = 1000;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case SKIP_MAIN:  // 接收到延迟两秒的消息
                    startMain();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_index);

//        发送延迟两秒的消息
        mHandler.sendEmptyMessageDelayed(SKIP_MAIN,2 * 1000);

    }

    /**
     * 进入主页
     */
    private void startMain() {
//1.判断App是否是第一次启动 install - first run
//        SpUtils.getInstance().initSp(BaseApp.getContext());
        boolean isFirstApp = SpUtils.getInstance().getBoolean(Constants.SP_IS_FIRST_APP,true);
        Intent intent = new Intent();

        if (isFirstApp){
            //跳转到引导页
            intent.setClass(this,GuideActivity.class);
//            非第一次启动
            SpUtils.getInstance().putBoolean(Constants.SP_IS_FIRST_APP,false);
        }else{
//            2.如果非第一次启动，判断是否曾经登录过
            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN,"");
            if (TextUtils.isEmpty(token)){
//                3.判断Bmob是否登录
                if (BmobManager.getInstance().isLogin()){
                    intent.setClass(this,MainActivity.class);
                }else {
                    intent.setClass(this,LoginActivity.class);
                }
//                跳转到登录页
                intent.setClass(this,LoginActivity.class);

            }else {
//                跳转到主页
                intent.setClass(this, MainActivity.class);
            }
        }

        startActivity(intent);
        finish();
    }
    /**
     * 优化
     * 冷启动经过的步骤
     * 1 第一次安装，加载应用程序并启动
     * 2 启动后显示一个空白的窗口 getWindow()
     * 3 启动/创建我们的应用进程
     *
     * APP内部
     * 1.创建APP对象
     * 2.启动主线程
     * 3.创建应用入口/LAUNACH
     * 4.填充ViewGroup
     * 5.绘制View measure -> layout -> draw
     *
     *
     * 优化手段：
     * 1.视图优化
     *  1)设置主题透明
     *  2)设置启动图片
     * 2.代码优化
     *  1)优化Application
     *  2)布局的优化，不需要繁琐的布局
     *  3)阻塞UI线程操作
     *  4)加载Bitmap/大图
     *  5)其他的一个占用主线程的操作
     *
     * 检测APP Activity的启动时间
     * 1.shell
     *  ActivityManager -> adb shell am start -S -W com.example.meet/com.example.meet.ui.IndexActivity
     *
     *  TotalTime: 527 启动一连串Activity的总耗时
     *  WaitTime: 537  应用创建的时间 + TotalTime
     *  应用创建的时间 WaitTime - TotalTime
     * 2.Log
     *   Android 4.4开始，ActivityManager增加了Log TAG = displayed
     */
}