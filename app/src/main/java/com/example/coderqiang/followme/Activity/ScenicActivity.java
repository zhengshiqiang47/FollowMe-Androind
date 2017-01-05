package com.example.coderqiang.followme.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.coderqiang.followme.Fragment.ScenicFragment;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/12/4.
 */

public class ScenicActivity extends FragmentActivity {
    public static final String EXTRA_CITY="city";

    City city;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenic);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
        city= CityLab.get(getApplicationContext()).isContain(getIntent().getStringExtra(EXTRA_CITY));
        Log.i("MainActivity", "Activity接收的城市名" + city.getCityName());
        Fragment scenicFragment= ScenicFragment.newInstance(city);
        getSupportFragmentManager().beginTransaction().replace(R.id.scenic_framelayout, scenicFragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_slide_exit,R.anim.back_slide_enter);
    }

}

