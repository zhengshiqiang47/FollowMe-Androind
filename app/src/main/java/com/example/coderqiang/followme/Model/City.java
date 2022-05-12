package com.example.coderqiang.followme.Model;

import android.content.Context;
import android.util.Log;

import com.baidu.platform.comapi.map.C;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by CoderQiang on 2016/12/3.
 */

public class City implements Serializable{
    private int id;
    private boolean isGlobal;
    private String provinceName;
    private String cityName;
    private String ctripId;
    private String country;
    private boolean isParse=false;
    private int scenicPage=1;
    private Weather weather;
    private String imageUrl;
    private List<String> iamgeUrls=new ArrayList<String>();
    private List<Scenicspot> scenicspots = new ArrayList<>();
    private int countPage=1;

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

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

    public void addscenicPage() {
        scenicPage++;
        Log.i("City", "Page" + scenicPage);
        if(scenicPage>countPage) countPage=scenicPage;
    }

    public void setScenicPage(int scenicPage){
        this.scenicPage=scenicPage;
    }

    public List<Scenicspot> getScenicspots() {
        return scenicspots;
    }

    public int getScenicPage() {
        return scenicPage;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public List<String> getIamgeUrls() {
        return iamgeUrls;
    }

    public void setIamgeUrls(List<String> iamgeUrls) {
        this.iamgeUrls = iamgeUrls;
    }

    public void setScenicspots(List<Scenicspot> scenicspots) {
        this.scenicspots = scenicspots;
    }

    public boolean deleteScenicspot(Scenicspot scenicspot){
        if(scenicspots.contains(scenicspot))
            scenicspots.remove(scenicspot);
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
