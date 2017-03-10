package com.example.coderqiang.followme.Interface;

import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.FMUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public interface GetUserInterface {
    @GET("userInfoServlet")
    Call<FMUser> getUser(@Query("type") String type,
                                    @Query("id") String userId);
}
