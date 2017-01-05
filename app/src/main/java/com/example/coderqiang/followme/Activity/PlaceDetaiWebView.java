package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.coderqiang.followme.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/1/5.
 */

public class PlaceDetaiWebView extends Activity {
    @Bind(R.id.palce_webview)
    WebView webView;
    @Bind(R.id.palce_web_back)
    ImageView backIcon;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palcewebview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
        url=getIntent().getStringExtra("url");
        initView();
        Log.i("PlaceWebview","Url"+url);
        webView.loadUrl(url);
    }

    private void initView(){
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("Place", "url:" + url);
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_slide_exit,R.anim.back_slide_enter);
    }
}
