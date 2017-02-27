package com.example.coderqiang.followme.Model;

import android.content.Context;

import com.baidu.location.BDLocation;

/**
 * Created by CoderQiang on 2016/12/4.
 */

public class MyLocation {
    private static MyLocation myLocation;
    private Double latitute;
    private Double longtitute;
    private String cityName;
    private boolean hasLocation=false;
    private BDLocation bdLocation;

    private MyLocation() {

    }

    public static MyLocation getMyLocation(Context context) {
        if (myLocation == null) {
            myLocation=new MyLocation();
        }
        return myLocation;
    }

    public Double getLatitute() {
        return myLocation.latitute;
    }

    public void setLatitute(Double latitute) {
        myLocation.latitute = latitute;
    }

    public Double getLongtitute() {
        return myLocation.longtitute;
    }

    public void setLongtitute(Double longtitute) {
        myLocation.longtitute = longtitute;
    }

    public String getCityName() {
        return myLocation.cityName;
    }

    public void setCityName(String cityName) {
        myLocation.cityName = cityName;
    }


    public boolean isHasLocation() {
        return myLocation.hasLocation;
    }

    public void setHasLocation(boolean hasLocation) {
        myLocation.hasLocation = hasLocation;
    }

    public BDLocation getBdLocation() {
        return myLocation.bdLocation;
    }

    public void setBdLocation(BDLocation bdLocation) {
        myLocation.bdLocation = bdLocation;
    }
}
