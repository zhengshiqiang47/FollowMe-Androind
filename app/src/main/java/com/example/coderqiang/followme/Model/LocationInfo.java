package com.example.coderqiang.followme.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderQiang on 2017/2/18.
 */

public class LocationInfo implements Serializable {

    private double latitude;
    private double longtitude;
    private int imgId;
    private String name;
    private String distance;
    private int love;

    public static List<LocationInfo> infos=new ArrayList<>();

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }
}
