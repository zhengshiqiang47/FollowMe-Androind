package com.example.coderqiang.followme.Util;

import com.example.coderqiang.followme.Model.Comment;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2017/3/6.
 */

public class MyStringUtil {

    public static ArrayList<String> CommentImageParse(String images){
        ArrayList<String> result = new ArrayList<>();
        if (images!=null){
            images=images.replaceAll("\\|","");
            String[] imageList=images.split("https://");
            for (int i = 1; i < imageList.length; i++) {
                result.add("https://"+imageList[i]);
            }
        }
        return result;
    }
}
