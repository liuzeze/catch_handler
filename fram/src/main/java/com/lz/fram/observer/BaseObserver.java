package com.lz.fram.observer;


import com.lz.fram.base.BaseView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * -------- 日期 ---------- 维护人 ------------ 变更内容 --------
 * 2017/12/26	9:24	     刘泽			  公用的订阅处理 ResourceObserver
 *
 * @author Administrator
 */
public abstract class BaseObserver<T> implements Observer<T> {
    private BaseView mView;

    protected BaseObserver() {
    }

    protected BaseObserver(BaseView view) {
        this.mView = view;
    }


    @Override
    public void onSubscribe(Disposable d) {

    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        ApiException apiException = null;
        if (e instanceof ApiException) {
            apiException = (ApiException) e;
        } else {
            apiException = ApiException.handleException(e);
        }
        String message = apiException.getMessage();
        int code = apiException.getCode();
        onError(code, message);
        if (mView != null) {
            mView.showErrorMsg(message);
        }
    }

    @Override
    public void onComplete() {

    }


    protected void onError(int code, String mes) {

    }

}