package com.example.coderqiang.followme.Util;

import com.example.coderqiang.followme.Model.ScenicImg;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by CoderQiang on 2016/11/29.
 */

public interface ImgJsonIterface {
    //   districtId=14&type=2&pindex=1&phsid=63443790&resource=48020

    @GET("AjaxPhotoDetailList")
    Call<ArrayList<ScenicImg>> getImg(@Query("districtId") String distictId,
                                      @Query("type") String type,
                                      @Query("pindex") String pindex,
                                      @Query("phsid") String phsid,
                                      @Query("resource") String res);
}
