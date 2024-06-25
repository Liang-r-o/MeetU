package com.example.meet.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    private List<T> mList;

    private OnBindDataListener<T> onBindDataListener;
    private OnMoreBingDataListener<T> onMoreBingDataListener;

    /**
     * 适配普通的布局
     * @param mList
     * @param onBindDataListener
     */
    public CommonAdapter(List<T> mList, OnBindDataListener<T> onBindDataListener) {
        this.mList = mList;
        this.onBindDataListener = onBindDataListener;
    }


    /**
     * 兼容多类型的布局
     * @param mList
     * @param onMoreBingDataListener
     */
    public CommonAdapter(List<T> mList, OnMoreBingDataListener<T> onMoreBingDataListener) {
        this.mList = mList;
        this.onBindDataListener = onMoreBingDataListener;
        this.onMoreBingDataListener = onMoreBingDataListener;
    }



    @Override
    public int getItemViewType(int position) {
        if (onMoreBingDataListener != null){
            return onMoreBingDataListener.getItemType(position);

        }
        return  0;
    }

    /**
     * 绑定数据
     * @param <T>
     */
    public interface OnBindDataListener<T>{
//       参数是需要外部处理的，往外抛出去
        void onBindViewHolder(T model,CommonViewHolder viewHolder,int type,int position);
//        绑定数据要有对应的布局
        int getLayoutId(int type);
    }

//    绑定多类型的数据
    public interface OnMoreBingDataListener<T> extends OnBindDataListener<T>{
        int getItemType(int type);
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = onBindDataListener.getLayoutId(viewType);
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(parent, layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        onBindDataListener.onBindViewHolder(mList.get(position),holder
                ,getItemViewType(position),position);
    }

    @Override
    public int getItemCount() {
        return mList == null ?0 : mList.size();
    }
}
