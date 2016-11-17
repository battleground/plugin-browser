package com.abooc.plugin.browser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.abooc.plugin.browser.utils.AppFunction;
import com.abooc.plugin.browser.widget.NavigationBarPhone;


/**
 * Created by dayu on 14-10-23.
 */
public class PhoneUI implements UI, View.OnTouchListener {

    protected Drawable mGenericFavicon;
    Activity mActivity;
    Controller mController;
    private WebView mWebView;
    private NavigationBarPhone mNavigationBarPhone;

    public PhoneUI(Activity browser, View browserView, Controller ui) {
        mActivity = browser;
        mController = ui;

        ActionBar mActionBar = browser.getActionBar();
        mActionBar.setCustomView(R.layout.title_bar_nav);
        mActionBar.setDisplayShowCustomEnabled(true);
        mNavigationBarPhone = (NavigationBarPhone) mActionBar.getCustomView();
        mNavigationBarPhone.setController(ui);
        mNavigationBarPhone.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        Resources res = mActivity.getResources();
        mGenericFavicon = res.getDrawable(
                R.drawable.app_web_browser_sm);
        mWebView = (WebView) browserView.findViewById(R.id.WebView);
        mWebView.setWebChromeClient(iWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setOnTouchListener(this);
        mController.setWebView(mWebView);
    }

    public NavigationBarPhone getNavigationBarPhone() {
        return mNavigationBarPhone;
    }

    public static interface OnOverrideUrlLoadingListener {
        public void onOverrideUrlLoading(WebView view, String url);
    }

    private OnOverrideUrlLoadingListener mOnOverrideUrlLoadingListener;

    public void setOnOverrideUrlLoadingListener(OnOverrideUrlLoadingListener listener) {
        mOnOverrideUrlLoadingListener = listener;
    }

    private WebChromeClient iWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, final int newProgress) {
            mController.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            mNavigationBarPhone.setTitle(title);
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (mOnOverrideUrlLoadingListener != null) {
                mOnOverrideUrlLoadingListener.onOverrideUrlLoading(view, url);
                return true;
            }
//            mController.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            webView.requestFocus();
            Controller.OnLoadUrlListener listener = mController.getOnLoadUrlListener();
            if (listener != null) listener.onPageStarted(webView, url, favicon);
            mNavigationBarPhone.setText(url);
            mNavigationBarPhone.onProgressStarted();
            mNavigationBarPhone.setFaviconImage(favicon);
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            webView.requestFocus();
            Controller.OnLoadUrlListener listener = mController.getOnLoadUrlListener();
            if (listener != null) listener.onPageFinished(webView, url);
            mNavigationBarPhone.setText(url);
            mNavigationBarPhone.onProgressStopped();
            mController.onPageFinished(mWebView);
        }

        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
//            webView.loadUrl("file:///android_asset/htmls/404.html");
//            mNavigationBarPhone.setText(failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };

    static class History {
        private static History instance = new History();
        private Uri uri = Uri.parse("content://org.lee.android.providers/history");

        private History() {
        }

        public void cache(Activity activity) {
            ContentResolver resolver = activity.getContentResolver();
//            resolver.query(uri, )
        }

    }

    @Override
    public WebView getCurrentWebView() {
        return mWebView;
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AppFunction.hideInputMethod(mActivity, v);
            v.requestFocus();
        }
        return false;
    }
}
