package com.lz.fram.observer;





import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author liuze
 */
public class LifeCycleObseverble<T> implements LifecycleObserver, ObservableTransformer<T, T> {


    public LifeCycleObseverble(Object baseView) {
        if (baseView instanceof AppCompatActivity) {
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
