package com.lz.fram.base;


import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.lz.fram.observer.LifeCycleObseverble;

/**
 * -------- 日期 ---------- 维护人 ------------ 变更内容 --------
 * 2017/12/26	9:24	    刘泽			   presenter基类用于管理订阅监听以及注册view
 * 2017/12/26	9:24	    刘泽			    增加yyy属性
 */

public class RxPresenter<attachView extends BaseView> implements BasePresenterImpl<attachView> {
    protected attachView mBaseView;
    protected Context mContext;

    @Override
    public void attachView(attachView baseView) {
        this.mBaseView = baseView;
        mContext = mBaseView.getContext();
    }

    protected <LifeCycle> LifeCycleObseverble<LifeCycle> bindLifecycle() {
        if (null == mBaseView) {
            throw new NullPointerException("lifecycleOwner == null");
        }
        return new LifeCycleObseverble<>(mBaseView);
    }
    @Override
    public void onCreate(LifecycleOwner owner) {

    }

    @Override
    public void onStart(LifecycleOwner owner) {

    }

    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {

    }

    @Override
    public void onStop(LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        this.mBaseView = null;
        mContext = null;
        if (owner != null) {
            owner.getLifecycle().removeObserver(this);
        }
    }

}
