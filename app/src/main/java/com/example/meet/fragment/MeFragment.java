package com.example.meet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.framework.bomb.BmobManager;
import com.example.framework.bomb.MUser;
import com.example.framework.helper.GlideHelper;
import com.example.meet.MainActivity;
import com.example.meet.R;
import com.example.meet.ui.NewFriendActivity;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends Fragment implements View.OnClickListener{


    private CircleImageView mIvMePhoto;
    private TextView mTvNickname;
    private TextView mTvServerStatus;
    private LinearLayout mLlMeInfo;
    private LinearLayout mLlNewFriend;
    private LinearLayout mLlPrivateSet;
    private LinearLayout mLlShare;
    private LinearLayout mLlNotice;
    private LinearLayout mLlSetting;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        initView();
        return view;
    }

    private void initView() {
        mIvMePhoto = (CircleImageView) view.findViewById(R.id.iv_me_photo);
        mTvNickname = (TextView) view.findViewById(R.id.tv_nickname);
        mTvServerStatus = (TextView) view.findViewById(R.id.tv_server_status);
        mLlMeInfo = (LinearLayout) view.findViewById(R.id.ll_me_info);
        mLlNewFriend = (LinearLayout) view.findViewById(R.id.ll_new_friend);
        mLlPrivateSet = (LinearLayout) view.findViewById(R.id.ll_private_set);
        mLlShare = (LinearLayout) view.findViewById(R.id.ll_share);
        mLlNotice = (LinearLayout) view.findViewById(R.id.ll_notice);
        mLlSetting = view.findViewById(R.id.ll_setting);

        mLlMeInfo.setOnClickListener(this);
        mLlNewFriend.setOnClickListener(this);
        mLlPrivateSet.setOnClickListener(this);
        mLlShare.setOnClickListener(this);
        mLlSetting.setOnClickListener(this);

        loadMeInfo();
    }

    /**
     * 加载个人信息
     */
    private void loadMeInfo() {
//        final MUser[] mUser = new MUser[1];
//        BmobManager.getInstance().queryPhoneUser("17939603000", new FindListener<MUser>() {
//            @Override
////            异步 打日志验证 耗时操作一般异步
//            public void done(List<MUser> list, BmobException e) {
//                mUser[0] = list.get(0);
//                GlideHelper.loadUrl(getActivity(), mUser[0].getPhoto(),mIvMePhoto);
//                mTvNickname.setText(mUser[0].getNickName());
//            }
//        });

        MUser mUser = MainActivity.mUser;
        GlideHelper.loadUrl(getActivity(),mUser.getTokenPhoto(),mIvMePhoto);
        mTvNickname.setText(mUser.getTokenNickName());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
       if (id == R.id.ll_new_friend){
           //新朋友
           startActivity(new Intent(getActivity(), NewFriendActivity.class));
       }

    }
}