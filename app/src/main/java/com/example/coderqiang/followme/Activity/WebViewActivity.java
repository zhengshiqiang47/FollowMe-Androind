package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpAnalyze;
import com.example.coderqiang.followme.View.NotificationWebview;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/1.
 */

public class WebViewActivity extends Activity {
    private static final String TAG = "WebViewActivity";
    public static final String TYPE="type";
    public static final int TYPE_LOCAL=1;
    public static final int TYPE_URL=0;
    public static final String WEB_URL="url";
    public static final String EXTRA_HTML = "html";
    @Bind(R.id.poidetail_webview)
    NotificationWebview webView;
    @Bind(R.id.webview_toolbar)
    Toolbar toolbar;
    @Bind(R.id.webview_title)
    TextView title;
    private int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_poidetail);
        ButterKnife.bind(this);
        initWebView();
        Intent intent = this.getIntent();
        type = getIntent().getIntExtra(TYPE, 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        final String url;
        if (type==TYPE_URL){
             url= intent.getStringExtra(WEB_URL);
            Log.i(TAG, url);
            webView.loadUrl(url);
        } else if(type == TYPE_LOCAL){
            String result = getIntent().getStringExtra(EXTRA_HTML);
            webView.loadDataWithBaseURL(null,result, "text/html", "utf-8",null);
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final String result=HttpAnalyze.getHtml(url);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        }).start();

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else super.onBackPressed();
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setOnScrollChangedCallback(new NotificationWebview.OnScrollChangedCallback() {
            private static final int HIDE_THRESHOLD=150;
            private static final int SHOW_THRESHOLD=20;
            private int scrollDistance=0;
            private boolean controlsVisible=true;
            @Override
            public void onScroll(int dx, int dy) {
                if(scrollDistance>HIDE_THRESHOLD&&controlsVisible&&dy>15){
                    hideToolBar();
                    controlsVisible=false;
                    scrollDistance=0;
                } else if (scrollDistance < -SHOW_THRESHOLD&& !controlsVisible) {
                    showToolBar();
                    controlsVisible=true;
                    scrollDistance=0;
                }
                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrollDistance+=dy;
                }
            }
        });
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "url:" + url);
            if (url.equals("http://m.ctrip.com/html5/you/operations/app.html?app=1&autoawaken=close&popup=close")){
                Log.i(TAG,"跳转到写游记页面");
                return false;
            }
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            String titleStr=webView.getTitle();
            titleStr = titleStr.replace("携程", "");
            title.setText(titleStr);
            super.onPageFinished(view, url);

            // html加载完成之后，添加监听图片的点击js函数
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }
    private void hideToolBar() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showToolBar() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }
}
