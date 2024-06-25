package com.example.meet.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.base.BaseBackActivity;
import com.example.framework.bomb.BmobManager;
import com.example.framework.bomb.MUser;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.meet.R;
import com.example.meet.adapter.AddFriendAdapter;
import com.example.meet.model.AddFriendModle;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFriendActivity1 extends BaseBackActivity implements View.OnClickListener {

    /**
     * 1.模拟用户数据
     * 2.根据条件
     * 3.推荐数据
     */

    private LinearLayout mLlToContact;
    private EditText mEtPhone;
    private ImageView mIvSearch;
    private RecyclerView mMSearchResultView;

    private View include_empty_view;

    private AddFriendAdapter mAddFriendAdapter;

    private List<AddFriendModle> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friend);


        initView();
    }


    /**
     * 初始化View
     */
    private void initView() {
        include_empty_view = findViewById(R.id.include_empty_view);
        mLlToContact = (LinearLayout) findViewById(R.id.ll_to_contact);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mMSearchResultView = (RecyclerView) findViewById(R.id.mSearchResultView);

        mLlToContact.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);

//        列表的实现
        mMSearchResultView.setLayoutManager(new LinearLayoutManager(this));
        mMSearchResultView.addItemDecoration(new DividerItemDecoration
                (this,DividerItemDecoration.VERTICAL));
        mAddFriendAdapter = new AddFriendAdapter(this,mList);
        mMSearchResultView.setAdapter(mAddFriendAdapter);

        mAddFriendAdapter.setOnClickListener(new AddFriendAdapter.OnClickListener() {
            @Override
            public void onClick(int positioin) {
                Toast.makeText(AddFriendActivity1.this,"positioin"+positioin,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//跳转到通讯录
        if (id == R.id.ll_to_contact){
//  处理权限 为什么还要判断呢，用户在第一次同意该权限后，在跳转到通讯录之前又不小心把它关了，所以在跳转之前再次检查一遍权限
            if (checkPermissions(Manifest.permission.READ_CONTACTS)){
                startActivity(new Intent(this,ContactFriendActivity.class));
            }else {
                requestPermission(new String[]{Manifest.permission.READ_CONTACTS},1001);
            }

        }else if (id == R.id.iv_search){
            queryPhoneUser();
        }
    }
//工作电话号码查询
    private void queryPhoneUser() {
//        1.获取电话号码
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,getString(R.string.text_login_phone_null),Toast.LENGTH_SHORT).show();
            return;
        }

//        2 过滤自己
//        String phoneNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
//        if (phone.equals(phoneNumber)){
//            Toast.makeText(this,"不能查询自己",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        3 查询
        BmobManager.getInstance().queryPhoneUser(phone, new FindListener<MUser>() {
            @Override
            public void done(List<MUser> list, BmobException e) {
                if (e != null){
                    return;
                }
                if (CommonUtils.isEmpty(list)){
                    MUser mUser = list.get(0);
                    LogUtils.i("muser:"+mUser.toString());
                    include_empty_view.setVisibility(View.GONE);
                    mMSearchResultView.setVisibility(View.VISIBLE);
//                    每次你查询有数据则清空
                    mList.clear();

                    addTitle("查询结果");
                    addContent(mUser);
                    mAddFriendAdapter.notifyDataSetChanged();

//                    推荐
                    pushUser();

                }else {
//                    显示空数据
                    include_empty_view.setVisibility(View.VISIBLE);
                    mMSearchResultView.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 推荐好友
     */
    private void pushUser() {
//        查询所有的好友，取100
        BmobManager.getInstance().queryAllUser(new FindListener<MUser>() {
            @Override
            public void done(List<MUser> list, BmobException e) {
                if (e == null){
                    if (CommonUtils.isEmpty(list)){
                        addTitle("推荐好友");
//                        如果好友的个数小于100个，直接取所有的好友，如何大于等于100个，则取100个
                        int num  = Math.min(list.size(), 20);
                        for (int i = 0; i < num; i++){
                            addContent(list.get(i));
                        }
                        mAddFriendAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }


    /**
     * 添加头部
     * @param title
     */
    private void addTitle(String title){
        AddFriendModle modle = new AddFriendModle();
        modle.setType(AddFriendAdapter.TYPE_TITLE);
        modle.setTitle(title);
        mList.add(modle);

    }

    /**
     * 添加内容
     * @param mUser
     */
    private void addContent(MUser mUser){

        AddFriendModle modle = new AddFriendModle();
        modle.setType(AddFriendAdapter.TYPE_CONTENT);
        modle.setUserId(mUser.getObjectId());
        modle.setPhoto(mUser.getPhoto());
        modle.setAge(mUser.getAge());
        modle.setSex(mUser.isSex());
        modle.setNickName(mUser.getNickName());
        modle.setDesc(mUser.getDesc());
        mList.add(modle);


     }


}