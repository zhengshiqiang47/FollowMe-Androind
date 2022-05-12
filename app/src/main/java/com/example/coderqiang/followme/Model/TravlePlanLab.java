package com.example.coderqiang.followme.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderQiang on 2016/12/29.
 */

public class TravlePlanLab {

    private static TravlePlanLab travelPlanLab;
    private Context mContext;
    private TravelPlan currentPlan;
    private List<TravelPlan> travelPlans;

    private TravlePlanLab(Context context){
        travelPlans=new ArrayList<TravelPlan>();
        this.mContext=context;
    }

    public static TravlePlanLab get(Context context){
        if (travelPlanLab == null) {
            travelPlanLab = new TravlePlanLab(context);
        }
        return travelPlanLab;
    }

    public void setTravelPlans(List<TravelPlan> travelPlans) {
        this.travelPlans = travelPlans;
    }

    public List<TravelPlan> getTravelPlans(){
        return travelPlans;
    }

    public TravelPlan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(TravelPlan currentPlan) {
        this.currentPlan = currentPlan;
    }
}
