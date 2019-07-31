package com.lz.fram.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;


import com.lz.fram.R;
import com.lz.fram.inject.PresenterDispatch;
import com.lz.fram.inject.PresenterProviders;
import com.lz.fram.view.SwipePanel;
import com.lz.fram.view.TitleToolbar;

import io.reactivex.annotations.Nullable;


public abstract class FramBaseActivity extends FragmentActivity implements BaseView {

    protected Activity mActivity;
    private TitleToolbar titleToolbar;
    private SwipePanel swipePanel;

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
        InjectManager.getLayoutId(this);
        PresenterDispatch presenterDispatch = PresenterProviders.inject(this).presenterCreate();
        presenterDispatch.attachView(this, getLifecycle());
    }


    protected TitleToolbar getTitleToolbar() {
        if (titleToolbar == null) {
            titleToolbar = findViewById(R.id.common_toolbar);
        }
        return titleToolbar;
    }

    protected SwipePanel getSwipePanel() {
        if (swipePanel == null) {
            swipePanel = findViewById(R.id.sp_root);
        }
        return swipePanel;
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
        if (getSwipePanel() != null) {
            getSwipePanel().setOnFullSwipeListener(new SwipePanel.OnFullSwipeListener() {
                @Override
                public void onFullSwipe(int direction) {
                    getSwipePanel().close(direction);
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


}
