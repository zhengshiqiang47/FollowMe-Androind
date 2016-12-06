package com.example.coderqiang.followme.Model;

import android.content.Context;

/**
 * Created by CoderQiang on 2016/12/4.
 */

public class MyLocation {
    private static MyLocation myLocation;
    private Double latitute;
    private Double longtitute;
    private String cityName;
    private boolean hasLocation=false;

    private MyLocation() {

    }

    public static MyLocation getMyLocation(Context context) {
        if (myLocation == null) {
            myLocation=new MyLocation();
        }
        return myLocation;
    }

    public Double getLatitute() {
        return latitute;
    }

    public void setLatitute(Double latitute) {
        this.latitute = latitute;
    }

    public Double getLongtitute() {
        return longtitute;
    }

    public void setLongtitute(Double longtitute) {
        this.longtitute = longtitute;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public boolean isHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(boolean hasLocation) {
        this.hasLocation = hasLocation;
    }
}
