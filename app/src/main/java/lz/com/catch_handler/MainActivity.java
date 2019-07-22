package lz.com.catch_handler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lz.httplib.RxRequestUtils;
import com.lz.httplib.http.ConfigModule;
import com.lz.httplib.http.GlobalConfigBuild;
import com.lz.httplib.transformer.Transformer;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        new CatchHandler.Builder(getApplicationContext())
                .setCatchHEnabled(true)
                .setAutoSend(false)
                .setUrl("钉钉机器人上传地址")
                .build();
        StatusView.init(this).showContentView();*/
        RxRequestUtils.initConfig(new ConfigModule() {
            @Override
            public void applyOptions(GlobalConfigBuild.Builder builder) {

                builder.baseurl("http://lf.snssdk.com/api/");

            }
        });
        RxRequestUtils
                .create(ApiService.class)
                .getNewsArticle2("推荐", "")
                .compose(Transformer.<String>switchSchedulersObser()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("============"+s);
            }
        });
    }

//    public void crash(View view) {
//        int i = 1 / 0;
//    }
}
