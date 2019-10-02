package com.lz.httplib.observer;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * -------- 日期 ---------- 维护人 ------------ 变更内容 --------
 * 2017/12/26	9:24	     刘泽			  公用的基类订阅处理  添加生命周期管理  以及错误信息格式化处理
 *
 * @author Administrator ResourceObserver
 */
public abstract class BaseObserver<T> implements Observer<T>, LifecycleObserver {

    /**
     *要想实现生命周期的去掉订阅操作必须传入可以获取到lifeCycle 的对象
     *
     * @param lifecycle
     */
    public BaseObserver(Object lifecycle) {
        if (lifecycle != null) {
            if (lifecycle instanceof AppCompatActivity) {
                ((AppCompatActivity) lifecycle).getLifecycle().addObserver(this);
                return;
            }
            if (lifecycle instanceof Fragment) {
                ((Fragment) lifecycle).getLifecycle().addObserver(this);
                return;
            }
            if (lifecycle instanceof Lifecycle) {
                ((Lifecycle) lifecycle).addObserver(this);
                return;
            }

        }
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        BaseException apiException = null;
        if (e instanceof BaseException) {
            apiException = (BaseException) e;
        } else {
            apiException = getErrorType(e);
        }
        String message = apiException.getMessage();
        int code = apiException.getCode();
        onError(code, message);

    }

    @Override
    public void onComplete() {

    }


    protected void onError(int code, String mes) {

    }

    protected BaseException getErrorType(Throwable e) {
        return DefaultException.handleException(e);
    }

    private Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    private final void cancel(LifecycleOwner owner) {
        if (this.disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (owner != null) {
            owner.getLifecycle().removeObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        this.cancel(owner);
    }
}