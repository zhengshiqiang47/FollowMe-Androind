package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpAnalyze;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/1.
 */

public class WebViewActivity extends Activity {
    private static final String TAG="WebViewActivity";
    public static final String WEB_URL="url";
    @Bind(R.id.poidetail_webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_poidetail);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        final String url = intent.getStringExtra(WEB_URL);
        Log.i(TAG, url);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.loadUrl(url);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final String result=HttpAnalyze.getHtml(url);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        webView.loadDataWithBaseURL(null,result, "text/html", "utf-8",null);
//                    }
//                });
//            }
//        }).start();

    }
}
