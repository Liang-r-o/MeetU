package com.example.meet.fragment.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.framework.bomb.BmobManager;
import com.example.framework.bomb.MUser;
import com.example.framework.cloud.CloudManager;
import com.example.framework.gson.TextBean;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.meet.R;
import com.example.meet.adapter.CommonAdapter;
import com.example.meet.adapter.CommonViewHolder;
import com.example.meet.model.ChatRecordModel;
import com.example.meet.ui.ChatActivity;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.rong.imlib.IRongCoreCallback;
import io.rong.imlib.IRongCoreEnum;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatRecordFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private View item_empty_view;
    private SwipeRefreshLayout mMChatRecordRefreshLayout;
    private RecyclerView mMChatRecordView;

    private CommonAdapter<ChatRecordModel> mChatRecordAdapter;
    private List<ChatRecordModel> mList = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_record, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        item_empty_view = view.findViewById(R.id.item_empty_view);
        mMChatRecordRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mChatRecordRefreshLayout);
        mMChatRecordView = (RecyclerView) view.findViewById(R.id.mChatRecordView);

        mMChatRecordRefreshLayout.setOnRefreshListener(this);
        mMChatRecordView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMChatRecordView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        mChatRecordAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<ChatRecordModel>() {
            @Override
            public void onBindViewHolder(ChatRecordModel model, CommonViewHolder viewHolder, int type, int position) {

                viewHolder.setImageUrl(getActivity(),R.id.iv_photo,model.getUrl());
                viewHolder.setText(R.id.tv_nickname, model.getNickName());
                viewHolder.setText(R.id.tv_content, model.getEndMsg());
                viewHolder.setText(R.id.tv_time, model.getTime());

                if(model.getUnReadSize() == 0){
                    viewHolder.getView(R.id.tv_un_read).setVisibility(View.GONE);
                }else{
                    viewHolder.getView(R.id.tv_un_read).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_un_read, model.getUnReadSize() + "");
                }

//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ChatActivity.startActivity(getActivity(),
//                                model.getUserId(),model.getNickName(),model.getUrl());
//                    }
//                });
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_chat_record_item;
            }
        });

        mMChatRecordView.setAdapter(mChatRecordAdapter);
        queryChatRecord();

    }

    /**
     * 查询聊天记录
     */
    private void queryChatRecord() {
        mMChatRecordRefreshLayout.setRefreshing(true);
        CloudManager.getInstance().getConversationList(new IRongCoreCallback.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                mMChatRecordRefreshLayout.setRefreshing(false);
                if (CommonUtils.isEmpty(conversations)){

                    if (mList.size() > 0){
                        mList.clear();
                    }

                    for (int i = 0; i < conversations.size(); i++){
                        Conversation c = conversations.get(i);
                        String id = c.getTargetId();
//                        有了Id就可以去对象的信息了
                        BmobManager.getInstance().queryObjectIdUser(id, new FindListener<MUser>() {
                            @Override
                            public void done(List<MUser> list, BmobException e) {
                                if (e == null){
                                    if (CommonUtils.isEmpty(list)){
                                        MUser mUser = list.get(0);
                                        ChatRecordModel chatRecordModel = new ChatRecordModel();
                                        chatRecordModel.setUrl(mUser.getPhoto());
                                        chatRecordModel.setNickName(mUser.getNickName());
                                        chatRecordModel.setTime(new SimpleDateFormat("HH:mm:ss").format(c.getReceivedTime()));
                                        chatRecordModel.setUnReadSize(c.getUnreadMessageCount());

                                        String objectName = c.getObjectName();
                                        if (objectName.equals(CloudManager.MSG_TEXT_NAME)){
                                            TextMessage textMessage = (TextMessage) c.getLatestMessage();
                                            String msg = textMessage.getContent();
                                            TextBean textBean = new Gson().fromJson(msg, TextBean.class);
                                            if (textBean.getType().equals(CloudManager.TYPE_TEXT)){
                                                chatRecordModel.setEndMsg(textBean.getMsg());
                                                mList.add(chatRecordModel);
                                            }

                                        }else if (objectName.equals(CloudManager.MSG_IMAGE_NAME)){
                                            chatRecordModel.setEndMsg(getString(R.string.text_chat_record_img));
                                            mList.add(chatRecordModel);
                                        }else if (objectName.equals(CloudManager.MSG_LOCATION_NAME)){
                                            chatRecordModel.setEndMsg(getString(R.string.text_chat_record_location));
                                            mList.add(chatRecordModel);
                                        }
                                        //                                        不能在这添加，在这add的话，如果不是普通文本（比如添加好友信息）还是会add
//                                        mList.add(chatRecordModel);

                                        mChatRecordAdapter.notifyDataSetChanged();

//                                        排除一些不确定的因素
                                        if (mList.size() > 0){
                                            item_empty_view.setVisibility(View.GONE);
                                            mMChatRecordView.setVisibility(View.VISIBLE);
                                        }else {
                                            item_empty_view.setVisibility(View.VISIBLE);
                                            mMChatRecordView.setVisibility(View.GONE);
                                        }

                                     }
                                }
                            }
                        });
                    }
                }else{
//                    为空的情况也要处理
                    mMChatRecordRefreshLayout.setRefreshing(false);
                    item_empty_view.setVisibility(View.VISIBLE);
                    mMChatRecordView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(IRongCoreEnum.CoreErrorCode e) {
                mMChatRecordRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
//        不在刷新的时候才会调用查询
        if (mMChatRecordRefreshLayout.isRefreshing()){
            queryChatRecord();
        }
    }
}