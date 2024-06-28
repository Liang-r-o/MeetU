package com.example.meet.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.base.BaseActivity;
import com.example.framework.bomb.BmobManager;
import com.example.framework.bomb.Friend;
import com.example.framework.bomb.MUser;
import com.example.framework.cloud.CloudManager;
import com.example.framework.entry.Constants;
import com.example.framework.helper.GlideHelper;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.CommonUtils;
import com.example.framework.view.DialogView;
import com.example.meet.MainActivity;
import com.example.meet.R;
import com.example.meet.adapter.CommonAdapter;
import com.example.meet.adapter.CommonViewHolder;
import com.example.meet.model.UserInfoModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    DialogView mAddFriendDialogView;
    private EditText mEtMsg;
    private TextView mTvCancel;
    private TextView mTvAddFriend;
    /**
     * 1.根据传递过来的Id，查询用户信息，并且显示
     *  -普通的消息
     *  -构建一个RecyclerView宫格
     * 2.建立好友关系模型
     *  - 根据什么条件判断这个人是不是你的好友
     *     与我有关系的是好友（1在我的好友列表中，2同意了我的好友申请—— BmobObject建表 3查询所有的Friend表，其中User对应的列是我的好友
     * 3.实现添加好友的提示框
     * 4.发送添加好友的消息
     *  -自定义协议消息（复杂）
     *  -自定义协议
     *  发送文本消息content，我们对文本进行处理：增加JSON 定义一个标记来显示，点击提示框的发送按钮去发送
     *
     * 5.接收添加好友的消息
     */

    /**
     * 跳转
     *
     * @param mContext
     * @param userId
     */
    public static void startActivity(Context mContext, String userId) {
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID, userId);
        mContext.startActivity(intent);
    }

    private RelativeLayout mLlBack;
    private CircleImageView mIvUserPhoto;
    private TextView mTvNickname;
    private TextView mTvDesc;
    private RecyclerView mMUserInfoView;
    private CommonAdapter<UserInfoModel> mUserInfoAdapter;
    private List<UserInfoModel> mUserInfoList = new ArrayList<>();

    private Button mBtnAddFriend;
    private LinearLayout mLlIsFriend;
    private Button mBtnChat;
    private Button mBtnAudioChat;
    private Button mBtnVideoChat;

    //个人信息颜色
    private int[] mColor = {0x881E90FF, 0x8800FF7F, 0x88FFD700, 0x88FF6347, 0x88F08080, 0x8840E0D0};

    //用户ID
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        initView();
    }


    private void initView() {

        initAddFriendDialog();

//        获取用户ID
        userId = getIntent().getStringExtra(Constants.INTENT_USER_ID);

        mLlBack = (RelativeLayout) findViewById(R.id.ll_back);
        mIvUserPhoto = (CircleImageView) findViewById(R.id.iv_user_photo);
        mTvNickname = (TextView) findViewById(R.id.tv_nickname);
        mTvDesc = (TextView) findViewById(R.id.tv_desc);
        mMUserInfoView = (RecyclerView) findViewById(R.id.mUserInfoView);
        mBtnAddFriend = (Button) findViewById(R.id.btn_add_friend);
        mLlIsFriend = (LinearLayout) findViewById(R.id.ll_is_friend);
        mBtnChat = (Button) findViewById(R.id.btn_chat);
        mBtnAudioChat = (Button) findViewById(R.id.btn_audio_chat);
        mBtnVideoChat = (Button) findViewById(R.id.btn_video_chat);


        mLlBack.setOnClickListener(this);
        mBtnAddFriend.setOnClickListener(this);
        mBtnChat.setOnClickListener(this);
        mBtnAudioChat.setOnClickListener(this);
        mBtnVideoChat.setOnClickListener(this);

//        列表
        mUserInfoAdapter = new CommonAdapter<>(mUserInfoList, new CommonAdapter.OnBindDataListener<UserInfoModel>() {
            @Override
            public void onBindViewHolder(UserInfoModel model, CommonViewHolder viewHolder, int type, int position) {
                viewHolder.getView(R.id.ll_bg).setBackgroundColor(model.getBgColor());
                viewHolder.setText(R.id.tv_type, model.getTitle());
                viewHolder.setText(R.id.tv_content, model.getContent());
//                viewHolder.setBackgroundColor(R.id.ll_bg,model.getBgColor());


            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_user_info_item;
            }
        });

        mMUserInfoView.setLayoutManager(new GridLayoutManager(this, 3));
        mMUserInfoView.setAdapter(mUserInfoAdapter);


        queryUserInfo();


    }

    /**
     * 添加好友的提示框
     */
    private void initAddFriendDialog() {
        mAddFriendDialogView = DialogManager.getInstance().initView(this, R.layout.dialog_send_friend);
        mEtMsg = (EditText) mAddFriendDialogView.findViewById(R.id.et_msg);
        mTvCancel = (TextView) mAddFriendDialogView.findViewById(R.id.tv_cancel);
        mTvAddFriend = (TextView) mAddFriendDialogView.findViewById(R.id.tv_add_friend);
//        mEtMsg.setText(getString(R.string.text_me_info_tips) + MainActivity.mUser.getTokenNickName());

        mTvCancel.setOnClickListener(this);
        mTvAddFriend.setOnClickListener(this);

    }

    private void queryUserInfo() {
        if (TextUtils.isEmpty(userId)) {
            return;
        }

//        查询用户信息
        BmobManager.getInstance().queryObjectIdUser(userId, new FindListener<MUser>() {
            @Override
            public void done(List<MUser> list, BmobException e) {
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
                        MUser mUser = list.get(0);
                        updateUserInfo(mUser);
                    }
                }
            }
        });

