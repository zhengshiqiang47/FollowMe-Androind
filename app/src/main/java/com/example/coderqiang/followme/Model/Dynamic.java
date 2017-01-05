package com.example.coderqiang.followme.Model;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/4.
 */

public class Dynamic {
    private String userID;
    private String userName;
    private String dynamicID;
    private String Content;
    private String Date;
    private ArrayList<String> imagURL;
    private String Address;
    private int love;

    public Dynamic(String userName,String userID,String DynamicID,String Content,String Date,ArrayList<String> imagURL) {
        this.userName = userName;
        this.userID=userID;
        this.userName=userName;
        this.dynamicID=DynamicID;
        this.Content=Content;
        this.imagURL=imagURL;
        love=0;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDynamicID() {
        return dynamicID;
    }

    public void setDynamicID(String dynamicID) {
        this.dynamicID = dynamicID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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

    public ArrayList<String> getImagURL() {
        return imagURL;
    }

    public void setImagURL(ArrayList<String> imagURL) {
        this.imagURL = imagURL;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

}
