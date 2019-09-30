package com.lz.httplib.http;


import com.lz.httplib.gson.GsonAdapter;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2018-10-16       创建class
 */
public class RetrofitFactory {
    private static RetrofitFactory instance;
    private final Retrofit mRetrofit;

    public static RetrofitFactory getInstance() {
        if (instance == null) {
            synchronized (RetrofitFactory.class) {
                if (instance == null) {
                    instance = new RetrofitFactory();
                }
            }
        }
        return instance;
    }

    public RetrofitFactory() {

        GlobalConfigBuild configBuild = HttpConfigFactory.getInstance().getConfigBuild();
        if (configBuild.getBaseUrl() == null) {
            throw new NullPointerException("请先配置地址参数！");
        }
        OkHttpClient build = OkhttpFactory.getInstance().getOkhttpBuilder().build();
        Retrofit.Builder client = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonAdapter.buildGson()))
                .baseUrl(configBuild.getBaseUrl())
                .client(build);

        if (configBuild != null) {
            if (configBuild.getRetrofitConfiguration() != null) {
                configBuild.getRetrofitConfiguration().configRetrofit(client);
            }
        }
        mRetrofit = client.build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public interface RetrofitConfiguration {
        void configRetrofit(Retrofit.Builder builder);
    }
}