//        判断好友关系
        BmobManager.getInstance().queryMyFriend(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
//                        有一个好友列表
//                        userId是recycleView中点击的子项的Id，list是我的所有好友列表，好友列表中存在和子项一样的实例，则证明这个UserId是我的朋友
                        for (int i = 0; i < list.size(); i++) {
                            Friend friend = list.get(i);
//
                            if (friend.getFriendUser().getObjectId().equals(userId)) {
//                                是好友关系
                                mBtnAddFriend.setVisibility(View.GONE);
                                mLlIsFriend.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });

    }


    /**
     * 更新用户信息
     *
     * @param mUser
     */
    @SuppressLint("NotifyDataSetChanged")
    private void updateUserInfo(MUser mUser) {
        //                        设置基本属性
        GlideHelper.loadUrl(UserInfoActivity.this, mUser.getPhoto(),
                mIvUserPhoto);
        mTvNickname.setText(mUser.getNickName());
        mTvDesc.setText(mUser.getDesc());

        //性别 年龄 生日 星座 爱好 单身状态
        addUserInfoModel(mColor[0], getString(R.string.text_me_info_sex), mUser.isSex() ? getString(R.string.text_me_info_boy) : getString(R.string.text_me_info_girl));
        addUserInfoModel(mColor[1], getString(R.string.text_me_info_age), mUser.getAge() + getString(R.string.text_search_age));
        addUserInfoModel(mColor[2], getString(R.string.text_me_info_birthday), mUser.getBirthday());
        addUserInfoModel(mColor[3], getString(R.string.text_me_info_constellation), mUser.getConstellation());
        addUserInfoModel(mColor[4], getString(R.string.text_me_info_hobby), mUser.getHobby());
        addUserInfoModel(mColor[5], getString(R.string.text_me_info_status), mUser.getStatus());
        //刷新数据
        mUserInfoAdapter.notifyDataSetChanged();
    }

    private void addUserInfoModel(int color, String title, String content) {
        UserInfoModel model = new UserInfoModel();
        model.setBgColor(color);
        model.setTitle(title);
        model.setContent(content);
        mUserInfoList.add(model);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.ll_back == id) {
            finish();
        } else if (id == R.id.btn_add_friend) {
            DialogManager.getInstance().show(mAddFriendDialogView);
        } else if (id == R.id.tv_add_friend) {
            String msg =mEtMsg.getText().toString().trim();
            if (TextUtils.isEmpty(msg)){
                msg = "你好，我是"+BmobManager.getInstance().getUser().getNickName();

            }
            DialogManager.getInstance().hide(mAddFriendDialogView);
            CloudManager.getInstance().sendTextMessage(msg,CloudManager.TYPE_ADD_FRIEND,userId);

            Toast.makeText(this,"消息发送成功",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.tv_cancel) {
            DialogManager.getInstance().hide(mAddFriendDialogView);
        }
    }
}