package com.example.meet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.framework.bomb.BmobManager;
import com.example.framework.cloud.CloudManager;
import com.example.framework.db.NewFriend;
import com.example.framework.entry.Constants;
import com.example.framework.event.EventManager;
import com.example.framework.gson.TextBean;
import com.example.framework.helper.LitePalHelper;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.meet.MainActivity;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.listener.OnReceiveMessageWrapperListener;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.ReceivedProfile;
import io.rong.message.TextMessage;

public class CloudService extends Service {

    private Disposable disposable;
    public CloudService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("CloudService");
        linkCloudServer();
    }

    /**
     * 连接云服务
     */
    private void linkCloudServer() {
//        获取token
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        LogUtils.i("token:"+token);
//        连接服务
        CloudManager.getInstance().connect(token);
//        接收消息
        CloudManager.getInstance().setOnReceiveMessageListener(new OnReceiveMessageWrapperListener() {
            @Override
            public void onReceivedMessage(Message message, ReceivedProfile profile) {
                LogUtils.i("message:"+message);
                String objectName = message.getObjectName();
                if (objectName.equals(CloudManager.MSG_TEXT_NAME )){
                    TextMessage textMessage = (TextMessage)message.getContent();
                    String content = textMessage.getContent();
                    LogUtils.i("content:"+content);
//                    Gson JsonObject
                    TextBean textBean = new Gson().fromJson(content, TextBean.class);
//                    普通消息
                    if (textBean.getType().equals(CloudManager.TYPE_TEXT)){

                    }else if (textBean.getType().equals(CloudManager.TYPE_ADD_FRIEND)){
//                        存入数据库，Bomb RongCLoud都没有提供存储方法
//                        使用另外的方法，存入本地数据库
                        LogUtils.i("添加好友消息");
                        LitePalHelper.getInstance().saveNewFriend(textBean.getMsg(),message.getSenderUserId());
//                        查询数据库如果有重复的则不添加
//                        disposable =  Observable.create(new ObservableOnSubscribe<List<NewFriend>>() {
//                            @Override
//                            public void subscribe(ObservableEmitter<List<NewFriend>> emitter) throws Exception {
//                                emitter.onNext(LitePalHelper.getInstance().queryNewFriend());
//                                emitter.onComplete();
//                            }
//                        }).subscribeOn(Schedulers.newThread())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new Consumer<List<NewFriend>>() {
//                                            @Override
//                                            public void accept(List<NewFriend> newFriends) throws Exception {
//                                                if (CommonUtils.isEmpty(newFriends)) {
//                                                    boolean isHave = false;
//                                                    for (int j = 0; j < newFriends.size(); j++) {
//                                                        NewFriend newFriend = newFriends.get(j);
//                                                        if (message.getSenderUserId().equals(newFriend.getId())){
//                                                            isHave = true;
//                                                            break;
//                                                        }
//                                                    }
//                                                    LogUtils.i("isHave:"+isHave);
////                                                    防止重复添加
//                                                    if (!isHave){
//                                                        LogUtils.i("saveNewFriend");
//                                                        LitePalHelper.getInstance().saveNewFriend
//                                                                (textBean.getMsg(),message.getSenderUserId());
//                                                    }
//                                                }
//                                            }
//                                        });


                    }else if (textBean.getType().equals(CloudManager.TYPE_ARGEED_FRIEND)){
//                        1添加到好友列表
                        BmobManager.getInstance().addFriend(MainActivity.mUser, message.getSenderUserId(), new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
//                                    2.刷新好友列表
                                    EventManager.post(EventManager.FLAG_UPDATE_FRIEND_LIST);
                                }
                            }
                        });
                    }
                }
            }
        });

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (disposable.isDisposed()){
//            disposable.dispose();
//        }
//
//    }
}