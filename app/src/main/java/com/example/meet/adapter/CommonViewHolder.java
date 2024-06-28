package com.example.meet.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.helper.GlideHelper;

public class CommonViewHolder extends RecyclerView.ViewHolder {

//    子View的集合
    private SparseArray<View> mViews;
    private View mContentView;

    public CommonViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mContentView = itemView;
    }

    /**
     * 获取CommonViewHolder实体
     * @param parent
     * @param layoutId
     * @return
     */
    public static CommonViewHolder getViewHolder(ViewGroup parent,int layoutId){
        return new CommonViewHolder(View.inflate((parent.getContext()),layoutId,null));
    }

//    给外部提供获取子View的方式

    /**
     * 提供给外部访问子View的方法
     * @param viewId
     * @return
     * @param <T>
     */
    public <T extends View>T getView(int viewId){
        View view = mViews.get(viewId);
        if (view == null){
            view = mContentView.findViewById(viewId);
            mViews.put(viewId,view);

        }
        return (T)view;
    }


    /**
     * 设置文本
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId,String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }


    /**
     * 设置图片链接
     * @param mContext
     * @param viewId
     * @param url
     * @return
     */
    public CommonViewHolder setImageUrl(Context mContext,int viewId, String url){
        ImageView iv = getView(viewId);
        GlideHelper.loadUrl(mContext,url,iv);
        return this;
    }

    /**
     * 设置图片
     * @param viewId
     * @param resId
     * @return
     */
    public CommonViewHolder setImageResource( int viewId,int resId){
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /***
     * 设置背景颜色
     * @param viewId
     * @param color
     * @return
     */
    public CommonViewHolder setBackgroundColor( int viewId,int color){
        ImageView iv = getView(viewId);
        iv.setBackgroundColor(color);
        return this;
    }
}
