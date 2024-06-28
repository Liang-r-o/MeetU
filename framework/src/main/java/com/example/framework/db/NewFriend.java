package com.example.framework.db;

import org.litepal.crud.LitePalSupport;


/**
 * 新朋友类
 */
public class NewFriend extends LitePalSupport {
    private String msg;


//    对方Id
    private String userId;

    private long saveTime;

//    状态 -1：待确认 0：同意 1：拒绝
    private int isAgree = 1;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public int getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(int isAgree) {
        this.isAgree = isAgree;
    }
}
