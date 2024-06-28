package com.example.framework.cloud;

import android.content.Context;

import com.example.framework.manager.HttpManager;
import com.example.framework.utils.LogUtils;

import org.json.JSONObject;

import java.util.List;

import io.rong.imlib.IRongCoreCallback;
import io.rong.imlib.IRongCoreEnum;
import io.rong.imlib.RongCoreClient;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.listener.OnReceiveMessageWrapperListener;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.InitOption;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.ReceivedProfile;
import io.rong.message.TextMessage;

public class CloudManager {
    public static final String TOKEN_URL = "http://api.rong-api.com/user/getToken.json";
    //    key
    public static final String CLOUD_KEY = "vnroth0kv2duo";

    public static final  String CLOUD_SECRET ="eKPrx8aATU24fj";

//    ObjectName
    public static final String MSG_TEXT_NAME = "RC:TxtMsg";
    public static final String MSG_IMAGE_NAME = "RC:ImgMsg";
    public static final String MSG_LOCATION_NAME = "RC:LBSMsg";

    //Msg Type

    //普通消息
    public static final String TYPE_TEXT = "TYPE_TEXT";
    //添加好友消息
    public static final String TYPE_ADD_FRIEND = "TYPE_ADD_FRIEND";
    //同意添加好友的消息
    public static final String TYPE_ARGEED_FRIEND = "TYPE_ARGEED_FRIEND";



    public static volatile CloudManager mInstance = null;

    public CloudManager() {
    }

    public static CloudManager getInstance(){
        if (mInstance == null){
            synchronized (HttpManager.class){
                if (mInstance == null){
                    mInstance = new CloudManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化SDK
     * @param mContext
     */
    public void initCloud(Context mContext){
       // String appKey = "Your_AppKey"; // example: bos9p5rlcm2ba
        InitOption initOption = new InitOption.Builder().build();
        RongCoreClient.init(mContext, CLOUD_KEY, initOption);
     }

    /**
     * 连接融云服务
     * @param token
     */
    public void connect(String token){
        RongCoreClient.connect(token, new IRongCoreCallback.ConnectCallback() {
            /**
             * 数据库回调.
             * @param code 数据库打开状态. DATABASE_OPEN_SUCCESS 数据库打开成功; DATABASE_OPEN_ERROR 数据库打开失败
             */
            @Override
            public void onDatabaseOpened(IRongCoreEnum.DatabaseOpenStatus code) {
                LogUtils.i("code:"+code);
            }
            /**
             * 成功回调
             * @param userId 当前用户 ID
             */
            @Override
            public void onSuccess(String userId) {
                LogUtils.i("连接成功:"+userId);
                //启动模拟消息发送消息给手机，消息内容是，很高兴认识你
//                CloudManager.getInstance().sendTextMessage("很高兴认识你","c18e38a8cf");
            }
            /**
             * 错误回调
             * @param errorCode 错误码
             */
            @Override
            public void onError(IRongCoreEnum.ConnectionErrorCode errorCode) {
                LogUtils.i("errorCode:"+errorCode);
            }


        });
    }

    /**
     * 断开连接
     */
    public void disconnect(){
//        RongIMClient.getInstance().disconnect();
        RongCoreClient.getInstance().disconnect();
    }

    /**
     * 退出登录
     */
    public void logout(){
//        RongIMClient.getInstance().logout();
        RongCoreClient.getInstance().logout();
    }

    /**
     * 接收消息的监听器
     * @param listener
     */
    public void setOnReceiveMessageListener(OnReceiveMessageWrapperListener listener){
//        RongIMClient.setOnReceiveMessageListener(listener);
        RongCoreClient.addOnReceiveMessageListener(listener);

    }

    /**
     * 发送消息的结果回调
     * 为什么写在发送文本消息的外部，因为这个不局限于只是文本消息的结果回调
     */
    private IRongCoreCallback.ISendMessageCallback iSendMessageCallback =
            new IRongCoreCallback.ISendMessageCallback() {
                @Override
                public void onAttached(Message message) {

                }

                @Override
                public void onSuccess(Message message) {
                    LogUtils.i("sendmessage onsusscess");
                }

                @Override
                public void onError(Message message, IRongCoreEnum.CoreErrorCode coreErrorCode) {
                    LogUtils.i("sendmessage onerror:"+coreErrorCode);
                }
            };

    /**
     * 发送文本消息
     * @param msg
     * @param targetId
     * 一个手机发送，另外一个手机接收
     */
    public void sendTextMessage(String msg,String targetId){
        LogUtils.i("sendTextMsessage");
        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;

        TextMessage textMessage = TextMessage.obtain(msg);

        Message message = Message.obtain(targetId, conversationType, textMessage);
         RongCoreClient.getInstance().sendMessage(message,
                 null,
                 null,
                 iSendMessageCallback);
    }

    public void sendTextMessage(String msg,String type,String targetId){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("msg",msg);
//            如果没有type 就是普通消息
            jsonObject.put("type",type);
            sendTextMessage(jsonObject.toString(),targetId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 查询本地的会话记录
     * @param callback
     */
    public void getConversationList(IRongCoreCallback.ResultCallback<List<Conversation>> callback){
        RongCoreClient.getInstance().getConversationList(callback);
    }
}
