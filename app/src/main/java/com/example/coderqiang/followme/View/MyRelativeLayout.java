package com.example.coderqiang.followme.View;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by CoderQiang on 2016/11/29.
 */

public class MyRelativeLayout extends RelativeLayout {
    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        Rect newRect=insets;
        insets.set(newRect.left,0,newRect.right,newRect.bottom);
        return super.fitSystemWindows(insets);
    }
}
