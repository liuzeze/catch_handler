package com.lz.fram.observer;


import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lz.fram.utils.ActivityUtils;
import com.lz.fram.utils.LoadDialog;
import com.lz.httplib.util.JSONFactory;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/9/23-11:13
 * @desc : 修改内容
 * @version: 1.0
 */
public class JsonParseTransformer {


    public static ObservableTransformer<String, String> switchSchedulersStr() {
        final LoadDialog loadDialog = new LoadDialog(ActivityUtils.currentActivity());

        return new ObservableTransformer<String, String>() {
            @Override
            public ObservableSource<String> apply(Observable<String> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new MapFunction())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                loadDialog.show();

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadDialog.dismiss();
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<String, T> switchSchedulersObj(final Class<T> type) {
        final LoadDialog loadDialog = new LoadDialog(ActivityUtils.currentActivity());

        return new ObservableTransformer<String, T>() {
            @Override
            public ObservableSource<T> apply(Observable<String> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new MapFunction())
                        .flatMap(new Function<String, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(String s) throws Exception {
                                return Observable.just(JSONFactory.fromJson(TextUtils.isEmpty(s) ? "{}" : s, type));
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                                loadDialog.show();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadDialog.dismiss();
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<String, List<T>> switchSchedulersArray(final Class<T> clazz) {
        final LoadDialog loadDialog = new LoadDialog(ActivityUtils.currentActivity());
        return new ObservableTransformer<String, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(Observable<String> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new MapFunction())
                        .flatMap(new Function<String, ObservableSource<List<T>>>() {
                            @Override
                            public ObservableSource<List<T>> apply(String s) throws Exception {
                                Type type = TypeToken.getParameterized(List.class, clazz).getType();
                                List<T> list = JSONFactory.fromJson(s, type);
                                return Observable.just(list);
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                loadDialog.show();

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadDialog.dismiss();
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<String, T> switchSchedulers(final Type type) {
        final LoadDialog loadDialog = new LoadDialog(ActivityUtils.currentActivity());


        return new ObservableTransformer<String, T>() {
            @Override
            public ObservableSource<T> apply(Observable<String> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new MapFunction())
                        .flatMap(new Function<String, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(String s) throws Exception {
                                T t = JSONFactory.fromJson(s, type);
                                return Observable.just(t);
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                loadDialog.show();

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadDialog.dismiss();
                            }
                        });
            }
        };
    }

}
