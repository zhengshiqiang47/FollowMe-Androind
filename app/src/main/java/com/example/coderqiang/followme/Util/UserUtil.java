package com.example.coderqiang.followme.Util;

import static com.example.coderqiang.followme.Util.ServerUtil.BASE_URL;

import android.content.Context;
import android.util.Log;

import com.example.coderqiang.followme.Interface.UserInterface;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.ResultDTO;
import com.example.coderqiang.followme.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoderQiang on 2016/12/23.
 */

public class UserUtil {
    private static final String TAG="UserUtil";

    public static String getUserInfo(int type,String username) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        RequestBody body=new FormBody.Builder()
                .add("username",username)
                .add("type","6").build();
        Request request=new Request.Builder()
                .url(BASE_URL + "getUserInfoServlet")
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

    public static boolean getUser(Context context,String username){
//        HttpAnalyze httpAnalyze=new HttpAnalyze();
        User user= User.get(context.getApplicationContext());
//        String resultId=httpAnalyze.getHtml(BASE_URL + "userInfoServlet?type=3&username="+username);
//        int id=0;
//        try {
//            id=Integer.parseInt(resultId);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        if(id!=0)
//            user.setId(id);
//        Log.i(TAG,"id:"+id);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInterface userInterface =retrofit.create(UserInterface.class);
        Call<FMUser> call= userInterface.getUser(user.getId());
        try {
            FMUser fMUser=call.execute().body();
            Log.i(TAG,"获取用户"+fMUser.getUserName());
            user.setFmUser(fMUser);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "获取失败", e);
        }
        return false;
    }

    public static boolean login(Context context,String username,String password){
        User user= User.get(context.getApplicationContext());
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInterface userInterface =retrofit.create(UserInterface.class);
        try {
            Call<ResultDTO<FMUser>> call= userInterface.login(username,password);
            ResultDTO<FMUser> resultDTO = call.execute().body();
            if (resultDTO.isSuccess()) {
                user.setFmUser(resultDTO.getData());
                Log.i(TAG, "login_success");
                return true;
            }else {
                return false;
            }
        } catch (Throwable e) {
            Log.e(TAG, "login_exp", e);
            return false;
        }
    }

    public static FMUser getOtherUser(Context context,int userid){
        HttpAnalyze httpAnalyze=new HttpAnalyze();
        FMUser user=new FMUser();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL )
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInterface userInterface =retrofit.create(UserInterface.class);
        Call<FMUser> call= userInterface.getUser(userid);
        try {
            user=call.execute().body();
            Log.i(TAG,"获取用户"+user.getUserName());
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "获取失败", e);
        }
        return null;
    }



    public static FMUser getFMUser(int id){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<FMUser> call= userInterface.getUser(id);
        try {
            FMUser fMUser=call.execute().body();
            Log.i(TAG,"获取用户"+fMUser.getUserName());
            return fMUser;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "获取失败", e);
        }
        return null;
    }

    public static boolean signUp(Context context,String username, String password, String nickName){
        try {
            EMClient.getInstance().createAccount(username, "111222333");
            linkToServer(username,password,nickName);
            User.get(context).setName(username);
            User.get(context).setPassword(password);
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean addFriend(int uid,int fid,String emuid,String emfid){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        FormBody.Builder form=new FormBody.Builder()
                .add("type", "4")
                .add("uid", uid+"")
                .add("fid", fid+"")
                .add("emuid", emuid)
                .add("emfid", emfid)
                .add("addtime", System.currentTimeMillis()+"");
        RequestBody requestBody=form.build();
        Request request=new Request.Builder()
                .url(BASE_URL+"userInfoServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String res=new String(response.body().bytes(),"utf-8");
            if(res.equals("ok"))
                return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUser(FMUser fmUser){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        FormBody.Builder form=new FormBody.Builder();
        RequestBody requestBody=form.add("type","2")
                .add("id",fmUser.getId()+"")
                .add("username",fmUser.getUserName())
                .add("nickname",fmUser.getNickName())
                .add("phone",fmUser.getPhone()+"")
                .add("email",fmUser.getEmail()+"")
                .add("password",fmUser.getPassword())
                .add("signature",fmUser.getSignature())
                .add("city",fmUser.getCity())
                .add("birthday",fmUser.getBirthDay()+"")
                .add("sex",fmUser.getSex()+"")
                .add("concern",fmUser.getConcern()+"")
                .add("travle",fmUser.getTravle()+"")
                .add("follower",fmUser.getFollower()+"").build();
        Request request=new Request.Builder()
                .url(BASE_URL+"userInfoServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String res=new String(response.body().bytes(),"utf-8");
            if(res.equals("ok"))
                return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<FMUser> getFriend(int uid){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        FormBody.Builder form=new FormBody.Builder()
                .add("type", "5")
                .add("uid", uid+"");
        RequestBody requestBody=form.build();
        Request request=new Request.Builder()
                .url(BASE_URL+"userInfoServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String res=new String(response.body().bytes(),"utf-8");
            ArrayList<FMUser> users=new Gson().fromJson(res,new TypeToken<ArrayList<FMUser>>() {}.getType());
            Log.i(TAG,"userSize:"+users.size());
            return users;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static boolean linkToServer(String username,String password,String nickName){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        RequestBody body=new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .add("nick",nickName)
                .add("signature",nickName+"很懒,什么都没写").build();
        Request request=new Request.Builder()
                .url(BASE_URL + "signUpServlet")
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
