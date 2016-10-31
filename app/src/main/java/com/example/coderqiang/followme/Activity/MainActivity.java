package com.example.coderqiang.followme.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Text;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Fragment.JourneyFragment;
import com.example.coderqiang.followme.Model.JourneyDay;
import com.example.coderqiang.followme.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    android.app.FragmentManager fm;
    JourneyFragment journeyFragment;
    @Bind(R.id.journey_nav)
    BottomNavigationBar bottomNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fm = this.getFragmentManager();
        setDefaultFragment();
        initNavigation();

    }

    private void initNavigation(){
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.position1, "日程").setActiveColor(R.color.journey_green))
                .addItem(new BottomNavigationItem(R.mipmap.xingji1, "广场").setActiveColor(R.color.journey_green))
                .addItem(new BottomNavigationItem(R.mipmap.chat, "联系人")).setActiveColor(R.color.journey_green)
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void setDefaultFragment(){
        journeyFragment = JourneyFragment.newInstance();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_container, journeyFragment);
        ft.commit();
    }


}
