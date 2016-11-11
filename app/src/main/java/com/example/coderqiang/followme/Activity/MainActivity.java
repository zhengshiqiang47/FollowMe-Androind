package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Text;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Fragment.JourneyFragment;
import com.example.coderqiang.followme.Fragment.SquarFragment;
import com.example.coderqiang.followme.Fragment.TestFragment;
import com.example.coderqiang.followme.Fragment.UserinfoFragment;
import com.example.coderqiang.followme.Model.JourneyDay;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {
    private static final String TAG="MainActivity";
    android.app.FragmentManager fm;
    JourneyFragment journeyFragment;
    UserinfoFragment userinfoFragment;
    SquarFragment squarFragment;
    Fragment currentFragment;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        new GetScenicSpot().execute();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    int count=0;
    @Override
    public void onBackPressed() {
        if(count<2){
            count++;
        }else {
            super.onBackPressed();
        }
    }

    private void initNavigation(){
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.position1, "日程").setActiveColor(R.color.journey_green))
                .addItem(new BottomNavigationItem(R.mipmap.fire_white, "广场").setActiveColor(R.color.journey_green))
                .addItem(new BottomNavigationItem(R.mipmap.chat, "联系人")).setActiveColor(R.color.journey_green)
                .addItem(new BottomNavigationItem(R.mipmap.mine,"我的")).setActiveColor(R.color.journey_green)
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Log.i(TAG, "position" + position);
                switch (position) {
                    case 0:
                        switchFragment(currentFragment,journeyFragment);
                        break;
                    case 1:
                        if(squarFragment==null){
                            squarFragment=new SquarFragment();
                        }
                        switchFragment(currentFragment,squarFragment);
                        break;
                    case 2:
                        Intent intent = new Intent(getApplicationContext(), ScenicDetailActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        if (userinfoFragment == null) {
                            userinfoFragment=new UserinfoFragment();
                        }
                        switchFragment(currentFragment,userinfoFragment);
                        break;
                }

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void switchFragment(Fragment from,Fragment to){
        if(currentFragment!=to){
            if(!to.isAdded()){
                fm.beginTransaction().add(R.id.fragment_container,to).commit();
            }else {
                fm.beginTransaction().hide(from).show(to).commit();
            }
            currentFragment=to;
        }

    }

    private void setDefaultFragment(){
        journeyFragment = JourneyFragment.newInstance();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_container, journeyFragment);
        ft.commit();
        currentFragment=journeyFragment;
    }

    private class GetScenicSpot extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpParse httpParse=new HttpParse();
            httpParse.getScenicspot(getApplicationContext(),"中国", "3");
            return null;
        }
    }
}
