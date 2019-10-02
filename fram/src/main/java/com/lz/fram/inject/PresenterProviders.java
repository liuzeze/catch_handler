package com.lz.fram.inject;



import androidx.lifecycle.Lifecycle;

import com.lz.fram.base.BasePresenterImpl;
import com.lz.fram.scope.AttachPresenter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * -------- 日期 ---------- 维护人 ------------ 变更内容 --------
 * 2018/7/30       刘泽
 */
public class PresenterProviders {


    private final String DEFAULT_KEY = "PresenterStore.DefaultKey";
    private HashMap<String, ? super BasePresenterImpl> mMap = new HashMap<>();


    public PresenterProviders(Object obj, Lifecycle lifecycle) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            //获取字段上的注解
            Annotation[] anns = field.getDeclaredAnnotations();
            if (anns.length < 1) {
                continue;
            }
            for (Annotation ann : anns) {
                if (ann instanceof AttachPresenter) {
                    Class<?> type = field.getType();
                    Object o = null;
                    try {
                        o = field.get(obj);
                        if (o == null) {
                            field.set(obj, type.newInstance());
                        }
                        String canonicalName = type.getName();

                        BasePresenterImpl presenter = (BasePresenterImpl) field.get(obj);
                        Object oldPresenter = mMap.put(DEFAULT_KEY + ":" + canonicalName, presenter);
                        if (oldPresenter != null) {
                            oldPresenter = null;
                        }
                        if (presenter != null) {
                            presenter.attachView(obj);
                            lifecycle.addObserver(presenter);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }


                    break;
                }
            }
        }
    }

    public final void clear() {
        mMap.clear();
        mMap = null;
    }


}
