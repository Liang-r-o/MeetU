package com.example.meet.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.framework.base.BaseActivity;
import com.example.framework.bomb.MyData;
import com.example.framework.utils.LogUtils;
import com.example.framework.view.TouchPictureV;
import com.example.meet.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class TestActivity extends BaseActivity implements View.OnClickListener{
    private com.example.framework.view.TouchPictureV TouchPictureV;
    private Button mBtnAdd;
    private Button mBtnDel;
    private Button mBtnQuery;
    private Button mBtnUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        initView();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add){
            MyData myData = new MyData();
            myData.setName("zhangsan");
            myData.setSex(0);
            myData.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        LogUtils.i("新增成功：" + s);
                    }
                }
            });

        }else if (id == R.id.btn_del){

        }else if (id == R.id.btn_query){

        }else if (id == R.id.btn_update){

        }

    }


    private void initView(){

        mBtnAdd = findViewById(R.id.btn_add);
        mBtnDel = findViewById (R.id.btn_del);
        mBtnQuery = findViewById (R.id.btn_query);
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnAdd.setOnClickListener(this);
        mBtnDel.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);

        TouchPictureV =  findViewById(R.id.TouchPictureV);
        TouchPictureV.setViewResultListener(new TouchPictureV.OnViewResultListener() {


            @Override
            public void onResult() {
                LogUtils.i("开始验证");
                Toast.makeText(TestActivity.this, "验证通过", Toast.LENGTH_SHORT).show();
            }
        });

    }
}