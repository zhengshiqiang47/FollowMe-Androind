package com.example.coderqiang.followme.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by CoderQiang on 2016/12/27.
 */

public class TravleDay {
    private ArrayList<Scenicspot> scenicspots=new ArrayList<Scenicspot>();

    private String memo;
    private Date date;
    private int dayNum;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getDate() {
        return date;
    }

    public int getScenicCount(){

        return scenicspots.size();
    }

    public void setDate(Date date) {
        this.date = date;
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
}
