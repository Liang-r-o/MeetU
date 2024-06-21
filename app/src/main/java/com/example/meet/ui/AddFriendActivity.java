package com.example.meet.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.base.BaseBackActivity;
import com.example.framework.bomb.BmobManager;
import com.example.meet.R;

public class AddFriendActivity extends BaseBackActivity implements View.OnClickListener {

    /**
     * 1.模拟用户数据
     * 2.根据条件
     * 3.推荐数据
     */

    private LinearLayout mLlToContact;
    private EditText mEtPhone;
    private ImageView mIvSearch;
    private RecyclerView mMSearchResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friend);


        initView();
    }

    private void initView() {
        mLlToContact = (LinearLayout) findViewById(R.id.ll_to_contact);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mMSearchResultView = (RecyclerView) findViewById(R.id.mSearchResultView);

        mLlToContact.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.ll_to_contact){

        }else if (id == R.id.iv_search){
            queryPhoneFriend();
        }

    }
//工作电话号码查询
    private void queryPhoneFriend() {
//        1.获取电话号码
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,getString(R.string.text_login_phone_null),Toast.LENGTH_SHORT).show();
            return;
        }
//        2 查询
//        BmobManager.getInstance().queryPhoneUser(phone);
    }


}