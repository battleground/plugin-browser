package com.abooc.plugin.browser.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abooc.plugin.browser.Controller;
import com.abooc.plugin.browser.R;
import com.abooc.plugin.browser.utils.AppFunction;

/**
 * Created by dayu on 14-10-23.
 */
public class NavigationBarPhone extends LinearLayout implements View.OnClickListener, TextView.OnEditorActionListener,
        TextWatcher, View.OnFocusChangeListener {

    private Controller mController;
    private AutoCompleteTextView mUrlText;
    private ImageView mBackButton;
    private ImageView mClearButton;
    private ImageView mGoButton;
    private Animation mRotation;
    private TextView mPlayText;
    private TextView mTitleText;

    public NavigationBarPhone(Context context) {
        super(context);
    }

    public NavigationBarPhone(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationBarPhone(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        mRotation = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
        mRotation.setRepeatCount(Animation.INFINITE);

        mBackButton = (ImageView) findViewById(R.id.Back);
        mClearButton = (ImageView) findViewById(R.id.clear);
        mGoButton = (ImageView) findViewById(R.id.Go);
        mBackButton.setOnClickListener(this);
        mClearButton.setOnClickListener(this);
        mGoButton.setOnClickListener(this);
        mUrlText = (AutoCompleteTextView) findViewById(R.id.url);
        mTitleText = (TextView) findViewById(R.id.Title);

        mPlayText = (TextView) findViewById(R.id.Playable);
        mUrlText.setOnEditorActionListener(this);
        mUrlText.addTextChangedListener(this);
        mUrlText.setOnFocusChangeListener(this);
    }

    public void setController(Controller controller) {
        mController = controller;
    }

    public void setOnBackListener(OnClickListener onBackListener) {
        mBackButton.setOnClickListener(onBackListener);
    }

    public void setFaviconImage(Bitmap icon) {
//        mFaviconImage.setImageDrawable(mController.getUi().getFaviconDrawable(icon));
    }

    public void setPlayable(boolean enable) {
        if (enable) {
            mPlayText.setVisibility(View.VISIBLE);
        } else {
            mPlayText.setVisibility(View.GONE);
        }

    }

    public String getInputText() {
        return mUrlText.getText().toString().trim();
    }

    public void setText(String text) {
        mUrlText.setText(text);
    }

    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    public void onProgressStarted() {
        mGoButton.setImageResource(R.drawable.ic_browser_refresh_nor);
        mGoButton.startAnimation(mRotation);
        mClearButton.setVisibility(View.GONE);
    }

    public void onProgressStopped() {
//        mGoButton.setImageResource(R.drawable.ic_menu_refresh);
        mGoButton.clearAnimation();

//        BrowserWebView browserWebView = (BrowserWebView) mController.getCurrentWebView();
//        browserWebView.setOnScrollChangedListener(new BrowserWebView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(int l, int t, int oldl, int oldt) {
//                setTranslationY(-t);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Go) {
            String url = mUrlText.getText().toString().trim();
            if (TextUtils.isEmpty(url)) return;
            mUrlText.clearFocus();
            AppFunction.hideInputMethod(getContext(), this);
            v.requestFocus();
            mController.loadUrl(url);
            return;
        } else if (v.getId() == R.id.clear) {
            mUrlText.setText(null);
            return;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        mGoButton.performClick();
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            mClearButton.setVisibility(View.VISIBLE);
        } else {
            mClearButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && mUrlText.getText().length() > 0) {
            mGoButton.setImageResource(R.drawable.ic_menu_forward);
            mClearButton.setVisibility(View.VISIBLE);
        } else {
            mGoButton.setImageResource(R.drawable.ic_browser_refresh_nor);
            if (mController != null) {
                mUrlText.setText(mController.getCurrentWebView().getUrl());
            }
            mClearButton.setVisibility(View.GONE);
        }
    }

}
