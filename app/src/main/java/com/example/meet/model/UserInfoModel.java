package com.example.meet.model;

/**
 * 用户信息模型
 */
public class UserInfoModel {
//    标题
    private String title;
    private String content;

    private int bgColor;

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
