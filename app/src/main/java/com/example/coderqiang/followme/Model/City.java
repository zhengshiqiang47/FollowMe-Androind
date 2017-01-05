package com.example.coderqiang.followme.Model;

import android.content.Context;
import android.util.Log;

import com.baidu.platform.comapi.map.C;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by CoderQiang on 2016/12/3.
 */

public class City implements Serializable{
    private String provinceName;
    private String cityName;
    private String ctripId;
    private boolean isParse=false;
    private int scenicPage=1;
    private Weather weather;
    private ArrayList<String> iamgeUrls=new ArrayList<String>();
    private ArrayList<Scenicspot> scenicspots = new ArrayList<>();

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    private int countPage=1;


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

    public ArrayList<Scenicspot> getScenicspots() {
        return scenicspots;
    }

    public void setScenicspots(ArrayList<Scenicspot> scenicspots) {
        this.scenicspots = scenicspots;
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

    public ArrayList<String> getIamgeUrls() {
        return iamgeUrls;
    }

    public void setIamgeUrls(ArrayList<String> iamgeUrls) {
        this.iamgeUrls = iamgeUrls;
    }

    public boolean deleteScenicspot(Scenicspot scenicspot){
        if(scenicspots.contains(scenicspot))
            scenicspots.remove(scenicspot);
        return true;
    }
}
