package com.example.coderqiang.followme.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by CoderQiang on 2016/12/27.
 */

public class TravlePlan {
    private ArrayList<TravleDay> travleDays=new ArrayList<TravleDay>();
    private City city;
    private Date begin;
    private String travleName;
    private int dayCount;
    private String beginMemo;
    private Double latitute;
    private Double longtitute;

    public ArrayList<TravleDay> getTravleDays() {
        return travleDays;
    }

    public void setTravleDays(ArrayList<TravleDay> travleDays) {
        this.travleDays = travleDays;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public String getTravleName() {
        return travleName;
    }

    public void setTravleName(String travleName) {
        this.travleName = travleName;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public String getBeginMemo() {
        return beginMemo;
    }

    public void setBeginMemo(String beginMemo) {
        this.beginMemo = beginMemo;
    }

    public TravleDay getTravleDay(int i){
        if(travleDays.size()>i)
            return travleDays.get(i);
        else return null;
    }
}
