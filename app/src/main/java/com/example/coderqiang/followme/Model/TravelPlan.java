package com.example.coderqiang.followme.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderQiang on 2016/12/27.
 */

public class TravelPlan {
    private int id;
    private List<TravelDay> travelDays =new ArrayList<TravelDay>();
    private int userId;
    private String travleName;
    private City city;
    private String cityName;
    private Long time;
    private int dayCount=0;
    private String beginMemo;
    private String travleDaysStr;
    private Double latitute;
    private Double longtitute;

    public List<TravelDay> getTravelDays() {
        return travelDays;
    }

    public void setTravelDays(ArrayList<TravelDay> travelDays) {
        this.travelDays = travelDays;
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

    public TravelDay getTravleDay(int i){
        if(travelDays.size()>i)
            return travelDays.get(i);
        else return null;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTravleDaysStr() {
        return travleDaysStr;
    }

    public void setTravleDaysStr(String travleDaysStr) {
        this.travleDaysStr = travleDaysStr;
    }
}
