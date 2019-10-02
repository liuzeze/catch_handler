package com.lz.fram.view;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;


import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;

import com.lz.fram.R;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2018-11-12       重写toobar 方便管理
 */

public class TitleToolbar extends Toolbar implements View.OnClickListener {

    private LinearLayoutCompat mTitleLayout;
    private TextView mTitleTextView;
    private TextView mRightTextView;
    private CharSequence mTitleText;
    private boolean mTitleVisible;
    private boolean mTitleLayoutVisible;

    private TextView mSubtitleTextView;
    private CharSequence mSubTitleText;
    private boolean mSubTitleVisible;

    private TextView mCloseTextView;
    private CharSequence mCloseText;
    private CharSequence mRightText;
    private boolean mCloseVisible;
    private boolean mRightVisible;

    private TextView mBackTextView;
    private CharSequence mBackText;
    private boolean mBackVisible;

    private static final int DEFAULT_BACK_MARGIN_RIGHT = 8;
    private View mMyTitleLayout;

    protected OnOptionItemClickListener mOnOptionItemClickListener;

    public TitleToolbar(Context context) {
        this(context, null);
    }

    public TitleToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public TitleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView(context, attrs, defStyleAttr);
    }


    protected void initCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.Toolbar, defStyleAttr, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SaTitleToolbar);


        //右侧显示文字或者图标
        setRightView(context, typedArray);


        //标题layout

        int layoutResourceId = typedArray.getResourceId(R.styleable.SaTitleToolbar_titleLayout, 0);
        if (layoutResourceId == 0) {
            if (!isChild(mTitleLayout)) {
                mTitleLayout = new LinearLayoutCompat(context);
                mTitleLayout.setOrientation(LinearLayoutCompat.VERTICAL);
                mTitleLayout.setGravity(typedArray.getInt(
                        R.styleable.SaTitleToolbar_title_gravity, Gravity.CENTER_VERTICAL));

                LayoutParams params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
                addView(mTitleLayout, params);
            }

            //主标题
            setMasterTitle(context, a, typedArray);
            //副标题
            setSubTitle(context, a, typedArray);
        } else {
            if (!isChild(mMyTitleLayout)) {
                mMyTitleLayout = LayoutInflater.from(context).inflate(layoutResourceId, null);
                setTitleLayoutVisible(true);
                LayoutParams params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
                addView(mMyTitleLayout, params);
                mTitleVisible = false;
                mSubTitleVisible = false;
            }
        }
        //返回按钮
        setLeftBackView(context, typedArray);

        //左侧关闭文字或者图标
        setLeftCloseView(context, typedArray);

        typedArray.recycle();
        a.recycle();
    }


    private void setLeftCloseView(Context context, TypedArray typedArray) {
        if (!isChild(mCloseTextView)) {
            mCloseTextView = new TextView(context);
            mCloseTextView.setId(R.id.close);
            mCloseTextView.setSingleLine();
            mCloseTextView.setEllipsize(TextUtils.TruncateAt.END);
            mCloseTextView.setGravity(Gravity.CENTER_VERTICAL);

            int closeTextAppearance =
                    typedArray.getResourceId(R.styleable.SaTitleToolbar_closeTextAppearance, 0);

            if (closeTextAppearance != 0) {
                mCloseTextView.setTextAppearance(context, closeTextAppearance);
            }

            if (typedArray.hasValue(R.styleable.SaTitleToolbar_closeTextColor)) {
                int closeTextColor =
                        typedArray.getColor(R.styleable.SaTitleToolbar_closeTextColor, Color.WHITE);
                mCloseTextView.setTextColor(closeTextColor);
            }

            if (typedArray.hasValue(R.styleable.SaTitleToolbar_closeTextSize)) {
                mCloseTextView.setTextSize(
                        typedArray.getDimensionPixelSize(
                                R.styleable.SaTitleToolbar_closeTextSize, 0));
            }


            Drawable drawableClose = typedArray.getDrawable(R.styleable.SaTitleToolbar_closeIcon);
            if (drawableClose != null) {
                mCloseTextView.setCompoundDrawablesWithIntrinsicBounds(drawableClose, null, null, null);
            }

            setCloseText(typedArray.getText(R.styleable.SaTitleToolbar_closeText));
            setCloseVisible(typedArray.getBoolean(R.styleable.SaTitleToolbar_closeVisible, false));

            mCloseTextView.setClickable(true);
            mCloseTextView.setOnClickListener(this);

            addView(mCloseTextView, new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT, Gravity.LEFT | Gravity.CENTER_VERTICAL));
        }
    }

    private void setLeftBackView(Context context, TypedArray typedArray) {
        if (!isChild(mBackTextView)) {
            mBackTextView = new TextView(context);
            mBackTextView.setId(R.id.back);
            mBackTextView.setSingleLine();
            mBackTextView.setEllipsize(TextUtils.TruncateAt.END);
            mBackTextView.setGravity(Gravity.CENTER_VERTICAL);

            int backTextAppearance =
                    typedArray.getResourceId(R.styleable.SaTitleToolbar_backTextAppearance, 0);
            if (backTextAppearance != 0) {
                mBackTextView.setTextAppearance(context, backTextAppearance);
            }

            if (typedArray.hasValue(R.styleable.SaTitleToolbar_backTextColor)) {
                int backTextColor =
                        typedArray.getColor(R.styleable.SaTitleToolbar_backTextColor, Color.WHITE);
                mBackTextView.setTextColor(backTextColor);
            }

            if (typedArray.hasValue(R.styleable.SaTitleToolbar_backTextSize)) {
                mBackTextView.setTextSize(
                        typedArray.getDimensionPixelSize(R.styleable.SaTitleToolbar_backTextSize, 0));
            }

            Drawable drawable = typedArray.getDrawable(R.styleable.SaTitleToolbar_backIcon);
            if (drawable != null) {
                mBackTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }

            setBackText(typedArray.getText(R.styleable.SaTitleToolbar_backText));
            setBackVisible(typedArray.getBoolean(R.styleable.SaTitleToolbar_backVisible, false));

            mBackTextView.setClickable(true);
            mBackTextView.setOnClickListener(this);

            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT, Gravity.LEFT | Gravity.CENTER_VERTICAL);

            layoutParams.rightMargin = typedArray.getDimensionPixelSize(
                    R.styleable.SaTitleToolbar_backMarginRight, dp2px(DEFAULT_BACK_MARGIN_RIGHT));

            addView(mBackTextView, layoutParams);
        }
    }

    private void setSubTitle(Context context, TintTypedArray a, TypedArray typedArray) {
        if (!isChild(mSubtitleTextView, mTitleLayout)) {
            mSubtitleTextView = new TextView(context);
            mSubtitleTextView.setSingleLine();
            mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            mSubtitleTextView.setGravity(Gravity.CENTER);
            mSubtitleTextView.setEms(10);
            mSubtitleTextView.setGravity(typedArray.getInt(
                    R.styleable.SaTitleToolbar_title_gravity, Gravity.CENTER_VERTICAL));

            int subTextAppearance = a.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
            if (subTextAppearance != 0) {
                mSubtitleTextView.setTextAppearance(context, subTextAppearance);
            }

            if (a.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
                int subTitleColor = a.getColor(R.styleable.Toolbar_subtitleTextColor, Color.WHITE);
                mSubtitleTextView.setTextColor(subTitleColor);
            }

            if (typedArray.hasValue(R.styleable.SaTitleToolbar_subtitleTextSize)) {
                mSubtitleTextView.setTextSize(
                        typedArray.getDimensionPixelSize(
                                R.styleable.SaTitleToolbar_subtitleTextSize, 0));
            }

            setSubtitle(a.getText(R.styleable.Toolbar_subtitle));
            setSubtitleVisible(
                    typedArray.getBoolean(R.styleable.SaTitleToolbar_subtitleVisible, false));

            mTitleLayout.addView(mSubtitleTextView,
                    new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private void setMasterTitle(Context context, TintTypedArray a, TypedArray typedArray) {
        if (!isChild(mTitleTextView, mTitleLayout)) {
            mTitleTextView = new TextView(context);
            mTitleTextView.setSingleLine();
            mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            mTitleTextView.setGravity(Gravity.CENTER);
            mTitleTextView.setEms(10);
            mTitleTextView.setGravity(typedArray.getInt(
                    R.styleable.SaTitleToolbar_title_gravity, Gravity.CENTER_VERTICAL));

            int titleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
            if (titleTextAppearance != 0) {
                mTitleTextView.setTextAppearance(context, titleTextAppearance);
            }

            if (a.hasValue(R.styleable.Toolbar_titleTextColor)) {
                int titleColor = a.getColor(R.styleable.Toolbar_titleTextColor, Color.WHITE);
                mTitleTextView.setTextColor(titleColor);
            }

            if (typedArray.hasValue(R.styleable.SaTitleToolbar_titleTextSize)) {
                mTitleTextView.setTextSize(
                        typedArray.getDimensionPixelSize(R.styleable.SaTitleToolbar_backTextSize, 0));
            }

            setTitle(a.getText(R.styleable.Toolbar_title));
            setTitleVisible(typedArray.getBoolean(R.styleable.SaTitleToolbar_titleVisible, true));

            mTitleLayout.addView(mTitleTextView,
                    new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private void setRightView(Context context, TypedArray typedArray) {
        if (!isChild(mRightTextView)) {
            mRightTextView = new TextView(context);
            mRightTextView.setId(R.id.rightButton);
            mRightTextView.setSingleLine();
            mRightTextView.setEllipsize(TextUtils.TruncateAt.END);
            mRightTextView.setGravity(Gravity.CENTER_VERTICAL);

            int closeTextAppearance =
                    typedArray.getResourceId(R.styleable.SaTitleToolbar_rightTextAppearance, 0);

            if (closeTextAppearance != 0) {
                mRightTextView.setTextAppearance(context, closeTextAppearance);
            }

            if (typedArray.hasValue(R.styleable.SaTitleToolbar_rightTextColor)) {
                int closeTextColor =
                        typedArray.getColor(R.styleable.SaTitleToolbar_rightTextColor, Color.WHITE);
                mRightTextView.setTextColor(closeTextColor);
            }

            if (typedArray.hasValue(R.styleable.SaTitleToolbar_rightTextSize)) {
                mRightTextView.setTextSize(
                        typedArray.getDimensionPixelSize(
                                R.styleable.SaTitleToolbar_rightTextSize, 0));
            }


            Drawable rightIcon = typedArray.getDrawable(R.styleable.SaTitleToolbar_rightIcon);
            mRightTextView.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(
                    R.styleable.SaTitleToolbar_rightDrawPadding, dp2px(DEFAULT_BACK_MARGIN_RIGHT)));
            if (rightIcon != null) {
                mRightTextView.setCompoundDrawablesWithIntrinsicBounds(rightIcon, null, null, null);
            }

            setRightText(typedArray.getText(R.styleable.SaTitleToolbar_rightText));
            setRightVisible(typedArray.getBoolean(R.styleable.SaTitleToolbar_rightVisible, false));

            mRightTextView.setClickable(true);
            mRightTextView.setOnClickListener(this);

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT, Gravity.RIGHT);

            mRightTextView.setPadding(mRightTextView.getPaddingLeft(), mRightTextView.getPaddingTop(), typedArray.getDimensionPixelSize(
                    R.styleable.SaTitleToolbar_rightPaddingRight, dp2px(DEFAULT_BACK_MARGIN_RIGHT)), mRightTextView.getPaddingBottom());
            addView(mRightTextView, params);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleText = title;
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
    }

    @Override
    public CharSequence getTitle() {
        return mTitleText;
    }

    @Override
    public void setTitleTextAppearance(Context context, int resId) {
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    public void setTitleVisible(boolean visible) {
        mTitleVisible = visible;
        if (mTitleTextView != null) {
            mTitleTextView.setVisibility(mTitleVisible ? VISIBLE : GONE);
        }
    }

    public boolean getTitleVisible() {
        return mTitleVisible;
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        mSubTitleText = subtitle;
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setText(subtitle);
        }
    }

    @Override
    public CharSequence getSubtitle() {
        return mSubTitleText;
    }

    @Override
    public void setSubtitleTextAppearance(Context context, int resId) {
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setSubtitleTextColor(int color) {
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextColor(color);
        }
    }

    public TitleToolbar setSubtitleVisible(boolean visible) {
        mSubTitleVisible = visible;
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setVisibility(visible ? VISIBLE : GONE);
        }
        return this;
    }

    public boolean getSubtitleVisible() {
        return mSubTitleVisible;
    }

    public TitleToolbar setCloseText(int resId) {
        setCloseText(getContext().getText(resId));
        return this;
    }

    public TitleToolbar setCloseText(CharSequence closeText) {
        mCloseText = closeText;
        if (mCloseTextView != null) {
            mCloseTextView.setText(closeText);
        }
        return this;
    }

    public CharSequence getCloseText() {
        return mCloseText;
    }

    public TitleToolbar setCloseTextColor(int color) {
        if (mCloseTextView != null) {
            mCloseTextView.setTextColor(color);
        }
        return this;
    }

    public TitleToolbar setCloseVisible(boolean visible) {
        mCloseVisible = visible;
        if (mCloseTextView != null) {
            mCloseTextView.setVisibility(mCloseVisible ? VISIBLE : GONE);
        }
        return this;
    }

    public boolean isCloseVisible() {
        return mCloseVisible;
    }


    public TitleToolbar setRightText(int resId) {
        setRightText(getContext().getText(resId));
        return this;
    }

    public TitleToolbar setRightText(CharSequence rightText) {
        mRightText = rightText;
        if (mRightTextView != null) {
            mRightTextView.setText(rightText);
        }
        return this;
    }

    public CharSequence getRightText() {
        return mRightText;
    }

    public TitleToolbar setRightTextColor(int color) {
        if (mRightTextView != null) {
            mRightTextView.setTextColor(color);
        }
        return this;
    }

    public TitleToolbar setRightVisible(boolean visible) {
        mRightVisible = visible;
        if (mRightTextView != null) {
            mRightTextView.setVisibility(mRightVisible ? VISIBLE : GONE);
        }
        return this;
    }

    public boolean isRightVisible() {
        return mRightVisible;
    }


    public TitleToolbar setBackText(int resId) {
        setBackText(getContext().getText(resId));
        return this;
    }

    public TitleToolbar setBackText(CharSequence backText) {
        mBackText = backText;
        if (mBackTextView != null) {
            mBackTextView.setText(backText);
        }
        return this;
    }

    public CharSequence getBackText() {
        return mBackText;
    }

    public TitleToolbar setBackTextColor(int color) {
        if (mBackTextView != null) {
            mBackTextView.setTextColor(color);
        }
        return this;
    }

    public TitleToolbar setBackVisible(boolean visible) {
        mBackVisible = visible;
        if (mBackTextView != null) {
            mBackTextView.setVisibility(mBackVisible ? VISIBLE : GONE);
        }
        return this;
    }

    public boolean isBackVisible() {
        return mBackVisible;
    }

    @Override
    public void onClick(View v) {
        if (mOnOptionItemClickListener != null) {
            mOnOptionItemClickListener.onOptionItemClick(v);
        } else {
            if (v.getId() == R.id.back || v.getId() == R.id.close) {
                if (getContext() != null) {
                    ((Activity) getContext()).onBackPressed();
                }
            }
        }
    }

    /**
     * 设置返回按钮的图片
     *
     * @param drawable 资源文件
     */
    public TitleToolbar setBackIco(Drawable drawable) {
        if (drawable != null) {
            if (mBackTextView != null) {
                mBackTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        }
        return this;
    }

    /**
     * 设置返回按钮的图片
     *
     * @param drawable 资源文件
     */
    public TitleToolbar setRightIco(Drawable drawable) {
        if (drawable != null) {
            if (mRightTextView != null) {
                mRightTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        }
        return this;
    }

    public TitleToolbar setTitleLayoutVisible(boolean visible) {
        mTitleLayoutVisible = visible;
        if (mMyTitleLayout != null) {
            mMyTitleLayout.setVisibility(mTitleLayoutVisible ? VISIBLE : GONE);
        }
        return this;

    }

    public boolean getTitleLayoutVisible() {
        return mTitleLayoutVisible;
    }

    public TitleToolbar setTitleLayout(@LayoutRes int layoutId) {
        setTitleLayout(LayoutInflater.from(getContext()).inflate(layoutId, null));
        return this;
    }

    public TitleToolbar setTitleLayout(View view) {
        if (mMyTitleLayout != null) {
            removeView(mMyTitleLayout);
            mMyTitleLayout = null;
        }
        if (mTitleLayout != null) {
            removeView(mTitleLayout);
            mTitleLayout = null;
        }
        mMyTitleLayout = view;
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        addView(view, params);
        mTitleLayoutVisible = true;
        mTitleVisible = false;
        mSubTitleVisible = false;
        return this;

    }

    public boolean isChild(View view) {
        return view != null && view.getParent() == this;
    }

    public boolean isChild(View view, ViewParent parent) {
        return view != null && view.getParent() == parent;
    }

    public void setOnOptionItemClickListener(OnOptionItemClickListener listener) {
        mOnOptionItemClickListener = listener;
    }

    public interface OnOptionItemClickListener {
        void onOptionItemClick(View v);
    }

    public int dp2px(float dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

}