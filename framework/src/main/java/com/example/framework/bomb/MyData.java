package com.example.framework.bomb;

import cn.bmob.v3.BmobObject;

/**
 * 测试云函数
 */
public class MyData extends BmobObject {
//    姓名
    private String name;
//    性别 0 ：男，1：女
    private int sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
