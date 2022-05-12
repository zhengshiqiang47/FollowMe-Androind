package com.example.coderqiang.followme.Interface;

import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.ResultDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public interface CityInterface {

    @GET("api/city/all")
    Call<ResultDTO<List<City>>> allCity();

}
