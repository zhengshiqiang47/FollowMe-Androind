package com.example.coderqiang.followme.Model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/12/29.
 */

public class TravlePlanLab {

    private static TravlePlanLab travelPlanLab;
    private Context mContext;
    private TravlePlan currentPlan;
    private ArrayList<TravlePlan> travelPlans;

    private TravlePlanLab(Context context){
        travelPlans=new ArrayList<TravlePlan>();
        this.mContext=context;
    }

    public static TravlePlanLab get(Context context){
        if (travelPlanLab == null) {
            travelPlanLab = new TravlePlanLab(context);
        }
        return travelPlanLab;
    }

    public void setTravelPlans(ArrayList<TravlePlan> travelPlans) {
        this.travelPlans = travelPlans;
    }

    public ArrayList<TravlePlan> getTravelPlans(){
        return travelPlans;
    }

    public TravlePlan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(TravlePlan currentPlan) {
        this.currentPlan = currentPlan;
    }
}
