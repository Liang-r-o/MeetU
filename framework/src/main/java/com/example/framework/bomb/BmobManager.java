package com.example.framework.bomb;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class BmobManager {

    private static final String BMOB_SDK_ID = "9c70c8f47a2b7ed1384cb3ce20b02b66";
    private volatile static BmobManager mInstance = null;

    private BmobManager(){

    }


    public static BmobManager getInstance(){
        if (mInstance == null){
            synchronized (BmobManager.class){
                if (mInstance == null){
                    mInstance = new BmobManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化Bmob
     * @param context
     */
    public void initBmob(Context context){
        Bmob.initialize(context,BMOB_SDK_ID);
    }


    /**
     * 判断是否登录
     * @return
     */
    public boolean isLogin(){
        return BmobUser.isLogin();
    }

    /**
     * 获取本地对象
     * @return
     */
    public IMUser getUser(){
        return BmobUser.getCurrentUser(IMUser.class);
    }

    /**
     * 发送短信验证码
     * @param phone
     * @param listener
     */
    public void requestSMS(String phone, QueryListener<Integer> listener){
        BmobSMS.requestSMSCode(phone,"",listener);
    }


    /**
     * 通过手机号码注册或者登录
     * @param phone
     * @param code 短信验证码
     * @param listener
     */
    public void signOrLoginByMobilePhone(String phone, String code, LogInListener<IMUser> listener){
        BmobUser.signOrLoginByMobilePhone(phone,code,listener);
    }
}
