package com.example.framework.manager;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;

public class MediaPlayerManager {

    public static final int MEDIA_STATUS_PLAY = 0;
    public static final int MEDIA_STATUS_PAUSE = 1;
    public static final int MEDIA_STATUS_STOP = 2;

    public int MEDIA_STATUS = MEDIA_STATUS_STOP;

    private MediaPlayer mediaPlayer;

    private static final int H_PROGRESS = 1000;

    private OnMusicProgressListener musicProgressListener;


    /**
     * 计算歌曲的进度
     * 1.开始播放的时候就开始循环计算时常
     * 2.将进度计算结果对外抛出
     */
    private Handler mHandler = new Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case H_PROGRESS:
                    if (musicProgressListener != null){
                        int currentPosition = getCunrrentPosition();
                        int pos = (int)(((float)currentPosition) / ((float)getDuration()) * 100);
                        musicProgressListener.OnProgress(currentPosition,pos);
                        mHandler.sendEmptyMessageDelayed(H_PROGRESS,1000);
                    }
                    break;
            }
            return false;
        }
    });


    public MediaPlayerManager(){
        mediaPlayer = new MediaPlayer();
    }


    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    /**
     * 开始播放
     * @param path
     */
    public void startPlay(AssetFileDescriptor path){

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            MEDIA_STATUS = MEDIA_STATUS_PLAY;
            mHandler.sendEmptyMessage(H_PROGRESS);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 暂停播放
     */
    public void pausePlay(){
        if (isPlaying()){
            mediaPlayer.pause();
            MEDIA_STATUS = MEDIA_STATUS_PAUSE;
            mHandler.removeMessages(H_PROGRESS);
        }
    }

    /**
     * 继续播放
     */
    public void continuePlay(){
        mediaPlayer.start();
        MEDIA_STATUS = MEDIA_STATUS_PLAY;
        mHandler.sendEmptyMessage(H_PROGRESS);
    }

    /**
     * 停止播放
     */
    public void stopPlaying(){
        mediaPlayer.stop();
        MEDIA_STATUS = MEDIA_STATUS_STOP;
        mHandler.removeMessages(H_PROGRESS);
    }

    /**
     * 获取当前位置
     */
    public int getCunrrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 获取总时长
     */
    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    /**
     * 是否循环
     * @param isLooping
     */
    public void setLooping(boolean isLooping){
        mediaPlayer.setLooping(isLooping);
    }

    /**
     * 跳转位置
     */
    public void seekTo(int ms){
        mediaPlayer.seekTo(ms);
    }

    /**
     * 播放结束
     * @param listener
     */
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        mediaPlayer.setOnCompletionListener(listener);
    }

    /**
     * 播放错误
     * @param listener
     */
    public void setOnErrorListener(MediaPlayer.OnErrorListener listener){
        mediaPlayer.setOnErrorListener(listener);
    }

    /**
     * 播放进度
     * @param
     */
    public void setOnProgressListener(OnMusicProgressListener listener){
         musicProgressListener = listener;
    }

    public interface OnMusicProgressListener{
        void OnProgress(int progress,int pos);
    }
}

