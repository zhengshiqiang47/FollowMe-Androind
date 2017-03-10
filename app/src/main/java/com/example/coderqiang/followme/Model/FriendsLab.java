package com.example.coderqiang.followme.Model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2017/3/6.
 */

public class FriendsLab {
    private static FriendsLab FriendsLab;
    private Context mContext;

    public void setFMUsers(ArrayList<FMUser> FMUsers) {
        this.FMUsers = FMUsers;
    }

    private ArrayList<FMUser> FMUsers;

    private FriendsLab(Context context){
        FMUsers=new ArrayList<FMUser>();
        this.mContext=context;
    }

    public static FriendsLab get(Context context){
        if (FriendsLab == null) {
            FriendsLab = new FriendsLab(context);
        }
        return FriendsLab;
    }

    public ArrayList<FMUser> getFMUsers(){
        return FMUsers;
    }
}
