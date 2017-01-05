package com.example.coderqiang.followme.Util;

import android.content.Context;

import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.Model.TravelPlanLab;
import com.example.coderqiang.followme.Model.TravleDay;
import com.example.coderqiang.followme.Model.TravlePlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by CoderQiang on 2016/12/29.
 */

public class TravelPlanUtil {

    public static TravlePlan getTravelPlan(Context context){
        TravlePlan travlePlan=new TravlePlan();

        travlePlan.setBegin(new Date(System.currentTimeMillis()));
        travlePlan.setBeginMemo("这是整个行程过程的备忘录，让你不忘这次旅游的最终初衷");
        travlePlan.setCity(CityLab.get(context.getApplicationContext()).isContain("福州"));
        travlePlan.setDayCount(7);
        travlePlan.setTravleName("福州美妙之旅");
        ArrayList<TravleDay> travleDays=new ArrayList<TravleDay>();
        for (int i=0;i<10;i++){
            TravleDay travleDay=new TravleDay();
            travleDay.setMemo("");
            ArrayList<Scenicspot> scenicspots= new ArrayList<Scenicspot>();
            scenicspots.add(ScenicspotLab.get(context).getScenicspots().get(i));
            travleDay.setScenicspots(scenicspots);
            travleDay.setDayNum(i+1);
            travleDays.add(travleDay);
        }
        travlePlan.setTravleDays(travleDays);
        TravelPlanLab.get(context.getApplicationContext()).getTravelPlans().add(travlePlan);
        return travlePlan;
    }
}
