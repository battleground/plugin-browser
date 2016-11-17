package com.abooc.plugin.browser;


import android.webkit.WebView;

/**
 * Created by dayu on 14-10-23.
 */
public interface UI {
    WebView getCurrentWebView();

    void loadUrl(String url);
}
