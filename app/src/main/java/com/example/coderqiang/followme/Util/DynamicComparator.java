package com.example.coderqiang.followme.Util;

import com.example.coderqiang.followme.Model.Dynamic;

import java.util.Comparator;

/**
 * Created by CoderQiang on 2017/3/5.
 */

public class DynamicComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Dynamic d1=(Dynamic)o1;
        Dynamic d2=(Dynamic)o2;
        if(d1.getTimeStamp()>d2.getTimeStamp()){
            return -1;
        }else if(d1.getTimeStamp()==d2.getTimeStamp()){
            return 0;
        }else
            return 1;
    }
}
