package lz.com.catch_handler;

import android.graphics.Color;
import android.view.View;


import com.lz.fram.base.FramBaseActivity;
import com.lz.fram.base.InjectLayout;
import com.lz.httplib.RxRequestUtils;
import com.lz.httplib.http.ConfigModule;
import com.lz.httplib.http.GlobalConfigBuild;
import com.lz.httplib.transformer.Transformer;

import io.reactivex.functions.Consumer;

@InjectLayout(layoutId = R.layout.activity_main,isShowActTitle = true)
public class MainActivity extends FramBaseActivity {


    @Override
    protected void initData() {

       /* new CatchHandler.Builder(getApplicationContext())
//                .setAutoSend(false)
                .setErrorLogPath(Environment.getExternalStorageDirectory() + "/errorlog")
                .setCatchCallback(new CatchCallback() {
                    @Override
                    public void exceptionInfo(@Nullable ExceptionInfoBean exceptionInfoBean, @Nullable Throwable throwable) {
                        StringBuilder erreoStr = CatchHandlerHelper.getExceptionInfoString(MainActivity.this, exceptionInfoBean);
                        RxRequestUtils
                                .create(ApiService.class)
                                .getNewsArticle2("", "")
                                .compose(Transformer.<String>switchSchedulersObser()).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                System.out.println("============" + s);
                            }
                        });
                    }
                })
//                .setUrl("钉钉机器人上传地址")
                .build();
//        StatusView.init(this).showContentView();

*/
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
                System.out.println("============" + s);
            }
        });
    }

    public void crash(View view) {
        int i = 1 / 0;
    }
}
