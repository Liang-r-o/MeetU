package com.example.framework.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.example.framework.utils.LogUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 关于文件的帮助类
 */
public class FileHelper {

//    相机
    public static final int CAMERA_REQUEST_CODE = 1004;

//    相册
    public static final int ALBUM_REQUEST_CODE = 1005;
    private static volatile FileHelper mInstance = null;

    private File tempFile = null;
    private Uri imageUri = null;

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




    /**
     * 跳转到相机
     * @param mActivity
     */
    public void toCamera(Activity mActivity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = simpleDateFormat.format(new Date());
        tempFile = new File(Environment.getExternalStorageDirectory()+"/Download/MeetPhoto/"+fileName+".jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            imageUri = Uri.fromFile(tempFile);
        }else{
//            利用FileProvider
            imageUri = FileProvider.getUriForFile(mActivity,mActivity.getPackageName()
                    + ".fileprovider",tempFile);
//            安卓7.0添加权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        LogUtils.i("imageUri:"+imageUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

    }

    /**
     * 跳转到相册
     * @param mActivity
     */

    public void toAlbum(Activity mActivity){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent,ALBUM_REQUEST_CODE);
    }
    public File getTempFile(){

        return tempFile;

    }

    /**
     * 通过Uri去系统查找真实地址
     * @param mContext
     * @param uri
     */
    public String getRealPathFromURI(Context mContext,Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(mContext, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);

    }

}
