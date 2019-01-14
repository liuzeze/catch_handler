package lz.com.catch_handler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lz.com.acatch.UCEHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new UCEHandler.Builder(getApplicationContext())
                .setServiceUrl("钉钉机器人网址")
                .build();


//        int i=1/0;

    }
}
