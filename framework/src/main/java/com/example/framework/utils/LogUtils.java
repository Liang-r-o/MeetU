package com.example.framework.utils;


import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.framework.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log不光作为日志的打印，还可以记录日志
 */
public class LogUtils {
    private static final String TAG = "LogUtils";

    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    public static void i(String text){
        if (!TextUtils.isEmpty(text)){
            if (BuildConfig.LOG_DEBUG){
                Log.i(BuildConfig.LOG_TAG,text);
                writeToFile(text);
            }
        }
    }

    public static void e(String text){
        if (!TextUtils.isEmpty(text)){
            if (BuildConfig.LOG_DEBUG){
                Log.i(BuildConfig.LOG_TAG,text);
                writeToFile(text);
            }
        }
    }

    private static void writeToFile(String text)
    {




//        文件路径
        File file = Environment.getExternalStorageDirectory();
        Log.d(TAG,file.getAbsolutePath());
        String rootPath = file.getAbsolutePath();

        File filePath = new File(rootPath+"/Download/Meet");
        //        检查父路径
        if (!filePath.exists()){
            boolean mkdirs = filePath.mkdirs();
            Log.d(TAG,mkdirs+"");


        }

        String fileName = filePath + "/Meet.log";

//        日志内容：时间 + 内容
        String log = mSimpleDateFormat.format(new Date()) + " " + text + "\n";
//        开始写入
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(fileName,true);
//            编码问题 GBK才能正确的存入中文
             bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(fileOutputStream, Charset.forName("gbk")));
            bufferedWriter.write(log);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



}
