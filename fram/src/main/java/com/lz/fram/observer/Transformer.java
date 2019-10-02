package com.lz.fram.observer;

import com.lz.fram.utils.ActivityUtils;
import com.lz.fram.utils.LoadDialog;
import com.lz.httplib.observer.JsonParseTransformer;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.ObservableTransformer;

/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/10/2-9:58
 * @desc : 修改内容
 * @version: 1.0
 */
public class Transformer {

    public static ObservableTransformer<String, String> switchSchedulersStr() {

        LoadDialog dialog = new LoadDialog(ActivityUtils.currentActivity());
        return JsonParseTransformer.switchSchedulersStr(dialog.getDialog());
    }

    public static <T> ObservableTransformer<String, T> switchSchedulersObj(final Class<T> type) {
        LoadDialog dialog = new LoadDialog(ActivityUtils.currentActivity());
        return JsonParseTransformer.switchSchedulersObj(type, dialog.getDialog());
    }

    public static <T> ObservableTransformer<String, List<T>> switchSchedulersArray(final Class<T> clazz) {
        LoadDialog dialog = new LoadDialog(ActivityUtils.currentActivity());
        return JsonParseTransformer.switchSchedulersArray(clazz, dialog.getDialog());
    }

    public static <T> ObservableTransformer<String, T> switchSchedulers(final Type type) {
        LoadDialog dialog = new LoadDialog(ActivityUtils.currentActivity());
        return JsonParseTransformer.switchSchedulers(type, dialog.getDialog());
    }
}
