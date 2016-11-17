package com.abooc.plugin.browser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * Created by dayu on 14-10-23.
 */
public interface WebViewController {

    Activity getActivity();

    void onPageStarted(WebView webView, Bitmap favicon);

    void onPageFinished(WebView webView);

    void onProgressChanged(WebView webView, int newProgress);

}
