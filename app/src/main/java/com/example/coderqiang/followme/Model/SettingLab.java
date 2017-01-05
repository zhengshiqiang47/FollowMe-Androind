package com.example.coderqiang.followme.Model;

import android.content.Context;

/**
 * Created by CoderQiang on 2017/1/5.
 */

public class SettingLab {
    private static SettingLab settingLab;
    private Context context;
    private boolean touxiangUpdate=false;
    private boolean travelDayUpdate=false;

    private SettingLab(Context context){
        this.context=context;
    }

    public static SettingLab getSettingLab(Context context){
        if(settingLab==null){
            settingLab=new SettingLab(context);
        }
        return settingLab;
    }

    public boolean isTouxiangUpdate() {
        return touxiangUpdate;
    }

    public void setTouxiangUpdate(boolean touxiangUpdate) {
        this.touxiangUpdate = touxiangUpdate;
    }

    public boolean isTravelDayUpdate() {
        return travelDayUpdate;
    }

    public void setTravelDayUpdate(boolean travelDayUpdate) {
        this.travelDayUpdate = travelDayUpdate;
    }
}
