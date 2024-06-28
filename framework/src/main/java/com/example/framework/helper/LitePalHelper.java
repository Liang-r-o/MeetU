package com.example.framework.helper;

import com.example.framework.bomb.Friend;
import com.example.framework.db.NewFriend;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class LitePalHelper {

    private static volatile LitePalHelper mInstance = null;
    private LitePalHelper(){

    }

    public static LitePalHelper getInstance(){
        if (mInstance == null){
            synchronized (LitePalHelper.class){
                if (mInstance == null){
                    mInstance = new LitePalHelper();
                }
            }
        }
        return mInstance;
    }



    /**
     * 保存的基类
     * @param support
     */
    public void baseSave(LitePalSupport support){
        support.save();
    }

    public void saveNewFriend(String msg,String id){
        NewFriend newFriend = new NewFriend();
        newFriend.setMsg(msg);
        newFriend.setId(id);
        newFriend.setIsAgree(-1);
        newFriend.setSaveTime(System.currentTimeMillis());
        baseSave(newFriend);
    }

    /**
     * 查询的基类
     * @param cls
     * @return
     */
    public List<? extends LitePalSupport> baseQuery(Class cls){
        return LitePal.findAll(cls);
    }

    /**
     * 查询新朋友
     * @return
     */
    public List<NewFriend> queryNewFriend(){
        return (List<NewFriend>) baseQuery(NewFriend.class);
    }

    /**
     * 更新新朋友的数据库状态
     * @param userId
     * @param agree
     */
    public void updateNewFriend(String userId,int agree){
        NewFriend newFriend = new NewFriend();
        newFriend.setIsAgree(agree);
        newFriend.updateAll("userId = ?",userId);
    }


}
