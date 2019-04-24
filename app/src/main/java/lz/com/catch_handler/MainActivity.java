package lz.com.catch_handler;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import lz.com.acatch.BaseSendError;
import lz.com.acatch.CatchCallback;
import lz.com.acatch.CatchHandler;
import lz.com.acatch.ExceptionInfoBean;
import lz.com.status.StatusView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CatchHandler.Builder(getApplicationContext())
                .setCatchHEnabled(true)
                .setAutoSend(false)
                .setUrl("钉钉机器人上传地址")
                .build();
        StatusView.init(this).showContentView();
    }

    public void crash(View view) {
        int i = 1 / 0;
    }
}
