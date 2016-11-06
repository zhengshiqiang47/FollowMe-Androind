package com.example.coderqiang.followme.Model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class ScenicspotLab {
    private static ScenicspotLab scenicspotLab;
    private ArrayList<Scenicspot> scenicspots;
    private Context mContext;

    private ScenicspotLab(Context context) {
        scenicspots = new ArrayList<Scenicspot>();
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
}
