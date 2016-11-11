package com.example.coderqiang.followme.Fragment;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.coderqiang.followme.Activity.WebViewActivity;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.LocationApplication;
import com.example.coderqiang.followme.Model.JourneyDay;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Service.LocationService;

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
    @Bind(R.id.journey_menu_hide_text)
    TextView menuHideTv;
    @Bind(R.id.menu_new_day_layout)
    LinearLayout menuDayLayout;
    @Bind(R.id.menu_new_journey_layout)
    LinearLayout menuJourneyLayout;
    @Bind(R.id.menu_new_journey_note_layout)
    LinearLayout menuNoteLayou;
    @Bind(R.id.journey_day_mainlayout)
    LinearLayout mainLayout;
    @Bind(R.id.menu_new_journey)
    TextView menuNewJourney;
    private ArrayList<JourneyDay> journeyDays;
    private MyAdapter myAdapter;
    BottomSheetBehavior behavior;
    BaiduMap baiduMap;
    LocationClient locClient;
    BDLocationListener locListener;
    LocationService locationService;
    PoiSearch mPoiSearch;
    boolean isFirstLoc = true;
    boolean fabOpened=false;
    float headerTransY;

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
        initView();
        initMapview();
        return v;
    }

    private void initView(){
        menuHideTv.setVisibility(View.GONE);
        menuDayLayout.setVisibility(View.GONE);
        menuJourneyLayout.setVisibility(View.GONE);
        menuNoteLayou.setVisibility(View.GONE);
        menuNewJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu(journeyFab);
            }
        });
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
            private static final int ARROW_DOWN=2;
            private static final int ARROW_UP=3;
            private int arrowType=ARROW_DOWN;
            private int centerX;
            private int centerY;
            ImageView imageView;
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.i(TAG,"State:"+newState);
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (imageView == null) {
                    imageView = (ImageView) getActivity().findViewById(R.id.journey_recy_header_arrow);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(behavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }else {
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        }
                    });
                }
//                Log.i(TAG,"slideOffset:"+slideOffset);
                if(slideOffset>=0) {
                    searchView.setTranslationY(-slideOffset*200);
                    mapView.setTranslationY(-slideOffset*400);
                    journeyFab.setTranslationX(0);
                    journeyFab.setTranslationY(0);
                    imageView.setRotation(180+slideOffset*180);
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
    }

    private void initMapview(){
        locClient = new LocationClient(getActivity().getApplicationContext());
        locListener=new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyLocationData locationData=new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(100)
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                baiduMap.setMyLocationData(locationData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        };
        locClient.registerLocationListener(locListener);
        initLocation();
        locClient.start();

        baiduMap = mapView.getMap();
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(TAG, "-->" + latLng.toString());
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                Log.i(TAG, "Poi-->" + mapPoi.toString());
                mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(mapPoi.getUid()));
                return true;
            }
        });
        baiduMap.setMyLocationEnabled(true);
        mPoiSearch=PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                Log.i(TAG, "PoiResult-->" + poiResult);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                Log.i(TAG, "PoiDetailResult-->Name:" + poiDetailResult.getName()+" Address:"+poiDetailResult.getAddress()+" Url:"+poiDetailResult.getDetailUrl()+" \n shopHours:"+poiDetailResult.getShopHours()+" ImageNum"+poiDetailResult.getImageNum()+" "+poiDetailResult.getTelephone()+" "+poiDetailResult.getCommentNum()+" "+poiDetailResult.getEnvironmentRating()+" "+poiDetailResult.getTechnologyRating());
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.WEB_URL, poiDetailResult.getDetailUrl());
                startActivity(intent);
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        locClient.setLocOption(option);
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
                int state=behavior.getState();
                if(state==BottomSheetBehavior.STATE_HIDDEN){
                    Log.i(TAG,"隐藏变展开");
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else if(state==BottomSheetBehavior.STATE_COLLAPSED||state==BottomSheetBehavior.STATE_EXPANDED){
                    Log.i(TAG,"菜单展开");
                    if(!fabOpened){
                        openMenu(journeyFab);
                    }else {
                        closeMenu(journeyFab);
                    }
                }
                break;
        }
    }

    private void openMenu(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",0,-135);
        animator.setDuration(700);
        animator.start();
        menuHideTv.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,0.7f);
        AlphaAnimation alphaAnimation2=new AlphaAnimation(0.0f,1.0f);
        alphaAnimation2.setDuration(600);
        alphaAnimation2.setFillAfter(true);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        menuHideTv.startAnimation(alphaAnimation);
        menuDayLayout.setVisibility(View.VISIBLE);
        menuJourneyLayout.setVisibility(View.VISIBLE);
        menuNoteLayou.setVisibility(View.VISIBLE);
        menuDayLayout.startAnimation(alphaAnimation2);
        menuJourneyLayout.startAnimation(alphaAnimation2);
        menuNoteLayou.startAnimation(alphaAnimation2);
        fabOpened=true;
    }
    private void closeMenu(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",-135,0);
        animator.setDuration(600);
        animator.start();
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.7f,0.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        menuHideTv.startAnimation(alphaAnimation);
        menuHideTv.setVisibility(View.GONE);
        menuDayLayout.startAnimation(alphaAnimation);
        menuJourneyLayout.startAnimation(alphaAnimation);
        menuNoteLayou.startAnimation(alphaAnimation);
        menuDayLayout.setVisibility(View.GONE);
        menuJourneyLayout.setVisibility(View.GONE);
        menuNoteLayou.setVisibility(View.GONE);
        fabOpened=false;
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_HEADER=0;
        private static final int TYPE_NORMAL=1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_HEADER){
                return new HeaderHolder(LayoutInflater.from(getActivity()).inflate(R.layout.journey_recycler_header, parent, false));
            }
            if(viewType==TYPE_NORMAL){
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.journey_day_list_item, parent, false));
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
            if(viewholder instanceof MyViewHolder){
                position=position-1;
                MyViewHolder holder=(MyViewHolder)viewholder;
                holder.city.setText(journeyDays.get(position).getCity());
                holder.detail.setText(journeyDays.get(position).getDetail());
                holder.day.setText("Day " + journeyDays.get(position).getDay());
            }else if(viewholder instanceof HeaderHolder){
                HeaderHolder headerHolder=(HeaderHolder)viewholder;
            }

        }

        @Override
        public int getItemViewType(int position) {
            if(position==0){
                return TYPE_HEADER;
            }else return TYPE_NORMAL;
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

        private class HeaderHolder extends RecyclerView.ViewHolder {
            ImageView arrowView;
            public HeaderHolder(View itemView) {
                super(itemView);
                arrowView = (ImageView) itemView.findViewById(R.id.journey_recy_header_arrow);
                arrowView.setRotation(180);
            }
        }

    }
}
