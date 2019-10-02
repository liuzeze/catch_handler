package lz.com.catch_handler;

import android.view.View;

import com.lz.fram.base.FramBaseActivity;
import com.lz.fram.inject.InjectLayout;
import com.lz.fram.scope.AttachPresenter;
import com.lz.httplib.RxHttp;
import com.lz.httplib.bean.ParseInfo;
import com.lz.httplib.callback.APICallBack;
import com.lz.httplib.http.ConfigModule;
import com.lz.httplib.http.GlobalConfigBuild;


@InjectLayout(layoutId = R.layout.activity_main, isShowActTitle = true)
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

                    }
                })
//                .setUrl("钉钉机器人上传地址")
                .build();*/
//        StatusView.init(this).showContentView();
        RxHttp.initConfig(new ConfigModule() {
            @Override
            public void applyOptions(GlobalConfigBuild.Builder builder) {
                builder
                        .addInterceptor(new LoggerInterceptor())
                        .baseurl("http://lf.snssdk.com/api/")
                        .setPaeseInfor(new ParseInfo("errorCode", "data", "errorMsg", "0"))
                        .setAPICallBack(new APICallBack() {
                            @Override
                            public String callback(String code, String resultData) {
                                return null;
                            }
                        });

            }
        });
    }

    @AttachPresenter
    MainPresenter mMainPresenter;

    public void crash(View view) {
        mMainPresenter.crash();
    }
}
