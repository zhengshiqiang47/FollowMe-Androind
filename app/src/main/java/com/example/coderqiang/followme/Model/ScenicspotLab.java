package com.example.coderqiang.followme.Model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class ScenicspotLab {
    private static ScenicspotLab scenicspotLab;
    private ArrayList<Scenicspot> scenicspots;
    private ArrayList<Scenicspot> hotScenicspots;
    private ArrayList<Scenicspot> collectionspots;
    private Context mContext;

    private ScenicspotLab(Context context) {
        scenicspots = new ArrayList<Scenicspot>();
        hotScenicspots = new ArrayList<Scenicspot>();
        collectionspots = new ArrayList<Scenicspot>();
        mContext = context;
    }

    public static ScenicspotLab get(Context context) {
        if (scenicspotLab == null) {
            scenicspotLab = new ScenicspotLab(context);
        }
        return scenicspotLab;
    }

    public ArrayList<Scenicspot> getScenicspots() {
        return scenicspots;
    }

    public ArrayList<Scenicspot> getHotScenicspots() {
        return hotScenicspots;
    }


    public ArrayList<Scenicspot> getCollectionspots() {
        return collectionspots;
    }

    public void setCollectionspots(ArrayList<Scenicspot> collectionspots) {
        this.collectionspots = collectionspots;
    }

    public void clear(){
        scenicspots.clear();
        hotScenicspots.clear();
        collectionspots.clear();
    }
}
