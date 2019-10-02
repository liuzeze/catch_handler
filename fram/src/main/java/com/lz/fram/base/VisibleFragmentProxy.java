package com.lz.fram.base;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VisibleFragmentProxy {

    private Fragment mFragment;

    private VisibleFragmentOwner fragmentOwner;
    /**
     * Fragment的view是否已经初始化完成
     */
    private boolean mIsActivityCreated;
    /**
     * 懒加载，是否已经在view初始化完成之前调用
     */
    private boolean mIsLazyAfterView;
    /**
     * 懒加载，是否已经在view初始化完成之后调用
     */
    private boolean mIsLazyBeforeView;

    public VisibleFragmentProxy(Fragment fragment) {
        this.mFragment = fragment;
        if (fragment instanceof VisibleFragmentOwner) {
            this.fragmentOwner = (VisibleFragmentOwner) fragment;
        } else {
            throw new IllegalArgumentException("Fragment请实现ImmersionOwner接口");
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (mFragment != null) {
            if (mFragment.getUserVisibleHint()) {
                if (!mIsLazyBeforeView) {
                    fragmentOwner.onLazyBeforeView();
                    mIsLazyBeforeView = true;
                }
                if (mIsActivityCreated) {
                    if (mFragment.getUserVisibleHint()) {

                        if (!mIsLazyAfterView) {
                            fragmentOwner.onLazyAfterView();
                            mIsLazyAfterView = true;
                        }
                        fragmentOwner.onVisible();
                    }
                }
            } else {
                if (mIsActivityCreated) {
                    fragmentOwner.onInvisible();
                }
            }
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (mFragment != null && mFragment.getUserVisibleHint()) {
            if (!mIsLazyBeforeView) {
                fragmentOwner.onLazyBeforeView();
                mIsLazyBeforeView = true;
            }
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mIsActivityCreated = true;
        if (mFragment != null && mFragment.getUserVisibleHint()) {

            if (!mIsLazyAfterView) {
                fragmentOwner.onLazyAfterView();
                mIsLazyAfterView = true;
            }
        }
    }

    public void onResume() {
        if (mFragment != null && mFragment.getUserVisibleHint()) {
            fragmentOwner.onVisible();
        }
    }

    public void onPause() {
        if (mFragment != null) {
            fragmentOwner.onInvisible();
        }
    }

    public void onDestroy() {
        mFragment = null;
        fragmentOwner = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (mFragment != null && mFragment.getUserVisibleHint()) {
            fragmentOwner.onVisible();
        }
    }

    public void onHiddenChanged(boolean hidden) {
        if (mFragment != null) {
            mFragment.setUserVisibleHint(!hidden);
        }
    }
}
