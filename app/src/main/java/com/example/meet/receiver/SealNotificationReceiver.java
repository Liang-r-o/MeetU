package com.example.meet.receiver;

import android.content.Context;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

public class SealNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage notificationMessage) {
//        返回false ，会弹出融云默认通知
//        返回true，融云SDK 不会弹通知，通知需要自定义
        return super.onNotificationMessageArrived(context, pushType, notificationMessage);
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage notificationMessage) {
//        返回false，会走融云SDK默认处理逻辑，即点击该同志会打开会话列表或会话界面
//        返回true，则由您自定义处理逻辑
        return super.onNotificationMessageClicked(context, pushType, notificationMessage);
    }
}
