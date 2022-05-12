package com.example.coderqiang.followme.Interface;

import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.Paging;
import com.example.coderqiang.followme.Model.ResultDTO;
import com.example.coderqiang.followme.Model.TravelPlan;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public interface TravelPlanInterface {

    @GET("api/travelPlan/query")
    Call<ResultDTO<Paging<TravelPlan>>> queryTravelPlan(@Query("userid") int userId,
                                                     @Query("page") int page,
                                                     @Query("size") int size);

    @POST("api/travelPlan/create")
    Call<ResultDTO<TravelPlan>> createTravelPlan(@Body TravelPlan travelPlan);
}
