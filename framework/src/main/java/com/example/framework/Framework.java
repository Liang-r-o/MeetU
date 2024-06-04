package com.example.framework;

/**
 * Framework 入口
 */
public class Framework {
    private volatile static Framework mFramework;

    private Framework(){

    }

    public static Framework getmFramework(){
        if (mFramework == null){
            synchronized (Framework.class){
                if (mFramework == null){
                    mFramework = new Framework();
                }
            }
        }
        return mFramework;
    }
}
