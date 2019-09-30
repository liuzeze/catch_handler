package com.lz.httplib.api;


import com.lz.httplib.bean.SimpleParams;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2019-07-22       创建class
 */
public interface BaseApi {

    /**
     * 通用get
     */
    @GET
    Observable<String> get(@Url String url);


    /**
     * 通用POST
     *
     * @param url
     * @return
     */
    @POST
    Observable<String> post(@Url String url);

    /**
     * 通用get
     */
    @GET
    Observable<String> get(@Url String url, @QueryMap SimpleParams params);


    /**
     * 通用POST
     *
     * @param url
     * @param json
     * @return
     */
    @POST
    Observable<String> post(@Url String url, @Body String json);

    /**
     * 通用POST
     *
     * @param url
     * @param obj
     * @return
     */
    @POST
    Observable<String> post(@Url String url, @Body Object obj);

    /**
     * 通用POST
     *
     * @param url
     * @param json
     * @return
     */
    @POST
    Observable<String> post(@Url String url, @Body SimpleParams json);

}
