package com.example.coderqiang.followme.Model;

import android.content.Context;
import android.util.Log;

import java.security.Key;
import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/12/3.
 */

public class CityLab {
    private static final String TAG = "CityLab";
    private static CityLab cityLab;
    private Context context;
    private City currentCity;
    private ArrayList<City> cities;
    private ArrayList<String> provinces;

    private CityLab(Context context) {
        cities=new ArrayList<City>();
        provinces = new ArrayList<String>();
        this.context=context;
    }

    public void setDefault(String cityName){
        currentCity=cityLab.isContain(cityName);
    }

    public static CityLab get(Context context) {
        if (cityLab == null) {
            cityLab = new CityLab(context);
        }
        return cityLab;
    }

    public City isContain(String cityname) {
        City city=new City();
        if (cityname != null) {
            for (int i=0;i<cities.size();i++) {
                City cityT=cities.get(i);
                if(cityname.contains(cityT.getCityName())){
//                    if(cityT.isParse()){
//                        return;
//                    }else
                    city=cityT;
                    break;
                }else if(i==cities.size()-1){
                    Log.i(TAG, "城市:" + cityname + "不存在");
                    cities.add(city);
                }
            }
        }else {
            city.setCityName("北京");
            city.setProvinceName("北京");
            city.setCtripId("beijing1");
        }
        return city;
    }

    public ArrayList<String> getProviceCity(String provinceName){
        ArrayList<String> provinceCities = new ArrayList<String>();
        for (int i=0;i<cities.size();i++){
            if(cities.get(i).getProvinceName().contains(provinceName)){
                provinceCities.add(cities.get(i).getCityName());
            }
        }
        return provinceCities;
    }

    public ArrayList<String> getProvince(){
        if(provinces.size()==0){
            String name;
            for(City city:cities){
                name=city.getProvinceName();
                if(!provinces.contains(name)) provinces.add(name);
            }
        }
        return provinces;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }
}
