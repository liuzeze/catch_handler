package lz.com.catch_handler;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/10/2-13:51
 * @desc : 修改内容
 * @version: 1.0
 */
public interface ApiServer {


    /**
     * @return
     */
    @GET("https://aip.baidubce.com/oauth/2.0/token?")
    Observable<ResToken> get(@Query("grant_type") String client_credentials,
                           @Query("client_id") String apikey,
                           @Query("client_secret") String apiSecret
    );
}
