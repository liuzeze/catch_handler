package com.lz.fram.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * @author : liuze
 * @e-mail : 835052259@qq.com
 * @date : 2019/9/23-12:43
 * @desc : 修改内容
 * @version: 1.0
 */
public class LoadDialog {


    private Dialog mDialog;

    public LoadDialog(Context context) {
        if (context == null) {
            return;
        }
        initView(context);
    }

    private void initView(Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        window.setAttributes(mLayoutParams);
        if (mLayoutParams != null) {
            mLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = Gravity.CENTER;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ProgressBar progressBar = new ProgressBar(context);
        LinearLayout.LayoutParams rootLp = new LinearLayout.LayoutParams(dp2px(70), dp2px(70));
        mDialog.setContentView(progressBar, rootLp);


    }

    private static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int showCount = 0;

    public synchronized void show() {
        if (mDialog != null) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
            showCount = showCount + 1;
        }
    }

    public synchronized void dismiss() {
        if (mDialog != null) {
            showCount = showCount - 1;
            if (showCount <= 0) {
                showCount = 0;
                mDialog.dismiss();

            }
        }
    }
}