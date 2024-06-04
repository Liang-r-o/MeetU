package com.example.meet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.framework.utils.LogUtils;
import com.example.framework.utils.TimeUtil;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

            LogUtils.i(TimeUtil.formatDuring(System.currentTimeMillis()));



    }




}