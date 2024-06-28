package com.example.framework;

import android.content.Context;

import com.example.framework.bomb.BmobManager;
import com.example.framework.cloud.CloudManager;
import com.example.framework.utils.SpUtils;
import org.litepal.LitePal;
/**
 * Framework 入口
 */
public class Framework {
    private volatile static Framework mFramework;

    private Framework(){

    }

    public static Framework getFramework(){
        if (mFramework == null){
            synchronized (Framework.class){
                if (mFramework == null){
                    mFramework = new Framework();
                }
            }
        }
        return mFramework;
    }

    public void initFramework(Context mContext){
        SpUtils.getInstance().initSp(mContext);
        BmobManager.getInstance().initBmob(mContext);
        CloudManager.getInstance().initCloud(mContext);
        LitePal.initialize(mContext);

    }
}
