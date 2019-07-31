package com.lz.fram.base;


import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


public abstract class BaseUiPresenter extends RxPresenter {


    private View convertView;


    public void creatLayout() {
        //初始化convertView
        Context context = (Context) mBaseView;
        try {
            if (getLayout() != -1) {
                LayoutInflater inflater = LayoutInflater.from(context);

                convertView = inflater.inflate(getLayout(), null, false);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public View getHolderView() {
        if (convertView == null) {
            throw new NullPointerException("NullPointerException Please call createLayout first.");
        }
        return convertView;
    }

    protected abstract int getLayout();

    @Override
    public void onDestroy(LifecycleOwner owner) {
        super.onDestroy(owner);
        convertView = null;
    }
}