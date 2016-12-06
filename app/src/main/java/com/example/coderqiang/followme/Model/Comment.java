package com.example.coderqiang.followme.Model;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class Comment {
    private String commentId;
    private String ownerImag="";
    private String CommentName="";
    private Scenicspot ScenicSpot;
    private String Content="";
    private String time="";
    private String sightMark="";
    private String useful = "";
    private ArrayList<String> images=new ArrayList<String>();
    private ArrayList<String> imgSmals=new ArrayList<String>();

    public String getCommentName() {
        return CommentName;
    }

    public void setCommentName(String commentName) {
        CommentName = commentName;
    }


    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getOwnerImag() {
        return ownerImag;
    }

    public void setOwnerImag(String ownerImag) {
        this.ownerImag = ownerImag;
    }

    public String getSightMark() {
        return sightMark;
    }

    public void setSightMark(String sightMark) {
        this.sightMark = sightMark;
    }

    public String getUseful() {
        return useful;
    }

    public void setUseful(String useful) {
        this.useful = useful;
    }

    public ArrayList<String> getImgSmals() {
        return imgSmals;
    }

    public void setImgSmals(ArrayList<String> imgSmals) {
        this.imgSmals = imgSmals;
    }

    public Scenicspot getScenicSpot() {
        return ScenicSpot;
    }

    public void setScenicSpot(Scenicspot scenicSpot) {
        ScenicSpot = scenicSpot;
    }
}
