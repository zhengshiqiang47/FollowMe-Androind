package com.example.coderqiang.followme.View;

import android.app.Dialog;
import android.content.Context;

import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.TravleDay;

/**
 * Created by CoderQiang on 2017/1/5.
 */

public class DeleteScenicDialog extends Dialog {

    public DeleteScenicDialog(Context context, TravleDay travleDay, Scenicspot scenicspot) {
        super(context);

    }

    public DeleteScenicDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DeleteScenicDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
