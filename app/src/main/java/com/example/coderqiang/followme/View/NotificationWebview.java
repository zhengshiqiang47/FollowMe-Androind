package com.example.coderqiang.followme.View;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by zsq on 16-8-28.
 */
public class NotificationWebview extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public NotificationWebview(Context context) {
        super(context);
    }

    public NotificationWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }
}
