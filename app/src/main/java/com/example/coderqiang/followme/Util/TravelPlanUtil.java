package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;

import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.TravlePlanLab;
import com.example.coderqiang.followme.Model.TravleDay;
import com.example.coderqiang.followme.Model.TravlePlan;
import com.example.coderqiang.followme.Model.User;

import java.util.ArrayList;
import java.util.Date;

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

    public static TravlePlan getTravelPlan(Context context){
        final TravlePlan travlePlan=new TravlePlan();
        travlePlan.setTime(Long.parseLong(""+System.currentTimeMillis()));
        travlePlan.setBeginMemo("这是整个行程过程的备忘录，让你不忘这次旅游的最终初衷");
        travlePlan.setCity(CityLab.get(context.getApplicationContext()).isContain("福州"));
        travlePlan.setCityName("福州");
        travlePlan.setDayCount(7);
        travlePlan.setUserId(User.get(context.getApplicationContext()).getId());
        travlePlan.setTravleName("福州美妙之旅");
        ArrayList<TravleDay> travleDays=new ArrayList<TravleDay>();
        for (int i=0;i<10;i++){
            TravleDay travleDay=new TravleDay();
            travleDay.setMemo("");
            ArrayList<Scenicspot> scenicspots= new ArrayList<Scenicspot>();
            scenicspots.add(CityLab.get(context.getApplicationContext()).getCurrentCity().getScenicspots().get(i));
            travleDay.setScenicspots(scenicspots);
            travleDay.setDayNum(i+1);
            travleDays.add(travleDay);
        }
        final Context finalContext=context.getApplicationContext();
        travlePlan.setTravleDays(travleDays);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                ArrayList<TravlePlan> travlePlans=ServerUtil.getTravlePlan(finalContext,User.get(finalContext).getId());
                TravlePlanLab.get(finalContext.getApplicationContext()).setTravelPlans(travlePlans);
                for(TravlePlan plan:travlePlans){
                    if(travlePlan.getUserId()==plan.getUserId()&&travlePlan.getTravleName().equals(plan.getTravleName())){
                        Log.i(TAG,"服务器上已有");
                        return;
                    }
                }
                int id= ServerUtil.uploadTravelPlan(finalContext,travlePlan);
                Log.i(TAG,"id"+id+ travlePlan.getTravleDays().toString()+" ");
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
        TravlePlanLab.get(context.getApplicationContext()).getTravelPlans().add(travlePlan);
        TravlePlanLab.get(context.getApplicationContext()).setCurrentPlan(travlePlan);
        return travlePlan;
    }
}
