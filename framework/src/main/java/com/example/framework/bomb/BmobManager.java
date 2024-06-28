package com.example.framework.bomb;

import android.content.Context;
import android.webkit.WebView;

import com.example.framework.utils.CommonUtils;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
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

//        queryPhoneUser("18979932702", new FindListener<MUser>() {
//            @Override
//            public void done(List<MUser> list, BmobException e) {
//                user = list.get(0);
//            }
//        });

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


    // todo 接下来两个方法的第一个参数是我写死的用户，
    /**
     * 添加好友
     */
    public void addFriend(MUser mMuser,MUser mUser, SaveListener<String> listener) {
        Friend friend = new Friend();
        friend.setUser(mMuser);
        friend.setFriendUser(mUser);
        friend.save(listener);
    }

    /**
     *  通过Id添加好友
     */
    public void addFriend(MUser mMuser,String id, SaveListener<String> listener) {
        queryObjectIdUser(id, new FindListener<MUser>() {
            @Override
            public void done(List<MUser> list, BmobException e) {
                if (e == null){
                    if (CommonUtils.isEmpty(list)){
                        MUser mUser = list.get(0);
                        addFriend(mMuser,mUser,listener);
                    }
                }
            }
        });
    }


    public interface OnUploadPhotoListener{
        void OnUpdateDone();
        void OnUpdateFail(BmobException e);
    }


    /**
     * 根据objectId查询用户
     * @param userId
     * @param listener
     */
    public void queryObjectIdUser(String objectId, FindListener<MUser> listener) {
        baseQuery("objectId",objectId,listener);

    }

    /**
     * 根据电话号码查询用户
     * @param phone
     */
    public void queryPhoneUser(String phone, FindListener listener){
        baseQuery("mobilePhoneNumber",phone,listener);
    }

    /**
     * 查询我的好友
     * @param listener
     */
    public void queryMyFriend(FindListener<Friend> listener){
        BmobQuery<Friend> query = new BmobQuery<>();
        query.addWhereEqualTo("user",getUser());
        query.findObjects(listener);
    }
    /**
     * 查询所有用户
     * @param
     */
    public void queryAllUser( FindListener<MUser> listener){
        BmobQuery<MUser> query = new BmobQuery<>();

        query.findObjects(listener);
    }

    /**
     * 查询基类
     * @param key
     * @param values
     * @param listener
     */
    public void baseQuery(String key,String values, FindListener<MUser> listener){
        BmobQuery<MUser> query = new BmobQuery<>();
        query.addWhereEqualTo(key,values);
        query.findObjects(listener);
    }
}
