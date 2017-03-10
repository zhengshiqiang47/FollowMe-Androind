package com.example.coderqiang.followme.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class Scenicspot implements Serializable{
    private int id;
    private String ScenicspotID="";
    private String scenicName="";
    private String rank="";
    private String addr="";
    private String manyA="";
    private String type="";
    private String url;
    private String mark = "";
    private String commentCount = "";
    private String firstImage="";
    private ArrayList<ScenicImg> imgUrls = new ArrayList<ScenicImg>();
    private String cityName="";
    private ArrayList<Comment> comments=new ArrayList<Comment>();
    private String phoneNumber="";
    private String introduction="";
    private String traffic="";
    private String ScenicWeb="";
    private String brightPoint="";
    private String tips="";
    private String ticket="";
    private String openTime="";
    private String countTime="";
    private String strategy="";
    private String shotIntro="";
    private boolean isParse=false;
    private String res = "";
    private String districtName = "";
    private String districtId = "";
    private String poiId = "";
    private int pageNum=2;
    private String distance;
    public static int ID=0;

    public String getScenicspotID() {
        return ScenicspotID;
    }

    public void setScenicspotID(String scenicspotID) {
        ScenicspotID = scenicspotID;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        Scenicspot.ID = ID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void pageNumPlus() {
        pageNum++;
    }

    public String getShotIntro() {
        return shotIntro;
    }

    public void setShotIntro(String shotIntro) {
        this.shotIntro = shotIntro;
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

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
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

    public String getBrightPoint() {
        return brightPoint;
    }

    public void setBrightPoint(String brightPoint) {
        this.brightPoint = brightPoint;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ScenicImg> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(ArrayList<ScenicImg> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getCountTime() {
        return countTime;
    }

    public void setCountTime(String countTime) {
        this.countTime = countTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
