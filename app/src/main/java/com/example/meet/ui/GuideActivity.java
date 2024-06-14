package com.example.meet.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.viewpager.widget.ViewPager;

import com.example.framework.base.BaseActivity;
import com.example.framework.base.BasePageAdapter;
import com.example.framework.manager.MediaPlayerManager;
import com.example.framework.utils.AnimUtils;
import com.example.framework.utils.LogUtils;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    private ImageView ivMusicSwitch;
    private TextView tvGuideSkip;
    private ImageView ivGuidePoint1;
    private ImageView ivGuidePoint2;
    private ImageView ivGuidePoint3;

    public GuideActivity() {
    }

    /**
     * 1.ViewPager：适配器|帧动画播放
     * 2.小圆点的逻辑
     * 3.歌曲的播放
     * 4.属性动画旋转
     * 5.跳转
     */

//    代表ViewPager的三个View
    private View view1;
    private View view2;
    private View view3;

    private List<View> mPageList = new ArrayList<>();

    private BasePageAdapter mpageAdapter;

    private ImageView iv_guide_star;
    private ImageView iv_guide_smile;
    private ImageView iv_guide_night;
    private MediaPlayerManager mGuideMusic;

    private ObjectAnimator mAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        ivMusicSwitch = (ImageView) findViewById(R.id.iv_music_switch);
        tvGuideSkip = (TextView) findViewById(R.id.tv_guide_skip);
        ivGuidePoint1 = (ImageView) findViewById(R.id.iv_guide_point_1);
        ivGuidePoint2 = (ImageView) findViewById(R.id.iv_guide_point_2);
        ivGuidePoint3 = (ImageView) findViewById(R.id.iv_guide_point_3);

        ivMusicSwitch.setOnClickListener(this);
        tvGuideSkip.setOnClickListener(this);

        view1 = View.inflate(this,R.layout.layout_pager_guide_1,null);
        view2 = View.inflate(this,R.layout.layout_pager_guide_2,null);
        view3 = View.inflate(this,R.layout.layout_pager_guide_3,null);

        mPageList.add(view1);
        mPageList.add(view2);
        mPageList.add(view3);

//        预加载
        mViewPager.setOffscreenPageLimit(mPageList.size());

        mpageAdapter = new BasePageAdapter(mPageList);
        mViewPager.setAdapter(mpageAdapter);


//        帧动画
        iv_guide_star = view1.findViewById(R.id.iv_guide_star);
        iv_guide_night = view2.findViewById(R.id.iv_guide_night);
        iv_guide_smile = view3.findViewById(R.id.iv_guide_smile);

//        开始播放帧动画
        AnimationDrawable animStar = (AnimationDrawable)iv_guide_star.getBackground();
        animStar.start();

        AnimationDrawable animNight = (AnimationDrawable)iv_guide_night.getBackground();
        animNight.start();

        AnimationDrawable animSmile = (AnimationDrawable)iv_guide_smile.getBackground();
        animSmile.start();

//        小圆点逻辑
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //LogUtils.i("position:"+ position);
                selectPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        歌曲的逻辑
        startMusic();

    }

    /**
     * 播放音乐
     */
    private void startMusic() {
        mGuideMusic = new MediaPlayerManager();
        mGuideMusic.setLooping(true);
        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.guide);
        mGuideMusic.startPlay(file);
        //        旋转动画
        mAnim = AnimUtils.rotation(ivMusicSwitch);
        mAnim.start();
    }


//    动态选择小圆点

    private void selectPoint(int position) {
        switch (position){
            case 0:
                ivGuidePoint1.setImageResource(R.drawable.img_guide_point_p);
                ivGuidePoint2.setImageResource(R.drawable.img_guide_point);
                ivGuidePoint3.setImageResource(R.drawable.img_guide_point);
                break;
            case 1:
                ivGuidePoint1.setImageResource(R.drawable.img_guide_point);
                ivGuidePoint2.setImageResource(R.drawable.img_guide_point_p);
                ivGuidePoint3.setImageResource(R.drawable.img_guide_point);
                break;
            case 2:
                ivGuidePoint1.setImageResource(R.drawable.img_guide_point);
                ivGuidePoint2.setImageResource(R.drawable.img_guide_point);
                ivGuidePoint3.setImageResource(R.drawable.img_guide_point_p);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.iv_music_switch == id){
            if (mGuideMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PAUSE){
                mAnim.start();
                mGuideMusic.continuePlay();
                ivMusicSwitch.setImageResource(R.drawable.img_guide_music);
            }else if (mGuideMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PLAY){
                mAnim.pause();
                mGuideMusic.pausePlay();
                ivMusicSwitch.setImageResource(R.drawable.img_guide_music_off);
            }
        } else if (R.id.tv_guide_skip == id) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGuideMusic.stopPlaying();
    }
}