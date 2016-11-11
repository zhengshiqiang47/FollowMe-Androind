package com.example.coderqiang.followme.Util;

import android.util.Log;

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
}
