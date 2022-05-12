package com.example.coderqiang.followme.Interface;

import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.ResultDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public interface UserInterface {

    @GET("api/user/login")
    Call<ResultDTO<FMUser>> login(@Query("username") String username,
                                  @Query("password") String password);

    @GET("api/user/get")
    Call<FMUser> getUser(@Query("userid")int userId);

}
