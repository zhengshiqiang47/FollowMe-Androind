package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;

import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.TravlePlanLab;
import com.example.coderqiang.followme.Model.TravelDay;
import com.example.coderqiang.followme.Model.TravelPlan;
import com.example.coderqiang.followme.Model.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2016/12/29.
 */

public class TravelPlanUtil {
    private static  final  String TAG="TravelPlanUtil";

    public static TravelPlan getTravelPlan(Context context){
        final TravelPlan travelPlan =new TravelPlan();
        travelPlan.setTime(Long.parseLong(""+System.currentTimeMillis()));
        travelPlan.setBeginMemo("这是整个行程过程的备忘录，让你不忘这次旅游的最终初衷");
        travelPlan.setCity(CityLab.get(context.getApplicationContext()).isContain("福州"));
        travelPlan.setCityName("福州");
        travelPlan.setDayCount(7);
        travelPlan.setUserId(User.get(context.getApplicationContext()).getId());
        travelPlan.setTravleName("福州美妙之旅");
        ArrayList<TravelDay> travelDays =new ArrayList<TravelDay>();
        for (int i=0;i<10;i++){
            TravelDay travelDay =new TravelDay();
            travelDay.setMemo("");
            ArrayList<Scenicspot> scenicspots= new ArrayList<Scenicspot>();
            scenicspots.add(CityLab.get(context.getApplicationContext()).getCurrentCity().getScenicspots().get(i));
            travelDay.setScenicspots(scenicspots);
            travelDay.setDayNum(i+1);
            travelDays.add(travelDay);
        }
        final Context finalContext=context.getApplicationContext();
        travelPlan.setTravelDays(travelDays);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                List<TravelPlan> travelPlans =ServerUtil.getTravelPlan(finalContext,User.get(finalContext).getId());
                TravlePlanLab.get(finalContext.getApplicationContext()).setTravelPlans(travelPlans);
                for(TravelPlan plan: travelPlans){
                    if(travelPlan.getUserId()==plan.getUserId()&& travelPlan.getTravleName().equals(plan.getTravleName())){
                        Log.i(TAG,"服务器上已有");
                        return;
                    }
                }
                int id= ServerUtil.uploadTravelPlan(finalContext, travelPlan);
                Log.i(TAG,"id"+id+ travelPlan.getTravelDays().toString()+" ");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
        TravlePlanLab.get(context.getApplicationContext()).getTravelPlans().add(travelPlan);
        TravlePlanLab.get(context.getApplicationContext()).setCurrentPlan(travelPlan);
        return travelPlan;
    }
}
