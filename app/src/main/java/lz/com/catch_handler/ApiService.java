package lz.com.catch_handler;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2019-07-22       创建class
 */
public interface ApiService {
    @GET("news/feed/v62/?iid=12507202490&device_id=37487219424&refer=1&count=20&aid=13")
    Observable<String> getNewsArticle2(
            @Query("category") String category,
            @Query("max_behot_time") String maxBehotTime
    );

    @POST("news/feed/v62/?iid=12507202490&device_id=37487219424&refer=1&count=20&aid=13")
    Observable<String> sendServer(
            @Query("category") String category,
            @Query("max_behot_time") String maxBehotTime
    );

}
