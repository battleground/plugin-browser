package com.abooc.plugin.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.abooc.widget.Toast;

/**
 * 浏览器
 *
 * @author liruiyu
 * @email allnet@live.cn
 * @date 2014-9-12
 */
public class BrowserActivity extends Activity implements OnAttachBrowser {

    protected Controller mController;

    public static void launch(Context context, Uri uri) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.init(this);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);

        FrameLayout browser = createBrowser();
        onBrowserCreated(browser);
        Uri uri = getIntent().getData();
        if (uri != null) {
            mController.getUi().getNavigationBarPhone().setText(uri.toString());
            mController.loadUrl(uri.toString());
        }
    }

    @Override
    public void onBrowserCreated(FrameLayout browser) {
        setContentView(browser);
    }

    private FrameLayout createBrowser() {
        FrameLayout browserLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_browser, null);
        mController = new Controller(this);
        PhoneUI ui = new PhoneUI(this, browserLayout, mController);
        mController.setUi(ui);
        return browserLayout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        WebView webView = mController.getCurrentWebView();
        menu.findItem(R.id.GoBack).setEnabled(webView.canGoBack());
        menu.findItem(R.id.GoForward).setEnabled(webView.canGoForward());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        WebView webView = mController.getCurrentWebView();
        int i = item.getItemId();
        if (i == R.id.GoBack) {
            webView.goBack();
            return super.onOptionsItemSelected(item);
        } else if (i == R.id.GoForward) {
            webView.goForward();
            return super.onOptionsItemSelected(item);
        } else if (i == R.id.Home) {
            String url = item.getTitleCondensed().toString();
            mController.loadUrl(url);
            return super.onOptionsItemSelected(item);
        } else if (i == R.id.Refresh) {
            mController.getCurrentWebView().reload();
            return super.onOptionsItemSelected(item);
        } else if (i == R.id.Inmi) {
            String url = item.getTitleCondensed().toString();
            mController.loadUrl(url);
            return super.onOptionsItemSelected(item);
        } else if (i == R.id.OpenOther) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(mController.getCurrentWebView().getUrl()));
            startActivity(intent);
            return true;
        } else if (i == R.id.Google) {
            String url = item.getTitleCondensed().toString();
            mController.loadUrl(url);
            return super.onOptionsItemSelected(item);
        } else if (i == R.id.ClearCache) {
            mController.getCurrentWebView().clearCache(true);
            return true;
        } else if (i == R.id.Settings) {
            Toast.show("建设中...");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onExit(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        WebView webView = mController.getCurrentWebView();
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    public static String fixScreen(String imgUrl) {
        return newHtml.replace("%image-url%", imgUrl);
    }

    private static final String script =
            "<script type=\"text/javascript\">"//
                    + "window.onload = function (){"//
                    // + "alert(1);"
                    + "var clientHeight=document.documentElement.clientHeight;"//
                    + "var imgHeight=document.getElementById('image_show').height;"//
                    + "if(clientHeight > imgHeight){"//
                    + "	var margin = (clientHeight - imgHeight) / 2;"//
                    + "	document.getElementById('container').style.height = clientHeight+\"px\";"//
                    + "	document.getElementById('image_show').style['margin-top'] = margin+\"px\";"//
                    + "}"//
                    + "};"//
                    + "</script>"; //

    private static final String newHtml =
            "<!DOCTYPE html>"//
                    + "<html lang=\"zh-cn\">"//
                    + "<head>"//
                    + "<title>image</title>"//
                    + "<meta charset=\"utf-8\" />"//
                    + "<style type=\"text/css\">"//
                    + "body{margin:0;padding:0;}"//
                    + "#container{width:100%;height:100%;overflow:scroll-y;vertical-align:middle;margin:0px;padding:0px;}"//
                    + "#container img{display:block;max-width:100%;min-width:100%;margin:0px;padding:0px;}"//
                    + "</style>"//
                    + "</head>"//
                    + "<body>"//
                    + "<div id=\"container\">"//
                    + "<img id=\"image_show\" src=\"%image-url%\" />"//
                    + script//
                    + "</div>"//
                    + "</body>"//
                    + "</html>";

}
