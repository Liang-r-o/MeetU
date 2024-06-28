package com.example.framework.bomb;

import cn.bmob.v3.BmobObject;

public class Friend extends BmobObject {
//    我自己
    private MUser user;

//    好友
    private MUser friendUser;

    public MUser getUser() {
        return user;
    }

    public void setUser(MUser user) {
        this.user = user;
    }

    public MUser getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(MUser friendUser) {
        this.friendUser = friendUser;
    }
}
