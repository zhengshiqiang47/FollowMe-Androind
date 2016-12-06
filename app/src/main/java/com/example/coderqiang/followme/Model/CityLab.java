package com.example.coderqiang.followme.Model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/12/3.
 */

public class CityLab {
    private static CityLab cityLab;
    private Context context;
    private ArrayList<City> cities;

    private CityLab(Context context) {
        cities=new ArrayList<City>();
        this.context=context;
    }

    public static CityLab get(Context context) {
        if (cityLab == null) {
            cityLab = new CityLab(context);
        }
        return cityLab;
    }

    public ArrayList<City> getCities() {
        return cities;
    }
}
