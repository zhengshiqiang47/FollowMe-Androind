package com.example.coderqiang.followme.Model;

import java.io.Serializable;

/**
 * Created by CoderQiang on 2016/11/29.
 */

public class ScenicImg implements Serializable{

    /**
     * id : 17007299
     * smallImgUrl : http://dimg09.c-ctrip.com/images/fd/tg/g1/M05/7B/AD/CghzfFWwuq-AA5pdAALKb19P8Vo085_C_70_70_Mtg_7.jpg
     * bigImgUrl : http://dimg09.c-ctrip.com/images/fd/tg/g1/M05/7B/AD/CghzfFWwuq-AA5pdAALKb19P8Vo085_C_1600_1200_Mtg_7.jpg
     * width : 1081
     * height : 717
     * originalimage : null
     * userInfo : {"name":"尊敬的会员","picUrl":"http://images4.c-ctrip.com/target/t1/headphoto/295/361/724/faaedf607954497eafa6249a1743c550_C_50_50.jpg","level":0,"travelNum":0,"homeUrl":"/members/3F2951AB44E947DF8B4A6567FDFE5A44","travelHomeUrl":"/members/3F2951AB44E947DF8B4A6567FDFE5A44/journals"}
     * imgInfo : {"title":"故宫3","time":"2014-09-24","travelName":null,"travelUrl":null,"poi":"故宫","poiScore":"4.7","commentNum":36440,"poiUrl":"/sight/beijing1/229.html","destination":"北京","destinationUrl":"/place/beijing1.html","address":"北京市东城区景山前街4号","commentUrl":"/sight/beijing1/229.html#comment"}
     */

    private int id;
    private String smallImgUrl;
    private String bigImgUrl;
    private int width;
    private int height;
    private UserInfoBean userInfo;
    private ImgInfoBean imgInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }

    public String getBigImgUrl() {
        return bigImgUrl;
    }

    public void setBigImgUrl(String bigImgUrl) {
        this.bigImgUrl = bigImgUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public ImgInfoBean getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfoBean imgInfo) {
        this.imgInfo = imgInfo;
    }

    public static class UserInfoBean implements Serializable{
        /**
         * name : 尊敬的会员
         * picUrl : http://images4.c-ctrip.com/target/t1/headphoto/295/361/724/faaedf607954497eafa6249a1743c550_C_50_50.jpg
         * level : 0
         * travelNum : 0
         * homeUrl : /members/3F2951AB44E947DF8B4A6567FDFE5A44
         * travelHomeUrl : /members/3F2951AB44E947DF8B4A6567FDFE5A44/journals
         */

        private String name;
        private String picUrl;
        private int level;
        private int travelNum;
        private String homeUrl;
        private String travelHomeUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getTravelNum() {
            return travelNum;
        }

        public void setTravelNum(int travelNum) {
            this.travelNum = travelNum;
        }

        public String getHomeUrl() {
            return homeUrl;
        }

        public void setHomeUrl(String homeUrl) {
            this.homeUrl = homeUrl;
        }

        public String getTravelHomeUrl() {
            return travelHomeUrl;
        }

        public void setTravelHomeUrl(String travelHomeUrl) {
            this.travelHomeUrl = travelHomeUrl;
        }
    }

    public static class ImgInfoBean implements Serializable{
        /**
         * title : 故宫3
         * time : 2014-09-24
         * travelName : null
         * travelUrl : null
         * poi : 故宫
         * poiScore : 4.7
         * commentNum : 36440
         * poiUrl : /sight/beijing1/229.html
         * destination : 北京
         * destinationUrl : /place/beijing1.html
         * address : 北京市东城区景山前街4号
         * commentUrl : /sight/beijing1/229.html#comment
         */

        private String title;
        private String time;
        private String poi;
        private String poiScore;
        private int commentNum;
        private String poiUrl;
        private String destination;
        private String destinationUrl;
        private String address;
        private String commentUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPoi() {
            return poi;
        }

        public void setPoi(String poi) {
            this.poi = poi;
        }

        public String getPoiScore() {
            return poiScore;
        }

        public void setPoiScore(String poiScore) {
            this.poiScore = poiScore;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getPoiUrl() {
            return poiUrl;
        }

        public void setPoiUrl(String poiUrl) {
            this.poiUrl = poiUrl;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getDestinationUrl() {
            return destinationUrl;
        }

        public void setDestinationUrl(String destinationUrl) {
            this.destinationUrl = destinationUrl;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCommentUrl() {
            return commentUrl;
        }

        public void setCommentUrl(String commentUrl) {
            this.commentUrl = commentUrl;
        }
    }
}
