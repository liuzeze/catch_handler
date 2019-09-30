package com.lz.fram.inject;


import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lz.fram.R;
import com.lz.fram.view.TitleToolbar;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2019-04-03       创建class
 */
public class InjectManager {


    public static void inject(FragmentActivity activity) {
        injectActivity(activity);
    }

    public static View inject(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
        View view = getView(fragment.getClass(), inflater, container);
        return view;
    }

    public static PresenterProviders attachPresenter(Object object, Lifecycle lifecycle) {
        return new PresenterProviders(object, lifecycle);
    }

    /**
     * Activity注册
     *
     * @param target
     */
    private static void injectActivity(Activity target) {
        Class<? extends Activity> aClass = target.getClass();
        InjectLayout injectLayout = aClass.getAnnotation(InjectLayout.class);
        LinearLayout linearLayout = (LinearLayout) target.getLayoutInflater().inflate(R.layout.layout_root, null);
        if (injectLayout != null) {
            int value = injectLayout.layoutId();
            if (injectLayout.isShowActTitle()) {
                TitleToolbar titleToolbar = linearLayout.findViewById(R.id.common_toolbar);
                titleToolbar.setTitle(injectLayout.titleName());
                titleToolbar.setBackVisible(injectLayout.actBackIcon());
            } else {
                linearLayout.removeAllViews();
            }
            target.getLayoutInflater().inflate(value, linearLayout);
        }
        target.setContentView(linearLayout);
    }


    public static View getView(Class tClass, LayoutInflater inflater, @Nullable ViewGroup container) {

        Class<?> aClass = tClass;
        InjectLayout injectLayout = aClass.getAnnotation(InjectLayout.class);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.layout_root, null);
        if (injectLayout != null) {
            if (injectLayout.isShowFragTitle()) {
                TitleToolbar titleToolbar = linearLayout.findViewById(R.id.common_toolbar);
                titleToolbar.setTitle(injectLayout.titleName());
                titleToolbar.setBackVisible(injectLayout.fragBackIcon());
                inflater.inflate(injectLayout.layoutId(), linearLayout);
            } else {
                linearLayout.removeAllViews();
                linearLayout.addView(inflater.inflate(injectLayout.layoutId(), container, false));
            }
        }
        return linearLayout;
    }
}



