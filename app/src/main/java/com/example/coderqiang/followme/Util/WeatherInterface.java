package com.example.coderqiang.followme.Util;

import com.example.coderqiang.followme.Model.ScenicImg;
import com.example.coderqiang.followme.Model.Weather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CoderQiang on 2016/12/30.
 */

public interface WeatherInterface {
    @GET("query")
    Call<Weather> getWeather(@Query("cityname") String cityname,
                                          @Query("key") String key,
                                          @Query("dtype") String type);
}
