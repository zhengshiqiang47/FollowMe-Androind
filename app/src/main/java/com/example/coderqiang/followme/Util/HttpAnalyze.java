package com.example.coderqiang.followme.Util;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CoderQiang on 2016/11/1.
 */

public class HttpAnalyze {
    private  final String TAG = "HttpAnalyze";
    public String getHtml(String url){
        String result=null;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            result = new String(response.body().bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getAndroidHtml(String url){
        String result=null;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("User-Agent","Mozilla/5.0 (Linux; U; Android 5.1.1; zh-tw; GT-I9300 Build/JZO54K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            result = new String(response.body().bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getAjaxJson(String url,String referer){
        String result=null;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            result=response.body().string();
//            result = new String(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getTravelsNotes(String url){
        String result = new String();
        result = getAndroidHtml("http://you.ctrip.com/travels/fuzhou164/2639504.html");
        Log.i(TAG, result);
        result.replace("<div class=\"tranny detail_translated bottom_box bottom-nav\">","");
        return result;
    }
}
