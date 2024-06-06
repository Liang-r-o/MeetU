package com.example.meet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.framework.base.BaseUIActivity;
import com.example.framework.manager.MediaPlayerManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.TimeUtil;

import java.io.File;
import java.io.IOException;


public class MainActivity extends BaseUIActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MediaPlayerManager mediaPlayerManager = new MediaPlayerManager();
        AssetFileDescriptor fileDescriptor = getResources().openRawResourceFd(R.raw.guide);
        mediaPlayerManager.startPlay(fileDescriptor);

        mediaPlayerManager.setOnProgressListener(new MediaPlayerManager.OnMusicProgressListener() {
            @Override
            public void OnProgress(int progress,int pos) {
               // LogUtils.e("progress:" + progress + "All:" + mediaPlayerManager.getDuration());//progress:30245All:51144
               int p = (int)(((float)progress) / ((float)mediaPlayerManager.getDuration()) * 100);
               LogUtils.e("p:" + p);
            }
        });

    }
}