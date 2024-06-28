package com.example.meet;

import static cn.bmob.v3.util.BmobContentProvider.getUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.framework.base.BaseUIActivity;
import com.example.framework.bomb.BmobManager;
import com.example.framework.bomb.MUser;
import com.example.framework.entry.Constants;
import com.example.framework.gson.TokenBean;
import com.example.framework.java.SimulationData;
import com.example.framework.manager.DialogManager;
import com.example.framework.manager.HttpManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.framework.view.DialogView;
import com.example.meet.fragment.ChatFragment;
import com.example.meet.fragment.MeFragment;
import com.example.meet.fragment.SquareFragment;
import com.example.meet.fragment.StarFragment;
import com.example.meet.service.CloudService;
import com.example.meet.ui.FirstUploadActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseUIActivity implements View.OnClickListener {


    private Disposable disposable;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private LinearLayout mLlStar;
    private ImageView mIvStar;
    private TextView mTvStar;
    private StarFragment mStarFragment = null;
    private FragmentTransaction mStarTransaction = null;

    private LinearLayout mLlSquare;
    private ImageView mIvSquare;
    private TextView mTvSquare;
    private SquareFragment mSquareFragment = null;
    private FragmentTransaction mSquareTransaction = null;

    private LinearLayout mLlChat;
    private ImageView mIvChat;
    private TextView mTvChat;
    private ChatFragment mChatFragment = null;
    private FragmentTransaction mChatTransaction = null;


    private LinearLayout mLlMe;
    private ImageView mIvMe;
    private TextView mTvMe;
    private MeFragment mMeFragment = null;
    private FragmentTransaction mMeTransaction = null;

    public static MUser mUser;

    DialogView mUploadView;



    /**
     * 1.初始化Fragment
     * 2.显示Fragment
     * 3.隐藏所有的Fragment
     * 4.恢复Fragment
     * <p>
     * 优化的手段
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
//    跳转上传头像的回调code
    public static final int UPLOAD_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initView();


    }

    public static MUser getMuser(){
        return mUser;
    }

    /**
     * 初始化View
     */

    private void initView() {

        mUser = new MUser();

        //17939603000
//        mUser.setObjectId("37ea59cb9c");
//        mUser.setTokenPhoto("http://img5.duitang.com/uploads/item/201408/14/20140814165959_VCtan.thumb.700_0.png");
//        mUser.setTokenNickName("努力吧");

        //        17946606411
//        mUser.setObjectId("c18e38a8cf");
//        mUser.setTokenPhoto("http://pic2.zhimg.com/v2-e7807853bb7b3d49f883fe367413328d_b.jpg");
//        mUser.setTokenNickName("触碰岁月");

////        17934603000
            mUser.setObjectId("a1d8a72b60");
            mUser.setTokenPhoto("http://img.52z.com/upload/news/image/20180423/20180423052858_22336.jpg");
            mUser.setTokenNickName("安守");
        requestPermiss();

        mLlStar = findViewById(R.id.ll_star);
        mIvStar = findViewById(R.id.iv_star);
        mTvStar = findViewById(R.id.tv_star);
        mLlSquare = findViewById(R.id.ll_square);
        mIvSquare = findViewById(R.id.iv_square);
        mTvSquare = findViewById(R.id.tv_square);
        mLlChat = findViewById(R.id.ll_chat);
        mIvChat = findViewById(R.id.iv_chat);
        mTvChat = findViewById(R.id.tv_chat);
        mLlMe = findViewById(R.id.ll_me);
        mIvMe = findViewById(R.id.iv_me);
        mTvMe = findViewById(R.id.tv_me);

        mLlStar.setOnClickListener(this);
        mLlSquare.setOnClickListener(this);
        mLlChat.setOnClickListener(this);
        mLlMe.setOnClickListener(this);

//        设置文本
        mTvStar.setText(getString(R.string.text_main_star));
        mTvSquare.setText(getString(R.string.text_main_square));
        mTvChat.setText(getString(R.string.text_main_chat));
        mTvMe.setText(getString(R.string.text_main_me));

        initFragment();


//        切换默认的选项卡
        checkMainTab(0);

//        检查token
        checkToken();

//        SimulationData.testData();
    }


    /**
     * 检查token
     */
    private void checkToken() {



        if (mUploadView != null) {
            DialogManager.getInstance().hide(mUploadView);
        }
//        获取 token，需要三个参数 1.用户ID，2.头像地址 3.昵称
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
//       启动云服务连接融云服务
            startCloudService();
        } else {

            String tokenPhoto = mUser.getTokenPhoto();
            String tokenName = mUser.getTokenNickName();
//            1 有三个参数
//            String tokenPhoto = BmobManager.getInstance().getUser().getTokenPhoto();
//            String tokenName = BmobManager.getInstance().getUser().getTokenNickName();
            if (!TextUtils.isEmpty(tokenPhoto) && !TextUtils.isEmpty(tokenName)) {
//                创建token
                createToken();
            } else {
//                创建上传提示框
                createUploadDialog();
            }
        }


    }


    /**
     * 创建token
     */
    private void createToken() {


        /**
         * 1去融云后台获取token
         * 2连接融云
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", mUser.getObjectId());
        map.put("name", mUser.getTokenNickName());
        map.put("portraitUri", mUser.getTokenPhoto());

//                    通过Http去请求Token 异步操作
        disposable = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                            执行请求过程
                        String json = HttpManager.getInstance().postCloudToken(map);
                        emitter.onNext(json);
                        emitter.onComplete();
                    }

//                        线程调度
                }).subscribeOn(Schedulers.newThread()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
//                        解析token
                        parisingCloudToken(s);
                    }
                });

    }


    /**
     * 解析Token
     * @param s
     */
    private void parisingCloudToken(String s) {
        TokenBean tokenBean = new Gson().fromJson(s, TokenBean.class);
        if (tokenBean.getCode() == 200){
            if (!TextUtils.isEmpty(tokenBean.getToken())){
                SpUtils.getInstance().putString(Constants.SP_TOKEN,tokenBean.getToken());
                startCloudService();
            }
        }
    }


    private void startCloudService(){
        LogUtils.i("startCloudService");
        startService(new Intent(this, CloudService.class));
    }
    /**
     * 创建上传提示框
     */
    private void createUploadDialog() {
        mUploadView = DialogManager.getInstance().
                initView(this, R.layout.dialog_first_upload);

//        外部点击不能消失
        mUploadView.setCancelable(false);
        ImageView iv_go_upload = mUploadView.findViewById(R.id.iv_go_upload);
        iv_go_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mUploadView);
                FirstUploadActivity.startActivity(MainActivity.this,UPLOAD_REQUEST_CODE);
            }
        });

        DialogManager.getInstance().show(mUploadView);

    }


    /**
     *初始化fragment
     */
    private void initFragment() {
//        星球
        if (mStarFragment == null){
            mStarFragment = new StarFragment();
            mStarTransaction = getSupportFragmentManager().beginTransaction();
            mStarTransaction.add(R.id.mMainLayout,mStarFragment);
            mStarTransaction.commit();
        }

        //        广场
        if (mSquareFragment == null){
            mSquareFragment = new SquareFragment();
            mSquareTransaction = getSupportFragmentManager().beginTransaction();
            mSquareTransaction.add(R.id.mMainLayout,mSquareFragment);
            mSquareTransaction.commit();
        }

        //        聊天
        if (mChatFragment == null){
            mChatFragment = new ChatFragment();
            mChatTransaction = getSupportFragmentManager().beginTransaction();
            mChatTransaction.add(R.id.mMainLayout,mChatFragment);
            mChatTransaction.commit();
        }

        //        我的
        if (mMeFragment == null){
            mMeFragment = new MeFragment();
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeTransaction.add(R.id.mMainLayout,mMeFragment);
            mMeTransaction.commit();
        }
    }


    /**
     * 显示fragment
     * @param fragment
     */
    private void showFragment(Fragment fragment){
        if (fragment != null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAll(transaction);
            transaction.show(fragment);
            transaction.commitNowAllowingStateLoss();

        }
    }

    /**
     * 隐藏所有的fragment
     * @param transaction
     */

    private void hideAll(FragmentTransaction transaction){
        if (mStarFragment!=null){
            transaction.hide(mStarFragment);
        }
        if (mSquareFragment!=null){
            transaction.hide(mSquareFragment);
        }
        if (mChatFragment!=null){
            transaction.hide(mChatFragment);
        }
        if (mMeFragment!=null){
            transaction.hide(mMeFragment);
        }
    }


    /**
     * 切换主页选项卡
     * 0：星球 1：广场 2：聊天 3：我的
     * @param index
     */
    private void checkMainTab(int index){
        switch (index){
            case 0:
                showFragment(mStarFragment);
                mIvStar.setImageResource(R.drawable.img_star_p);
                mIvSquare.setImageResource(R.drawable.img_square);
                mIvChat.setImageResource(R.drawable.img_chat);
                mIvMe.setImageResource(R.drawable.img_me);

                mTvStar.setTextColor(getResources().getColor(R.color.colorAccent));
                mTvSquare.setTextColor(Color.BLACK);
                mTvChat.setTextColor(Color.BLACK);
                mTvMe.setTextColor(Color.BLACK);
                break;
            case 1:
                showFragment(mSquareFragment);
                mIvStar.setImageResource(R.drawable.img_star);
                mIvSquare.setImageResource(R.drawable.img_square_p);
                mIvChat.setImageResource(R.drawable.img_chat);
                mIvMe.setImageResource(R.drawable.img_me);

                mTvStar.setTextColor(Color.BLACK);
                mTvSquare.setTextColor(getResources().getColor(R.color.colorAccent));
                mTvChat.setTextColor(Color.BLACK);
                mTvMe.setTextColor(Color.BLACK);
                break;
            case 2:
                showFragment(mChatFragment);
                mIvStar.setImageResource(R.drawable.img_star);
                mIvSquare.setImageResource(R.drawable.img_square);
                mIvChat.setImageResource(R.drawable.img_chat_p);
                mIvMe.setImageResource(R.drawable.img_me);

                mTvStar.setTextColor(Color.BLACK);
                mTvSquare.setTextColor(Color.BLACK);
                mTvChat.setTextColor(getResources().getColor(R.color.colorAccent));
                mTvMe.setTextColor(Color.BLACK);
                break;
            case 3:
                showFragment(mMeFragment);
                mIvStar.setImageResource(R.drawable.img_star);
                mIvSquare.setImageResource(R.drawable.img_square);
                mIvChat.setImageResource(R.drawable.img_chat);
                mIvMe.setImageResource(R.drawable.img_me_p);

                mTvStar.setTextColor(Color.BLACK);
                mTvSquare.setTextColor(Color.BLACK);
                mTvChat.setTextColor(Color.BLACK);
                mTvMe.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
        }
    }



    /**
     * 防止重叠
     * 当应用内存紧张的时候，系统会回收掉Fragment对象
     * 再一次进入的时候会重新创建Fragment
     * 非原来的对象，我们无法控制，导致重叠
     * @param fragment The view that was clicked.
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (mStarFragment != null && fragment instanceof StarFragment){
            mStarFragment = (StarFragment) fragment;
        }
        if (mSquareFragment != null && fragment instanceof SquareFragment){
            mSquareFragment = (SquareFragment) fragment;
        }
        if (mChatFragment != null && fragment instanceof ChatFragment){
            mChatFragment = (ChatFragment) fragment;
        }
        if (mMeFragment != null && fragment instanceof MeFragment){
            mMeFragment = (MeFragment) fragment;
        }
        super.onAttachedToWindow();
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_star){
            checkMainTab(0);
        }else if (id == R.id.ll_square){
            checkMainTab(1);
        }else if (id == R.id.ll_chat){
            checkMainTab(2);
        }else if (id == R.id.ll_me){
            checkMainTab(3);
        }

    }

    private void requestPermiss(){
        request(PERMISSION_REQUEST_CODE,new OnPermissionsResult() {
            @Override
            public void OnSuccess() {
                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFail(List<String> noPermissions) {
                LogUtils.i("noPermissions:"+noPermissions.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == UPLOAD_REQUEST_CODE){
//                说明上传头像成功
                checkToken();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
