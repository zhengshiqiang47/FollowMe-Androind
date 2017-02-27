package com.example.coderqiang.followme.Model;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by CoderQiang on 2016/12/23.
 */

public class User {

    private static User user;
    private Context context;

    private int id;
    private String name;
    private String password;
    private Bitmap touxiang;


    private User(Context context){
        this.context=context;
    }

    public static User get(Context context){
        if (user == null) {
            user=new User(context);
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(Bitmap touxiang) {
        this.touxiang = touxiang;
    }


}
