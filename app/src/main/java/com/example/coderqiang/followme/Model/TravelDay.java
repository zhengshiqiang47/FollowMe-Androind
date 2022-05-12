package com.example.coderqiang.followme.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by CoderQiang on 2016/12/27.
 */

public class TravelDay {
    private ArrayList<Scenicspot> scenicspots=new ArrayList<Scenicspot>();

    private HistoryTrackData historyTrackData;
    private Weather weather;
    private String memo;
    private Long time;
    private int dayNum;
    private String scenicspotsStr;


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public int getScenicCount(){

        return scenicspots.size();
    }


    public ArrayList<Scenicspot> getScenicspots() {
        return scenicspots;
    }

    public void setScenicspots(ArrayList<Scenicspot> scenicspots) {
        this.scenicspots = scenicspots;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public String addScenicSpots(Scenicspot scenicspot){
        if(!scenicspots.contains(scenicspot)){
            scenicspots.add(scenicspot);
            return "添加成功";
        }else {
            return "已添加过";
        }
    }

    public String deleteScenicSpots(Scenicspot scenicspot){
        if(scenicspots.contains(scenicspot)){
            scenicspots.remove(scenicspot);
            return "删除成功";
        }else {
            return "删除失败";
        }
    }

    public HistoryTrackData getHistoryTrackData() {
        return historyTrackData;
    }

    public void setHistoryTrackData(HistoryTrackData historyTrackData) {
        this.historyTrackData = historyTrackData;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getScenicspotsStr() {
        return scenicspotsStr;
    }

    public void setScenicspotsStr(String scenicspotsStr) {
        this.scenicspotsStr = scenicspotsStr;
    }
}
