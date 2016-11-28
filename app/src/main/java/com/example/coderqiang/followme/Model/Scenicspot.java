package com.example.coderqiang.followme.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class Scenicspot implements Serializable{
    private int ScenicspotID;
    private String ScenicName;
    private String rank;
    private String addr;
    private String manyA;
    private String firstImg;
    private ArrayList<String> imgUrls;
    private String cityName;
    private ArrayList<Comment> comments;
    private String phoneNumber;
    private String introduction;
    private String traffic;
    private String ScenicWeb;
    private String ticket;
    private String openTime;
    private String CountTime;
    private String strategy;
    private String shotIntro;
    private boolean isParse;
    public static int ID=0;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public String getShotIntro() {
        return shotIntro;
    }

    public void setShotIntro(String shotIntro) {
        this.shotIntro = shotIntro;
    }

    public int getScenicspotID() {
        return ScenicspotID;
    }

    public void setScenicspotID(int scenicspotID) {
        ScenicspotID = scenicspotID;
    }

    public String getScenicName() {
        return ScenicName;
    }

    public void setScenicName(String scenicName) {
        ScenicName = scenicName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getScenicWeb() {
        return ScenicWeb;
    }

    public void setScenicWeb(String scenicWeb) {
        ScenicWeb = scenicWeb;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCountTime() {
        return CountTime;
    }

    public void setCountTime(String countTime) {
        CountTime = countTime;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
    }


    public boolean isParse() {
        return isParse;
    }

    public void setParse(boolean parse) {
        isParse = parse;
    }
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getManyA() {
        return manyA;
    }

    public void setManyA(String manyA) {
        this.manyA = manyA;
    }
}
