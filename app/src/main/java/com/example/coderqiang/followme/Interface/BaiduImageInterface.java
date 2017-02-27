package com.example.coderqiang.followme.Interface;

import com.example.coderqiang.followme.Model.BaiduMapImage;
import com.example.coderqiang.followme.Model.Weather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**http://map.baidu.com/detail?qt=ugcphotolist&type=scope&orderBy=1&from=mappc&uid=a9173a180c1324641df74dd3&photoType=photo_exterior,phone_other&pageIndex=1&pageCount=20
 * Created by CoderQiang on 2017/2/18.
 */

public interface BaiduImageInterface {
    @GET("detail")
    Call<BaiduMapImage> getImage(@Query("qt") String qt,
                                            @Query("type") String type,
                                            @Query("orderBy") String orderBy,
                                            @Query("from") String from,
                                            @Query("uid") String uid,
                                            @Query("photoType")String photoType,
                                            @Query("pageIndex")String pageIndex,
                                            @Query("pageCount") String pageCount);
}
