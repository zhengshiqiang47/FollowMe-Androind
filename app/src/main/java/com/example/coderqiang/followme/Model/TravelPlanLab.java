package com.example.coderqiang.followme.Model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/12/29.
 */

public class TravelPlanLab {

    private static TravelPlanLab travelPlanLab;
    private Context mContext;
    private ArrayList<TravlePlan> travelPlans;

    private TravelPlanLab(Context context){
        travelPlans=new ArrayList<TravlePlan>();
        this.mContext=context;
    }

    public static TravelPlanLab get(Context context){
        if (travelPlanLab == null) {
            travelPlanLab = new TravelPlanLab(context);
        }
        return travelPlanLab;
    }

    public ArrayList<TravlePlan> getTravelPlans(){
        return travelPlans;
    }
}
