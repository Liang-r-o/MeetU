package com.example.framework.base;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;

import java.util.Objects;

public class BaseBackActivity extends BaseActivity {


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        getActionBar();
        if (supportActionBar != null){
            //显示返回键
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           // 清除阴影
            getSupportActionBar().setElevation(0);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}