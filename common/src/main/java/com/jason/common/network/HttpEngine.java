package com.jason.common.network;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liuzhenhui on 2016/11/8.
 */
public class HttpEngine {
    public static final String TAG = HttpEngine.class.getSimpleName();
    private static final int DEFAULT_TIMEOUT = 10;
    private OkHttpClient okHttpClient;
    private String AccessToken = null;

    private static final class LazyHolder {
        private static final HttpEngine INSTANCE = new HttpEngine();
    }

    public static final HttpEngine getInstance() {
        return LazyHolder.INSTANCE;
    }

    public HttpEngine() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder()
//                        .addHeader("Content-Type", "application/json")
//                        .addHeader("Authorization", getAccessToken())
//                        .build();
//                return chain.proceed(request);
//            }
//        });
        builder.addInterceptor(new LogInterceptor());
        okHttpClient = builder.build();
    }

    /**
     * 获取一个指定baseUrl的retrofit客户端
     */
    public Retrofit getRetrofit(String url) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    public void setAccessToken(String accessToken) {
        this.AccessToken = accessToken;
    }

    private String getAccessToken() {
        if (TextUtils.isEmpty(AccessToken)) {
            return "";
        } else {
            return AccessToken;
        }
    }

}
