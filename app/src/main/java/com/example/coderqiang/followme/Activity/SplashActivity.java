package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.coderqiang.followme.R;

/**
 * Created by CoderQiang on 2016/12/16.
 */

public class SplashActivity extends Activity {
    Activity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_null);
        activity=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();
    }
}
