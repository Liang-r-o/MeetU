package com.example.meet.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.example.framework.Framework;

public class BaseApp extends Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Framework.getFramework().initFramework(this);


        /**
         * Application的优化
         * 1.必要的组件在程序主页去初始化
         * 2.如果组件一定要在App中初始化，那么尽可能的延时
         * 3.非必要的组件，子线程中初始化
         */

        //只在主进程中初始化
        if (getApplicationInfo().packageName.equals(
                getCurProcessName(getApplicationContext()))) {
            Framework.getFramework().initFramework(this);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess :
                activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static Context getContext(){
        return context;
    }
}
