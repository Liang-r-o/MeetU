package com.example.framework.utils;

/**
 * 时间转换类
 */
public class TimeUtil {
    /**
     * 转换毫秒格式 HH:mm:ss
     * @param ms
     */
    public static String formatDuring(long ms){
        long hours = (ms % (1000 * 60 * 60 * 24))  / (1000 * 60 * 60);
        long minutes = (ms % (1000 * 60 * 60))  / (1000 * 60);
        long seconds = (ms % (1000 * 60))  / 1000;

        String h = hours +"";
        if (hours < 10){
            h = "0" + h;
        }

        String m = minutes +"";
        if (minutes < 10){
            m = "0" + m;
        }

        String s = minutes +"";
        if (seconds < 10){
            s = "0" + s;
        }


        return h + ":" + m + ":" + s;
    }
}
