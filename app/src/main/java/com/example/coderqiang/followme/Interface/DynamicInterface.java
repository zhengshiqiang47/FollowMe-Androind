package com.example.coderqiang.followme.Interface;

import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.Paging;
import com.example.coderqiang.followme.Model.ResultDTO;
import com.example.coderqiang.followme.Model.Weather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public interface DynamicInterface {

    @GET("api/dynamic/query")
    Call<ResultDTO<Paging<Dynamic>>> queryDynamic(@Query("userid") int userId,
                                                 @Query("page") int page,
                                                  @Query("size") int size);

    @POST("api/dynamic/create")
    Call<ResultDTO<Dynamic>> createDynamic(@Body Dynamic dynamic);
}
