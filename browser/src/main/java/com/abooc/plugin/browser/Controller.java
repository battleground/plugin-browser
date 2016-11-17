package com.abooc.plugin.browser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;

import com.abooc.plugin.browser.utils.UrlUtils;


/**
 * Created by dayu on 14-10-23.
 */
public class Controller implements UI, UiController, WebViewController {

    private Activity mActivity;
    private PhoneUI mUi;
    private WebView mWebView;


    public interface OnLoadUrlListener {
        public void onPageStarted(WebView view, String url, Bitmap favicon);

        public void onPageFinished(WebView webView, String url);

        public void onProgressChanged(WebView webView, int newProgress);
    }

    private OnLoadUrlListener mOnLoadUrlListener;

    public void setOnLoadUrlListener(OnLoadUrlListener listener) {
        this.mOnLoadUrlListener = listener;
    }

    public OnLoadUrlListener getOnLoadUrlListener() {
        return mOnLoadUrlListener;
    }

    public Controller(Activity browser) {
        mActivity = browser;
    }

    void setUi(PhoneUI ui) {
        mUi = ui;
    }

    public PhoneUI getUi() {
        return mUi;
    }

    @Override
    public void setWebView(WebView webView) {
        mWebView = webView;
    }

    @Override
    public WebView getCurrentWebView() {
        return mUi.getCurrentWebView();
    }

    @Override
    public void loadUrl(String url) {
        String fixUrl = UrlUtils.smartUrlFilter(url);
        mUi.getCurrentWebView().loadUrl(fixUrl);
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public void onPageStarted(WebView webView, Bitmap favicon) {
        mActivity.setProgressBarVisibility(true);
    }

    @Override
    public void onPageFinished(WebView webView) {
        mActivity.setProgressBarVisibility(false);
        mActivity.invalidateOptionsMenu();
    }


    @Override
    public void onProgressChanged(final WebView webView, final int newProgress) {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mActivity.setProgress(newProgress * 100);
                if (mOnLoadUrlListener != null)
                    mOnLoadUrlListener.onProgressChanged(webView, newProgress);
            }
        });

    }

}
