package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;

import com.example.coderqiang.followme.Model.User;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.FormUrlEncoded;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2016/12/23.
 */

public class UserUtil {
    private static final String url="http://123.206.195.52:8080/day_30/";

    public static String getUserInfo(int type,String username) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        RequestBody body=new FormBody.Builder()
                .add("username",username)
                .add("type","6").build();
        Request request=new Request.Builder()
                .url("http://123.206.195.52:8080/day_30/getUserInfoServlet")
                .post(body)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String result = new String(response.body().bytes());
            Log.i("UserUtil",""+result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UserUtil","获取用户数据失败",e);
        }
        return "";
    }

    public static void getUser(String username){

        return ;
    }

    public static boolean signUp(Context context,String username, String password, String nickName){
        try {
            EMClient.getInstance().createAccount(username, password);
            linkToServer(username,password,nickName);
            User.get(context).setName(username);
            User.get(context).setPassword(password);
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean linkToServer(String username,String password,String nickName){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        RequestBody body=new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .add("nick",nickName)
                .add("signature",nickName+"很懒,什么都没写").build();
        Request request=new Request.Builder()
                .url("http://123.206.195.52:8080/day_30/signUpServlet")
                .post(body)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String result = new String(response.body().bytes());
            Log.i("UserUtil",""+result);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
