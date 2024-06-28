package com.example.framework.manager;

import static java.security.spec.MGF1ParameterSpec.SHA1;


import com.example.framework.utils.SHA1;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class HttpManager {
//    Url
    private static final String TOKEN_URL = "http://api.rong-api.com/user/getToken.json";
//    key
    private static final String CLOUD_KEY = "vnroth0kv2duo";

    private static final  String CLOUD_SECRET ="eKPrx8aATU24fj";
    private static volatile HttpManager mInstance = null;
    private OkHttpClient mOkHttpClient;
    private HttpManager(){
        mOkHttpClient = new OkHttpClient();
    }

    public static HttpManager getInstance(){
        if (mInstance == null){
            synchronized (HttpManager.class){
                if (mInstance == null){
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;
    }

    public String postCloudToken(HashMap<String,String> map){

//参数
        String Timestape = String.valueOf(System.currentTimeMillis() / 1000);
        String Nonce = String.valueOf(Math.floor(Math.random() * 100000));
        String Signature = com.example.framework.utils.SHA1.sha1(CLOUD_SECRET + Nonce + Timestape);

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()){
            builder.add(key,map.get(key));
        }
        RequestBody requestBody = builder.build();
//        添加签名规则
        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .addHeader("Timestamp",Timestape)
                .addHeader("App-Key",CLOUD_KEY)
                .addHeader("Nonce",Nonce)
                .addHeader("Signature",Signature)
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)
                .build();

        try {
            return mOkHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

     }
}
