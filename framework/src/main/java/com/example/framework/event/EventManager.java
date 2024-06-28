package com.example.framework.event;

import org.greenrobot.eventbus.EventBus;

public class EventManager {

// 更新好友列表
    public static final int FLAG_UPDATE_FRIEND_LIST = 1000;

    /**
     * 注册
     * @param subscriber
     */
    public static void register(Object subscriber){
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 解注册
     * @param subscriber
     */
    public static void unregister(Object subscriber){
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(int type){
        EventBus.getDefault().post(new MessageEvent(type));
    }
}
