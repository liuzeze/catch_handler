package com.lz.httplib.observer;


import android.app.Dialog;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
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


    public static <T> ObservableTransformer<String, T> switchSchedulersObj(final Class<T> type, final Dialog dialog) {

        return new ObservableTransformer<String, T>() {
            @Override
            public ObservableSource<T> apply(Observable<String> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new MapFunction())
                        .map(new Function<String, T>() {
                            @Override
                            public T apply(String s) throws Exception {
                                return JSONFactory.fromJson(TextUtils.isEmpty(s) ? "{}" : s, type);
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                                if (dialog != null) {
                                    dialog.show();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<String, List<T>> switchSchedulersArray(final Class<T> clazz, final Dialog dialog) {
        return new ObservableTransformer<String, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(Observable<String> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new MapFunction())
                        .map(new Function<String, List<T>>() {
                            @Override
                            public List<T> apply(String s) throws Exception {
                                Type type = TypeToken.getParameterized(List.class, clazz).getType();
                                List<T> list = JSONFactory.fromJson(s, type);
                                return list;
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (dialog != null) {
                                    dialog.show();
                                }

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<String, T> switchSchedulersType(final Type type, final Dialog dialog) {


        return new ObservableTransformer<String, T>() {
            @Override
            public ObservableSource<T> apply(Observable<String> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new MapFunction())
                        .map(new Function<String, T>() {
                            @Override
                            public T apply(String s) throws Exception {
                                T t = JSONFactory.fromJson(s, type);
                                return t;
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (dialog != null) {
                                    dialog.show();
                                }

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        };
    }


    public static <T> ObservableTransformer<String, T> switchSchedulersNoBase(final Type type, final Dialog dialog) {


        return new ObservableTransformer<String, T>() {
            @Override
            public ObservableSource<T> apply(Observable<String> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new Function<String, T>() {
                            @Override
                            public T apply(String s) throws Exception {
                                T t = JSONFactory.fromJson(s, type);
                                return t;
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (dialog != null) {
                                    dialog.show();
                                }

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> switchThread(final Dialog dialog) {


        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (dialog != null) {
                                    dialog.show();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        };
    }


}
