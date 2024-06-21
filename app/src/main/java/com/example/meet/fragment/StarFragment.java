package com.example.meet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.framework.base.BaseFragment;
import com.example.meet.R;
import com.example.meet.ui.AddFriendActivity;
import com.moxun.tagcloudlib.view.TagCloudView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarFragment extends BaseFragment implements View.OnClickListener {


    private TextView mTvStarTitle;
    private ImageView mIvCamera;
    private ImageView mIvAdd;
    private TextView mTvConnectStatus;
    private TagCloudView mMCloudView;
    private LinearLayout mLlRandom;
    private TextView mTvRandom;
    private LinearLayout mLlSoul;
    private TextView mTvSoul;
    private LinearLayout mLlFate;
    private TextView mTvFate;
    private LinearLayout mLlLove;
    private TextView mTvLove;

    public StarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_star, null);
        initView(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_camera){
//            扫描
        }else if(id == R.id.iv_add){
//            添加好友
            startActivity(new Intent(getActivity(), AddFriendActivity.class));
        }else if(id == R.id.ll_random){
//          随机匹配
        }else if(id == R.id.ll_soul){
//          灵魂匹配
        }else if(id == R.id.ll_fate){
//          缘分匹配
        }else if(id == R.id.iv_add){

        }

    }

    private void initView(View view) {

        mTvStarTitle = view.findViewById(R.id.tv_star_title);
        mIvCamera = view.findViewById(R.id.iv_camera);
        mIvAdd = view.findViewById(R.id.iv_add);
        mTvConnectStatus = view.findViewById(R.id.tv_connect_status);
        mMCloudView = view.findViewById(R.id.mCloudView);
        mLlRandom = view.findViewById(R.id.ll_random);
        mTvRandom = view.findViewById(R.id.tv_random);
        mLlSoul = view.findViewById(R.id.ll_soul);
        mTvSoul = view.findViewById(R.id.tv_soul);
        mLlFate = view.findViewById(R.id.ll_fate);
        mTvFate = view.findViewById(R.id.tv_fate);
        mLlLove = view.findViewById(R.id.ll_love);
        mTvLove = view.findViewById(R.id.tv_love);
    }
}