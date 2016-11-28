package com.example.coderqiang.followme.Model;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class Comment {
    private String commentId;
    private String ownerImag;
    private String CommentName;
    private String ScenicSpot;
    private String Content;
    private String time;
    private ArrayList<String> images;

    public String getCommentName() {
        return CommentName;
    }

    public void setCommentName(String commentName) {
        CommentName = commentName;
    }

    public String getScenicSpot() {
        return ScenicSpot;
    }

    public void setScenicSpot(String scenicSpot) {
        ScenicSpot = scenicSpot;
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
}
