package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;

import com.example.coderqiang.followme.Interface.GetDynamicInterface;
import com.example.coderqiang.followme.Interface.ImgJsonIterface;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.DynamicLab;
import com.example.coderqiang.followme.Model.ScenicImg;
import com.example.coderqiang.followme.Model.User;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public class ServerUtil {
    private static final String TAG="ServerUtil";
    private static final String TYPE_GET="1";


    public static void getDynamic(Context context,User user){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://123.206.195.52:8080/day_30/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetDynamicInterface getDynamicInterface=retrofit.create(GetDynamicInterface.class);
        Call<ArrayList<Dynamic>> call=getDynamicInterface.getDyanmic(TYPE_GET,user.getId()+"");
        try {
            ArrayList<Dynamic> dynamics=call.execute().body();
            Log.i(TAG,"获取动态"+dynamics.size());
            DynamicLab.get(context.getApplicationContext()).getDynamics().addAll(dynamics);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "获取失败", e);
        }
    }
}
