package com.example.coderqiang.followme.Interface;

import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.Comment;
import com.example.coderqiang.followme.Model.Paging;
import com.example.coderqiang.followme.Model.ResultDTO;
import com.example.coderqiang.followme.Model.ScenicImg;
import com.example.coderqiang.followme.Model.Scenicspot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public interface ScenicInterface {

    @GET("api/scenic/query")
    Call<ResultDTO<Paging<Scenicspot>>> queryScenicSpot(@Query("cityname")String cityName,
                                                        @Query("page")Integer page,
                                                        @Query("size")Integer size);

    @GET("api/scenicComment/query")
    Call<ResultDTO<Paging<Comment>>> queryScenicComment(@Query("scenicid")int scenicid,
                                                        @Query("page")Integer page,
                                                        @Query("size")Integer size);

    @GET("api/scenicImg/query")
    Call<ResultDTO<Paging<ScenicImg>>> queryScenicImg(@Query("scenicid")int scenicid,
                                                      @Query("page")Integer page,
                                                      @Query("size")Integer size);

}
