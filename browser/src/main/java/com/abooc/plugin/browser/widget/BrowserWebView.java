package com.abooc.plugin.browser.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.abooc.plugin.browser.utils.AppFunction;

/**
 * Created by dayu on 14-10-22.
 */
public class BrowserWebView extends WebView {
    public BrowserWebView(Context context) {
        this(context, null);
    }

    public BrowserWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrowserWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        buildWebView();
    }


    private void buildWebView() {
        setHorizontalScrollBarEnabled(false);//水平不显示
        setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = getSettings();
        webSettings.setDomStorageEnabled(true);//见@1
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadsImagesAutomatically(true);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AppFunction.hideInputMethod(getContext(), this);
            requestFocus();
            return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * @1:
     *
     * 这里牵扯WebView比较怪异的一个问题，Android 4.4 以后的 WebView 是基于 chromium 内核的，
     * HTML5的Storage有两种：
     * 1.sessionStorage: 会话 (session) 级别的数据存储，会话结束后，相关的数据就会被清除掉。
     * 2.localStorage: 用于持久化的本地存储，除非主动删除数据，否则数据是永远不会过期的。
     * 作为 HTML5 标准的一部分，绝大多数的浏览器都是支持 localStorage 的，
     * 但是鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数
     * 据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
     * 这样就会引发出一个问题：
     * WebView加载某些js代码时，WebView会找不到js中的localStorage属性，例如localStorage.index ，
     * 这时WebView报错：
     * TypeError: Cannot read property 'index' of null"
     * 所以，WebView要设置webSettings.setDomStorageEnabled(true);
     */
}
