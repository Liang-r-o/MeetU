package com.example.framework.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.event.EventManager;
import com.example.framework.event.MessageEvent;
import com.example.framework.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 *      * -BaseActivity:所有的统一功能：语言切换，请求权限等
 *      *     - BaseUIActivity：单一功能：沉浸式
 *      *     - BaseBackActivity：返回键
 *      *     - ...
 */

public class BaseActivity extends AppCompatActivity {

//    申请运行时权限的Code

//    声明所需权限
    private String[] mStrPermission = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_SETTINGS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WAKE_LOCK,
            android.Manifest.permission.READ_CONTACTS
    };

//    保存没有同意的权限
    private List<String> mPerList = new ArrayList<>();

//    保存没有同意失败的权限
    private List<String> mPerNoList = new ArrayList<>();

    private OnPermissionsResult permissionsResult;

    private int requestCode;

    /**
     * 一个方法请求权限
     * @param requestCode
     * @param permissionsResult
     */
    protected void request(int requestCode,OnPermissionsResult permissionsResult){
        if (!checkPermissionsAll()){
            requestPermissionAll(requestCode,permissionsResult);
        }
    }



     /**
     * 判断单个权限
      */
    protected boolean checkPermissions(String permissions){
        int check = checkSelfPermission(permissions);
        return check == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断所有权限
     */
    protected boolean checkPermissionsAll(){
        mPerList.clear();
        for (String s : mStrPermission) {
            boolean check = checkPermissions(s);
//            如果不同意则请求
            if (!check) {
                mPerList.add(s);
            }
        }
        return mPerList.isEmpty();
    }

    /**
     *
     * @param mPermissions 申请单个权限
     * @param requestCode
     */
    protected void requestPermission(String[] mPermissions,int requestCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mPermissions,requestCode);
        }
    }

    /**
     * 申请所有权限
     * @param requestCode
     */
    protected void requestPermissionAll(int requestCode,OnPermissionsResult permissionsResult){
        this.requestCode = requestCode;
        this.permissionsResult = permissionsResult;
        requestPermission(mPerList.toArray(new String[mPerList.size()]),requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == this.requestCode) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED){
//                        有失败的权限
                        mPerNoList.add(permissions[i]);
                    }
                }
                if (permissionsResult != null){
                    permissionsResult.OnSuccess();
                }else {
                    permissionsResult.OnFail(mPerNoList);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    protected interface OnPermissionsResult{
        void OnSuccess();
        void OnFail(List<String> noPermissions);
    }

    /**
     * 判断窗口权限
     * @return
     */

    protected boolean checkedWindowPermissions(){
        return Settings.canDrawOverlays(this);
    }


    /**
     * 请求窗口权限
     * @param requestCode
     */
    protected void requestWindowPermission(int requestCode){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:"+getPackageName()));
        startActivityForResult(intent,requestCode);

    }

    /**
     * EventBus的步骤
     * 1注册
     * 2声明注册方法 onEvent
     * 3发送事件
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventManager.register(this);
     }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // Do something


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.unregister(this);

    }

}

