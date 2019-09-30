package com.lz.fram.observer;


import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;


import com.lz.fram.base.BaseView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 */
public class LifeCycleObseverble<T> implements LifecycleObserver, ObservableTransformer<T, T> {


    public LifeCycleObseverble(BaseView baseView) {
        if (baseView instanceof Activity) {
            ((AppCompatActivity) baseView).getLifecycle().addObserver(this);
        }
        if (baseView instanceof Fragment) {
            ((Fragment) baseView).getLifecycle().addObserver(this);
        }
    }

    public LifeCycleObseverble(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }


    private Disposable disposable;


    private void cancel(LifecycleOwner owner) {
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

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                LifeCycleObseverble.this.disposable = disposable;

            }
        });
    }
}
