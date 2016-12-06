package com.example.coderqiang.followme.Model;

import android.content.Context;

import com.baidu.platform.comapi.map.C;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/12/3.
 */

public class City {
    private String provinceName;
    private String cityName;
    private String ctripId;
    private boolean isParse=false;
    private ArrayList<Scenicspot> scenicspots = new ArrayList<>();

    public String getCtripId() {
        return ctripId;
    }

    public void setCtripId(String ctripId) {
        this.ctripId = ctripId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public boolean isParse() {
        return isParse;
    }

    public void setParse(boolean parse) {
        isParse = parse;
    }

    public ArrayList<Scenicspot> getScenicspots() {
        return scenicspots;
    }

    public void setScenicspots(ArrayList<Scenicspot> scenicspots) {
        this.scenicspots = scenicspots;
    }
}
