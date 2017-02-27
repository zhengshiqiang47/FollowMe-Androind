package com.example.coderqiang.followme.Util;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

/**
 * Created by CoderQiang on 2017/2/17.
 */

public class MapUtil {
    BaiduMap baiduMap;
    MapView mapView;

    public MapUtil(BaiduMap baiduMap,MapView mapView) {
        this.baiduMap=baiduMap;
        this.mapView=mapView;
    }

    public void setMap(){

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
