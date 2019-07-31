package com.lz.httplib;


import com.lz.httplib.download.DownParamBean;
import com.lz.httplib.download.RxDownloadManager;
import com.lz.httplib.http.ConfigModule;
import com.lz.httplib.http.HttpConfigFactory;
import com.lz.httplib.http.RetrofitFactory;
import com.lz.httplib.upload.UploadRetrofit;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2018-10-16       创建class
 */
public class RxRequestUtils {

    public static void initConfig(ConfigModule options) {
        HttpConfigFactory.getInstance().initConfig(options);
    }

    public static <T> T create(Class<T> apiClass) {
        return RetrofitFactory
                .getInstance()
                .getRetrofitClient()
                .build()
                .create(apiClass);

    }


    public static void downLoad(String url, String fileName, String filePath, Consumer<DownParamBean> consumer) {
        RxDownloadManager.getInstance().downLoad(url, fileName, filePath, consumer);
    }

    public static void downLoadPause(String url) {
        RxDownloadManager.getInstance().pause(url);
    }

    public static void downLoadCancel(String url) {
        RxDownloadManager.getInstance().cancel(url);
    }

    public static Observable<ResponseBody> uploadFile(String url, String file, String fileName) {
        return UploadRetrofit.getInstance().uploadImage(url,
                file + File.separator + fileName);


    }


}