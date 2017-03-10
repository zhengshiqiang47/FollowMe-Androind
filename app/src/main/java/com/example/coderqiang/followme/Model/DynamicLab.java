package com.example.coderqiang.followme.Model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/5.
 */

public class DynamicLab {
    private static DynamicLab dynamicLab;
    private Context mContext;
    private ArrayList<Dynamic> dynamics;
    private ArrayList<Dynamic> myDynamics;

    private DynamicLab(Context context){
        dynamics=new ArrayList<Dynamic>();
        myDynamics = new ArrayList<Dynamic>();
        this.mContext=context;
    }

    public static DynamicLab get(Context context){
        if (dynamicLab == null) {
            dynamicLab = new DynamicLab(context);
        }
        return dynamicLab;
    }

    public ArrayList<Dynamic> getDynamics(){
        return dynamics;
    }

    public ArrayList<Dynamic> getMyDynamics() {
        return myDynamics;
    }

    public void setMyDynamics(ArrayList<Dynamic> myDynamics) {
        this.myDynamics = myDynamics;
    }
}
