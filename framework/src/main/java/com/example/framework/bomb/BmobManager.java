package com.example.framework.bomb;

import android.content.Context;
import android.webkit.WebView;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class BmobManager {

    private static final String BMOB_SDK_ID = "9c70c8f47a2b7ed1384cb3ce20b02b66";

//    private static final String BMOB_SDK_ID = "f8efae5debf319071b44339cf51153fc";
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
    public MUser getUser(){
        return BmobUser.getCurrentUser(MUser.class);
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
    public void signOrLoginByMobilePhone(String phone, String code, LogInListener<MUser> listener){
        BmobUser.signOrLoginByMobilePhone(phone,code,listener);
    }


//    上传头像
    public void uploadFirstPhoto(String nickName, File file,OnUploadPhotoListener listener){
        /**
         * 1 上传文件拿到地址
         * 2 更新用户信息
         *
         */
        final MUser user = getUser();
        final BmobFile bmobFile = new BmobFile(file);

        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
//                    上传成功
                    user.setNickName(nickName);
                    user.setPhoto(bmobFile.getFileUrl());

//                    user.setTokenNickName(nickName);
//                    user.setTokenPhoto(bmobFile.getFileUrl());

//                    更新用户信息
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                listener.OnUpdateDone();
                            }else {
                                listener.OnUpdateFail(e);
                            }
                        }
                    });
                 }else {
                    listener.OnUpdateFail(e);

                }
            }
        });

    }

    public interface OnUploadPhotoListener{
        void OnUpdateDone();
        void OnUpdateFail(BmobException e);
    }


    /**
     * 根据电话号码查询用户
     * @param phone
     */
    public void queryPhoneUser(String phone, FindListener listener){
        baseQuery("mobilePhoneNumber",phone,listener);


    }

    /**
     * 查询基类
     * @param key
     * @param values
     * @param listener
     */
    public void baseQuery(String key,String values, FindListener listener){
        BmobQuery<MUser> query = new BmobQuery<>();
        query.addWhereEqualTo(key,values);
        query.findObjects(listener);
    }
}
