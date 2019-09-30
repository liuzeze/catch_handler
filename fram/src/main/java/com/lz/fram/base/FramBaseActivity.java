package com.lz.fram.base;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.lz.fram.R;
import com.lz.fram.inject.InjectManager;
import com.lz.fram.inject.PresenterProviders;
import com.lz.fram.utils.ActivityUtils;
import com.lz.fram.view.SwipePanel;
import com.lz.fram.view.TitleToolbar;

public abstract class FramBaseActivity extends AppCompatActivity implements BaseView {

    protected Activity mActivity;
    private TitleToolbar mTitleToolbar;
    protected SwipePanel mSwipePanel;
    private PresenterProviders mPresenterProviders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfig();
        initData();
        initLisenter();

    }

    /**
     * 初始化公用的参数
     */
    protected void initConfig() {
        mActivity = this;
        //站栈管理
        ActivityUtils.addActivity(this);
        //布局文件设置
        InjectManager.inject(this);
        //presenter
        mPresenterProviders = InjectManager.attachPresenter(this, getLifecycle());
        //返回控件
        mSwipePanel = SwipePanel.init(mActivity);

    }


    protected TitleToolbar getTitleToolbar() {
        if (mTitleToolbar == null) {
            mTitleToolbar = findViewById(R.id.common_toolbar);
        }
        return mTitleToolbar;
    }


    @Override
    public Context getContext() {
        return mActivity;
    }


    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected void initLisenter() {
        if (mSwipePanel != null) {
            mSwipePanel.setOnFullSwipeListener(new SwipePanel.OnFullSwipeListener() {
                @Override
                public void onFullSwipe(int direction) {
                    mSwipePanel.close(direction);
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.finishActivity(this);
        if (mPresenterProviders != null) {
            mPresenterProviders.clear();
        }

    }
}
