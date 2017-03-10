package com.example.coderqiang.followme.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class Comment implements Serializable{
    private int id;
    private int scenicId;
    private String commentId;
    private String ownerImg;
    private String commentName="";
    private String content="";
    private String time="";
    private String sightMark="";
    private String useful = "";
    private String imgSmall;
    private String images;
    private ArrayList<String> imagelist=new ArrayList<String>();
    private ArrayList<String> imgSmalslist=new ArrayList<String>();



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScenicId() {
        return scenicId;
    }

    public void setScenicId(int scenicId) {
        this.scenicId = scenicId;
    }

    public ArrayList<String> getImagelist() {
        return imagelist;
    }

    public void setImagelist(ArrayList<String> imagelist) {
        this.imagelist = imagelist;
    }

    public ArrayList<String> getImgSmalslist() {
        return imgSmalslist;
    }

    public void setImgSmalslist(ArrayList<String> imgSmalslist) {
        this.imgSmalslist = imgSmalslist;
    }


    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getOwnerImg() {
        return ownerImg;
    }

    public void setOwnerImg(String ownerImg) {
        this.ownerImg = ownerImg;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }
}
