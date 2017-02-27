package com.example.coderqiang.followme.Model;

import android.net.Uri;

import com.baidu.location.BDLocation;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/4.
 */

public class Dynamic {
    private int userID;
    private int dynamicID;
    private String userName;
    private String Content;
    private long timeStamp;
    private ArrayList<Uri> imagURL;
    private String Address;
    private Double Longtitude;
    private Double Latitude;
    private String travelTime;
    private ArrayList<DynamicComment> comments;
    private int glance;
    private String memo="";
    private int love;

    public Dynamic(String userName,int userID,String Content,String address,long timeStamp,ArrayList<Uri> imagURL) {
        this.userName = userName;
        this.userID=userID;
        this.userName=userName;
        this.Content=Content;
        this.imagURL=imagURL;
        love=0;
        glance=0;
        this.timeStamp=timeStamp;
        this.Address=address;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDynamicID() {
        return dynamicID;
    }

    public void setDynamicID(int dynamicID) {
        this.dynamicID = dynamicID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }



    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public void addLove() {
        this.love++;
    }

    public void delLove() {
        this.love--;
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Double getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(Double longtitude) {
        Longtitude = longtitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public int getGlance() {
        return glance;
    }

    public void setGlance(int glance) {
        this.glance = glance;
    }

    public ArrayList<DynamicComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<DynamicComment> comments) {
        this.comments = comments;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<Uri> getImagURL() {
        return imagURL;
    }

    public void setImagURL(ArrayList<Uri> imagURL) {
        this.imagURL = imagURL;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
