package com.example.framework.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framework.R;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.AnimUtils;

/**
 * 加载提示框
 */
public class LoadingView {


    private DialogView mLodingView;
    private ImageView mIvLoding;
    private TextView mTvLodingText;

    private ObjectAnimator mAnim;


    public LoadingView(Context mContext){
        mLodingView = DialogManager.getInstance().initView(mContext, R.layout.dialog_loding);
        mIvLoding = mLodingView.findViewById(R.id.iv_loding);
        mTvLodingText = mLodingView.findViewById(R.id.tv_loding_text);
        mAnim = AnimUtils.rotation(mIvLoding);
    }

    /**
     * 设置加载提示文本
     * @param text
     */
    public void setLoadingText(String text){
        if (!TextUtils.isEmpty(text)){
            mTvLodingText.setText(text);
        }
    }

    public void show(){
        mAnim.start();
        DialogManager.getInstance().show(mLodingView);
    }

    public void show(String text){
        mAnim.start();
        setLoadingText(text);
        DialogManager.getInstance().show(mLodingView);
    }

    public void hide(){
        mAnim.pause();
        DialogManager.getInstance().hide(mLodingView);
    }
}
