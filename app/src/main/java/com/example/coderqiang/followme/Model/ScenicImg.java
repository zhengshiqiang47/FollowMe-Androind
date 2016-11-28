package com.example.coderqiang.followme.Model;

/**
 * Created by CoderQiang on 2016/11/29.
 */

public class ScenicImg {

    /**
     * id : 63443468
     * smallImgUrl : http://dimg05.c-ctrip.com/images/100d070000002ie1061C1_C_70_70_Mtg_7.jpg
     * bigImgUrl : http://dimg05.c-ctrip.com/images/100d070000002ie1061C1_R_1600_10000_Mtg_7.jpg
     * width : 6597
     * height : 4403
     * originalimage : null
     * userInfo : {"name":"doris圈圈","picUrl":"http://images4.c-ctrip.com/target/t1/headphoto/763/486/854/834a5882f8834ffcba96c4d6bb18c458_C_50_50.jpg","level":0,"travelNum":0,"homeUrl":"/members/18A257D4E4424781B611E6EA44FFBEF6","travelHomeUrl":"/members/18A257D4E4424781B611E6EA44FFBEF6/journals"}
     * imgInfo : {"title":"_DSC3894","time":"2015-12-30","travelName":null,"travelUrl":null,"poi":"灵隐寺","poiScore":"4.5","commentNum":5489,"poiUrl":"/sight/hangzhou14/2040.html","destination":"杭州","destinationUrl":"/place/hangzhou14.html","address":"杭州市西湖区灵隐路法云弄1号","commentUrl":"/sight/hangzhou14/2040.html#comment"}
     */

    private int id;
    private String smallImgUrl;
    private String bigImgUrl;
    private int width;
    private int height;
    private Object originalimage;
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

    public Object getOriginalimage() {
        return originalimage;
    }

    public void setOriginalimage(Object originalimage) {
        this.originalimage = originalimage;
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

    public static class UserInfoBean {
        /**
         * name : doris圈圈
         * picUrl : http://images4.c-ctrip.com/target/t1/headphoto/763/486/854/834a5882f8834ffcba96c4d6bb18c458_C_50_50.jpg
         * level : 0
         * travelNum : 0
         * homeUrl : /members/18A257D4E4424781B611E6EA44FFBEF6
         * travelHomeUrl : /members/18A257D4E4424781B611E6EA44FFBEF6/journals
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

    public static class ImgInfoBean {
        /**
         * title : _DSC3894
         * time : 2015-12-30
         * travelName : null
         * travelUrl : null
         * poi : 灵隐寺
         * poiScore : 4.5
         * commentNum : 5489
         * poiUrl : /sight/hangzhou14/2040.html
         * destination : 杭州
         * destinationUrl : /place/hangzhou14.html
         * address : 杭州市西湖区灵隐路法云弄1号
         * commentUrl : /sight/hangzhou14/2040.html#comment
         */

        private String title;
        private String time;
        private Object travelName;
        private Object travelUrl;
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

        public Object getTravelName() {
            return travelName;
        }

        public void setTravelName(Object travelName) {
            this.travelName = travelName;
        }

        public Object getTravelUrl() {
            return travelUrl;
        }

        public void setTravelUrl(Object travelUrl) {
            this.travelUrl = travelUrl;
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
