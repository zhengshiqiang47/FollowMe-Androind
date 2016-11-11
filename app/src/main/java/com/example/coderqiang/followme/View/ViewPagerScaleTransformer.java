package com.example.coderqiang.followme.View;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by CoderQiang on 2016/11/8.
 */

public class ViewPagerScaleTransformer implements ViewPager.PageTransformer {
    float minScale=0.3f;
    float rightminscale=0.3f;
    float defScale=1.0f;
    @Override
    public void transformPage(View page, float position) {
        if (position < -1)
        {
            page.setScaleX(minScale);
            page.setScaleY(minScale);
        } else if (position <= 1)
        { // [-1,1]

            if (position < 0) //[0，-1]
            {
                float factor =minScale + (1 - minScale) * (1 + position);
                page.setScaleX(factor);
                page.setScaleY(factor);
            } else//[1，0]
            {
                float factor = rightminscale+ (1 - rightminscale) * (1 - position);
                page.setScaleX(factor);
                page.setScaleY(factor);
            }
        } else
        { // (1,+Infinity]
            page.setScaleX(rightminscale);
            page.setScaleY(rightminscale);
        }
    }
}
