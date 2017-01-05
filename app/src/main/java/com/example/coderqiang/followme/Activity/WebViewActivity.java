package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpAnalyze;
import com.example.coderqiang.followme.View.NotificationWebview;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * Created by CoderQiang on 2016/11/1.
 */

public class WebViewActivity extends Activity {
    private static final String TAG = "WebViewActivity";
    public static final String TYPE="type";
    public static final int TYPE_LOCAL=1;
    public static final int TYPE_URL=0;
    public static final String EXTRA_COUNT = "count";
    public static final String WEB_URL="url";
    public static final String EXTRA_HTML = "html";
    @Bind(R.id.poidetail_webview)
    NotificationWebview webView;
    @Bind(R.id.webview_layout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.webview_toolbar)
    Toolbar toolbar;
    @Bind(R.id.webview_title)
    TextView title;
    @Bind(R.id.webview_home)
    ImageView homeImg;
    @Bind(R.id.webview_like)
    ImageView likeImg;
    @Bind(R.id.webview_share)
    ImageView shareImg;
    @Bind(R.id.webview_back)
    ImageView backIcon;
    @Bind(R.id.webview_progress)
    ProgressBar progressBar;
    @Bind(R.id.webview_progress_num)
    TextView progressNum;

    private boolean isShowAnim=true;
    private boolean firstLoad=true;
    ImageView imageView;
    private int type = 0;
    private int count;
    private int canLoad=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_poidetail);
        ButterKnife.bind(this);
        initWebView();
        Intent intent = this.getIntent();
        type = getIntent().getIntExtra(TYPE, 0);
        count = getIntent().getIntExtra(EXTRA_COUNT, 10);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        final String url;
        if (type==TYPE_URL){
             url= intent.getStringExtra(WEB_URL);
            Log.i(TAG, url);
            webView.loadUrl(url);
        } else if(type == TYPE_LOCAL){
            String result = getIntent().getStringExtra(EXTRA_HTML);
            webView.loadDataWithBaseURL(null,result, "text/html", "utf-8",null);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            showToolBar();
            canLoad--;
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.back_slide_exit,R.anim.back_slide_enter);
        }
    }

    private void initWebView() {
        progressBar.setVisibility(View.GONE);
        progressNum.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setDrawingCacheEnabled(true);
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
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                showProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        homeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Destroy");
        webView=null;
        toolbar=null;
        title=null;
        homeImg=null;
        likeImg=null;
        shareImg=null;
        setContentView(R.layout.view_null);
        coordinatorLayout.removeAllViews();
        super.onDestroy();
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "url:" + url);
            if (url.equals("http://m.ctrip.com/html5/you/operations/app.html?app=1&autoawaken=close&popup=close")){
                Log.i(TAG,"跳转到写游记页面");
                return true;
            }

            if(canLoad<count){
                canLoad++;
                Log.i(TAG,"CanLoad"+ canLoad);
            }else return true;
            view.loadUrl(url);
            firstLoad=true;
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (webView != null && webView.getTitle() != null) {
                String titleStr=webView.getTitle();
                titleStr = titleStr.replace("携程", "");
                title.setText(titleStr);
            }

            super.onPageFinished(view, url);
//            if(isShowAnim&&firstLoad){
//                startAnimation(webView);
//                isShowAnim=false;
//            }
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e(TAG, "加载网页失败" + errorCode);
            super.onReceivedError(view, errorCode, description, failingUrl);

        }


    }

    private void showProgress(int progress){
        if(20<progress&&progress<100){
            progressBar.setVisibility(View.VISIBLE);
            progressNum.setVisibility(View.VISIBLE);
        }else if(progress==100||progress<30){
            progressBar.setVisibility(View.GONE);
            progressNum.setVisibility(View.GONE);
        }

        progressBar.setProgress(progress);
        progressNum.setText(progress+"%");

    }

    private void hideToolBar() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showToolBar() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }
}
