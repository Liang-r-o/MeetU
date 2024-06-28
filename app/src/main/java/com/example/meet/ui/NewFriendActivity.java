package com.example.meet.ui;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.bomb.BmobManager;
import com.example.framework.bomb.MUser;
import com.example.framework.cloud.CloudManager;
import com.example.framework.db.NewFriend;
import com.example.framework.helper.LitePalHelper;
import com.example.framework.utils.CommonUtils;
import com.example.meet.MainActivity;
import com.example.meet.R;
import com.example.meet.adapter.CommonAdapter;
import com.example.meet.adapter.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewFriendActivity extends AppCompatActivity {
    /**
     * 1.查询好友的申请列表
     * 2.通过适配器显示出来
     * 3.如果同意则添加对方为自己的好友
     * 4.并且发送给对方自定义的消息
     * 5.对方将添加到好友列表中
     */

    private MUser mUser;

    private View mItemEmptyView;
    private RecyclerView mMNewFriendView;

    private Disposable disposable;

    private CommonAdapter<NewFriend> mNewFriendAdapter;

    private List<NewFriend> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_friend);

        initView();
    }

    private void initView() {
        mItemEmptyView = (View) findViewById(R.id.item_empty_view);
        mMNewFriendView = (RecyclerView) findViewById(R.id.mNewFriendView);
        mMNewFriendView.setLayoutManager(new LinearLayoutManager(this));
        mMNewFriendView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mNewFriendAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<NewFriend>() {
            @Override
            public void onBindViewHolder(NewFriend model, CommonViewHolder viewHolder, int type, int position) {
//                根据Id查询用户信息
                BmobManager.getInstance().queryObjectIdUser(model.getId(), new FindListener<MUser>() {
                    @Override
                    public void done(List<MUser> list, BmobException e) {
//                        填充具体属性
                        if (e == null){
//                            要添加的好友信息
                            mUser = list.get(0);
                            viewHolder.setImageUrl(NewFriendActivity.this,R.id.iv_photo,mUser.getPhoto());
                            viewHolder.setImageResource(R.id.iv_sex,mUser.isSex()?R.drawable.img_boy_icon:R.drawable.img_girl_icon);
                            viewHolder.setText(R.id.tv_nickname,mUser.getNickName());
                            viewHolder.setText(R.id.tv_age,mUser.getAge()+getString(R.string.text_search_age));
                            viewHolder.setText(R.id.tv_desc,mUser.getDesc());
                            viewHolder.setText(R.id.tv_msg,model.getMsg());


                            if (model.getIsAgree() == 0){
                                viewHolder.getView(R.id.ll_agree).setVisibility(View.GONE);
                                viewHolder.getView(R.id.tv_result).setVisibility(View.VISIBLE);
                                viewHolder.setText(R.id.tv_result,"已同意");
                            }else if (model.getIsAgree() == -1){
                                viewHolder.getView(R.id.ll_agree).setVisibility(View.VISIBLE);
                                viewHolder.getView(R.id.tv_result).setVisibility(View.GONE);
                                viewHolder.setText(R.id.tv_result,"已拒绝");
                            }
                        }

                    }
                });
//                同意
                viewHolder.getView(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * 1.同意则刷新当前的item
                         * 2.将好友添加到自己的好友列表
                         * 3.通知对方已经同意了
                         * 4.对方将我添加到好友列表
                         * 5.刷新好友列表
                         */
                        updatItem(position,0);
                        BmobManager.getInstance().addFriend(MainActivity.mUser, mUser, new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
//                                    保存成功
//                                    通知对方
                                    CloudManager.getInstance().sendTextMessage("",
                                            CloudManager.TYPE_ARGEED_FRIEND,mUser.getObjectId());
                                }
                            }
                        });
                    }
                });

                //                拒绝
                viewHolder.getView(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatItem(position, 1);
                    }
                });
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_new_friend_item;
            }
        });
        mMNewFriendView.setAdapter(mNewFriendAdapter);
        queryNewFriend();
    }

    /**
     * 更新item
     * @param position
     * @param i
     */
    private void updatItem(int position, int i) {
        NewFriend newFriend = mList.get(position);
//        更新数据库
        LitePalHelper.getInstance().updateNewFriend(newFriend.getId(),i);
//        更新本地的数据源
        newFriend.setIsAgree(i);
        mList.set(position,newFriend);
        mNewFriendAdapter.notifyDataSetChanged();
    }


    /**
     * 查询新朋友
     */
    private void queryNewFriend(){
        /**
         * 在子线程中获取好友申请列表，在主线程中更新我们的UI
         * RxJava 线程调度
         */

        disposable = Observable.create(new ObservableOnSubscribe<List<NewFriend>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NewFriend>> emitter) throws Exception {
                emitter.onNext(LitePalHelper.getInstance().queryNewFriend());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<NewFriend>>() {
                            @Override
                            public void accept(List<NewFriend> newFriends) throws Exception {
//                                更新UI
                                if (CommonUtils.isEmpty(newFriends)){
                                    mList.addAll(newFriends);
                                    mNewFriendAdapter.notifyDataSetChanged();
                                }else {
                                    mItemEmptyView.setVisibility(View.VISIBLE);
                                    mMNewFriendView.setVisibility(View.GONE);
                                }
                            }
                        });


//        LitePalHelper.getInstance().queryNewFriend();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed()){
            disposable.dispose();
        }
    }
}