package com.lz.fram.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.lz.fram.R;
import com.lz.fram.view.SwipePanel;
import com.lz.fram.view.TitleToolbar;

import io.reactivex.annotations.Nullable;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2019-04-03       创建class
 */
public class InjectManager {

    /**
     * Activity注册
     *
     * @param target
     */
    public static void getLayoutId(Activity target) {
        Class<? extends Activity> aClass = target.getClass();
        initInjectLayout(target, aClass);
    }

    private static void initInjectLayout(Activity target, Class<? extends Activity> aClass) {
        InjectLayout injectLayout = aClass.getAnnotation(InjectLayout.class);
        if (injectLayout != null) {
            int value = injectLayout.layoutId();
            if (injectLayout.isShowActTitle()) {
                View root = target.getLayoutInflater().inflate(R.layout.layout_root, null);
                root.setId(R.id.sp_root);
                LinearLayout linearLayout = root.findViewById(R.id.container);
                TitleToolbar titleToolbar = root.findViewById(R.id.common_toolbar);
                titleToolbar.setTitle(injectLayout.titleName());
                titleToolbar.setBackVisible(injectLayout.actBackIcon());
                target.getLayoutInflater().inflate(value, linearLayout);
                target.setContentView(root);
            } else {
                final SwipePanel swipePanel = new SwipePanel(target);
                swipePanel.setId(R.id.sp_root);
                swipePanel.setLeftEdgeSize(100);
                swipePanel.setLeftDrawable(R.mipmap.title_back);
                swipePanel.wrapView(target.getLayoutInflater().inflate(value, null));
                target.setContentView(swipePanel);
            }
        }
    }

    public static View getView(Class tClass, LayoutInflater inflater, @Nullable ViewGroup container) {
        Class<?> aClass = tClass;
        InjectLayout layoutId = aClass.getAnnotation(InjectLayout.class);
        if (layoutId.isShowFragTitle()) {
            SwipePanel root = (SwipePanel) inflater.inflate(R.layout.layout_root, null);
            root.setId(R.id.sp_root);
            root.setLeftEnabled(false);
            LinearLayout linearLayout = root.findViewById(R.id.container);
            TitleToolbar titleToolbar = root.findViewById(R.id.common_toolbar);
            titleToolbar.setTitle(layoutId.titleName());
            titleToolbar.setBackVisible(layoutId.fragBackIcon());
            inflater.inflate(layoutId.layoutId(), linearLayout);
            return root;
        } else {
           View  inflate = inflater.inflate(layoutId.layoutId(), container, false);
            final SwipePanel swipePanel = new SwipePanel(inflater.getContext());
            swipePanel.setId(R.id.sp_root);
            swipePanel.setLeftEdgeSize(100);
            swipePanel.setLeftDrawable(R.mipmap.title_back);
            swipePanel.wrapView(inflate);
            return swipePanel;
        }
    }


}
