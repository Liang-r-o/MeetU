package com.example.meet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.helper.GlideHelper;
import com.example.meet.R;
import com.example.meet.model.AddFriendModle;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddFriendAdapter extends RecyclerView.Adapter {

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT = 1;

    private Context mContext;
    private List<AddFriendModle> mList;

    private LayoutInflater inflater;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public AddFriendAdapter(Context mContext, List<AddFriendModle> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            return new TitleViewHolder(inflater.inflate(R.layout.layout_search_title_item, null));
        } else if (viewType == TYPE_CONTENT) {
            return new ContentViewHolder(inflater.inflate(R.layout.layout_search_user_item, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        AddFriendModle model = mList.get(position);
        if (model.getType() == TYPE_TITLE) {
            ((TitleViewHolder) holder).tv_title.setText(model.getTitle());
        } else if (model.getType() == TYPE_CONTENT) {
//            设置头像
            GlideHelper.loadUrl(mContext, model.getPhoto(), ((ContentViewHolder) holder).mIvPhoto);
//            设置性别
            ((ContentViewHolder) holder).mIvSex.setImageResource(model.isSex() ?
                    R.drawable.img_boy_icon : R.drawable.img_girl_icon);
//            设置昵称
            ((ContentViewHolder) holder).mTvNickname.setText(model.getNickName());
//            年龄
            ((ContentViewHolder) holder).mTvAge.setText(model.getAge() + "岁");
//            描述
            ((ContentViewHolder) holder).mTvDesc.setText(model.getDesc());

//            通讯录
            if (model.isContact()) {
                ((ContentViewHolder) holder).mLlContactInfo.setVisibility(View.VISIBLE);
                ((ContentViewHolder) holder).mTvContactName.setText(model.getContactName());
                ((ContentViewHolder) holder).mTvContactPhone.setText(model.getContactPhone());
            }
        }

//        点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    private void initView() {


    }


    class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mIvPhoto;
        private ImageView mIvSex;
        private TextView mTvNickname;
        private TextView mTvAge;
        private TextView mTvDesc;

        private LinearLayout mLlContactInfo;
        private TextView mTvContactName;
        private TextView mTvContactPhone;

        public ContentViewHolder(View itemView) {
            super(itemView);

            mLlContactInfo = itemView.findViewById(R.id.ll_contact_info);
            mTvContactName = itemView.findViewById(R.id.tv_contact_name);
            mTvContactPhone = itemView.findViewById(R.id.tv_contact_phone);

            mIvPhoto = itemView.findViewById(R.id.iv_photo);
            mIvSex = itemView.findViewById(R.id.iv_sex);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
            mTvAge = itemView.findViewById(R.id.tv_age);
            mTvDesc = itemView.findViewById(R.id.tv_desc);

        }
    }

    public interface OnClickListener {
        void onClick(int positioin);
    }
}
