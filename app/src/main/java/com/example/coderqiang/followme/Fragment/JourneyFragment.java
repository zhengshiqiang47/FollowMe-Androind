package com.example.coderqiang.followme.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.baidu.mapapi.map.MapView;
import com.example.coderqiang.followme.Activity.MainActivity;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.JourneyDay;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.View.MyLinearLayoutManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/10/28.
 */

public class JourneyFragment extends Fragment implements View.OnClickListener{
    private static final String TAG="JourneyFragment";
    @Bind(R.id.baiduMapView)
    MapView mapView;
    @Bind(R.id.journey_coordinatorlayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.user_touxiang)
    CircleImagview circleImagview;
    @Bind(R.id.journey_day_recyclerview)
    RecyclerView mRecyclerview;
    DrawerLayout drawer;
    @Bind(R.id.journey_day_fab)
    FloatingActionButton journeyFab;
    @Bind(R.id.journey_day_search)
    SearchView searchView;
    private ArrayList<JourneyDay> journeyDays;
    private MyAdapter myAdapter;
    BottomSheetBehavior behavior;

    public static JourneyFragment newInstance(){
        JourneyFragment journeyFragment = new JourneyFragment();
        return journeyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_journey, container, false);
        ButterKnife.bind(this,v);
        circleImagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer = (DrawerLayout) getActivity().findViewById(R.id.main_drawerlayout);
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        journeyFab.setOnClickListener(this);
        behavior=BottomSheetBehavior.from(mRecyclerview);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.i(TAG,"State:"+newState);
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i(TAG,"slideOffset:"+slideOffset);
                if(slideOffset>=0) {
                    searchView.setTranslationY(-slideOffset*200);
                    mapView.setTranslationY(-slideOffset*400);
                    journeyFab.setTranslationX(0);
                    journeyFab.setTranslationY(0);
                }
                if(slideOffset<=0){
                    journeyFab.setTranslationY(slideOffset*250);
                    journeyFab.setTranslationX(-slideOffset*140);
                }
            }
        });
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new MyAdapter();
        mRecyclerview.setAdapter(myAdapter);
        return v;
    }

    private void initData(){
        journeyDays = new ArrayList<JourneyDay>();
        for(int i=0;i<6;i++){
            JourneyDay journeyDay=new JourneyDay();
            journeyDay.setCity("北京"+i);
            journeyDay.setDetail("故宫一日游，宫一日游，同妻子。");
            journeyDay.setDate("2016-11-"+i);
            journeyDay.setDay(i+1+"");
            journeyDays.add(journeyDay);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.journey_day_fab:
                Log.i(TAG,"监听");
                if(behavior.getState()==BottomSheetBehavior.STATE_HIDDEN){
                    Log.i(TAG,"隐藏变展开");
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
        }
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.journey_day_list_item, parent, false));
                return holder;

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.city.setText(journeyDays.get(position).getCity());
            holder.detail.setText(journeyDays.get(position).getDetail());
            holder.day.setText("Day " + journeyDays.get(position).getDay());
        }


        @Override
        public int getItemCount() {
            return journeyDays.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView city;
            TextView detail;
            TextView day;
            public MyViewHolder(View itemView) {
                super(itemView);
                city = (TextView) itemView.findViewById(R.id.journey_day_city);
                detail = (TextView) itemView.findViewById(R.id.journey_day_detail);
                day = (TextView) itemView.findViewById(R.id.journey_day_day);
            }
        }


    }
}
