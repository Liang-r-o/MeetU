package com.example.meet.ui;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

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
import com.example.meet.adapter.CommonAdapter;
import com.example.meet.adapter.CommonViewHolder;
import com.example.meet.model.AddFriendModle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ContactFriendActivity extends BaseBackActivity implements View.OnClickListener {

    private RecyclerView mMContactView;
    private Map<String,String> mContactMap = new HashMap<>();

    private AddFriendAdapter mAddFriendAdapter;
    private List<AddFriendModle> mList = new ArrayList<>();

    private CommonAdapter<AddFriendModle> mContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_friend);

        initView();
    }

    @Override
    public void onClick(View v) {

    }

    private void initView() {
        mMContactView = findViewById(R.id.mContactView);
        mMContactView.setLayoutManager(new LinearLayoutManager(this));
        mMContactView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        mContactAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<AddFriendModle>() {
            @Override
            public void onBindViewHolder(AddFriendModle model, CommonViewHolder viewHolder, int type, int position) {
                //设置头像
                viewHolder.setImageUrl(ContactFriendActivity.this, R.id.iv_photo, model.getPhoto());
                //设置性别
                viewHolder.setImageResource(R.id.iv_sex,
                        model.isSex() ? R.drawable.img_boy_icon : R.drawable.img_girl_icon);
                //设置昵称
                viewHolder.setText(R.id.tv_nickname, model.getNickName());
                //年龄
                viewHolder.setText(R.id.tv_age, model.getAge() + getString(R.string.text_search_age));
                //设置描述
                viewHolder.setText(R.id.tv_desc, model.getDesc());

                if (model.isContact()) {
                    viewHolder.getView(R.id.ll_contact_info).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_contact_name, model.getContactName());
                    viewHolder.setText(R.id.tv_contact_phone, model.getContactPhone());
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoActivity.startActivity(ContactFriendActivity.this,
                                model.getUserId());
                    }
                });
            }

            @Override
            public int getLayoutId(int type) {

                return R.layout.layout_search_user_item;

            }
        });

        mMContactView.setAdapter(mAddFriendAdapter);


//        读取联系人
        loadContact();

        loadUser();


    }

    /**
     * 加载用户
     */
    private void loadUser() {
        if (mContactMap.size() > 0){

//            for (int i = 10; i < mContactMap.size();i++){
//                mContactMap.remove(i);
//            }
            LogUtils.i("listLength:"+mContactMap.size());
            for (Map.Entry<String,String> entry : mContactMap.entrySet()){
                BmobManager.getInstance().queryPhoneUser(entry.getValue(), new FindListener<MUser>() {
                    @Override
                    public void done(List<MUser> list, BmobException e) {
                        if (e == null){
                            if (CommonUtils.isEmpty(list)){
                                MUser mUser = list.get(0);
                                addContent(mUser,entry.getKey(),entry.getValue());
                            }
                        }
                    }

                });
            }
        }
    }

//    加载手机联系人
    @SuppressLint("Range")
    private void loadContact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , null, null, null, null);

        String name;
        String phone;
        while (cursor.moveToNext()){

            name = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phone = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone = phone.replace(" ","").replace("-","");
//            LogUtils.i("name:"+name +" phone："+phone);
            mContactMap.put(name,phone);

        }
    }

    private void addContent(MUser mUser,String name,String phone){

        AddFriendModle modle = new AddFriendModle();
        modle.setType(AddFriendAdapter.TYPE_CONTENT);
        modle.setUserId(mUser.getObjectId());
        modle.setPhoto(mUser.getPhoto());
        modle.setAge(mUser.getAge());
        modle.setSex(mUser.isSex());
        modle.setNickName(mUser.getNickName());
        modle.setDesc(mUser.getDesc());

        modle.setContact(true);
        modle.setContactName(name);
        modle.setContactPhone(phone);

        mList.add(modle);
        mAddFriendAdapter.notifyDataSetChanged();


    }
}