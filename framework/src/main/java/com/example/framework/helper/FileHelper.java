package com.example.framework.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 关于文件的帮助类
 */
public class FileHelper {

    private static volatile FileHelper mInstance = null;

    private SimpleDateFormat simpleDateFormat;
    private FileHelper(){
        simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    }

    public static FileHelper getInstance(){
        if (mInstance == null){
            synchronized (FileHelper.class){
                if (mInstance == null){
                    mInstance = new FileHelper();
                }
            }
        }

        return mInstance;
    }


    private File tempFile = null;
    private Uri imageUri;
    public void toCamera(Context mContext){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = simpleDateFormat.format(new Date());
        tempFile = new File(Environment.getExternalStorageDirectory()+"/data/"+fileName+".jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            imageUri = Uri.fromFile(tempFile);
        }else{
//            利用FileProvider
            imageUri = FileProvider.getUriForFile(mContext,mContext.getPackageName()
                    + ".fileprovider",tempFile);
        }
    }
}
