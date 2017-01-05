package com.example.coderqiang.followme.Fragment;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bumptech.glide.Glide;
import com.example.coderqiang.followme.Activity.MainActivity;
import com.example.coderqiang.followme.Activity.PlaceDetaiWebView;
import com.example.coderqiang.followme.Activity.ScenicActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Activity.WebViewActivity;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.LocationApplication;
import com.example.coderqiang.followme.MapRelate.DrivingRouteOverlay;
import com.example.coderqiang.followme.Model.JourneyDay;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.Model.SettingLab;
import com.example.coderqiang.followme.Model.TravelPlanLab;
import com.example.coderqiang.followme.Model.TravleDay;
import com.example.coderqiang.followme.Model.TravlePlan;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Service.LocationService;
import com.example.coderqiang.followme.Service.MyOrientationListener;
import com.example.coderqiang.followme.Util.UploadImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/10/28.
 */

public class JourneyFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private static final String TAG="JourneyFragment";
    @Bind(R.id.journey_day_map_schedule_imagview)
    ImageView journey_day_map_schedule_imagview;
    @Bind(R.id.journey_day_map_schedule_layout)
    LinearLayout journey_day_map_schedule_layout;
    @Bind(R.id.journey_day_map_schedule_textview)
    TextView journey_day_map_schedule_textview;
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
    @Bind(R.id.journey_day_menu_layout)
    LinearLayout menuLayout;
    @Bind(R.id.menu_new_journey_layout)
    LinearLayout menuJourneyLayout;
    @Bind(R.id.menu_new_journey_note_layout)
    LinearLayout menuNoteLayou;
    @Bind(R.id.journey_day_mainlayout)
    RelativeLayout mainLayout;
    @Bind(R.id.menu_new_journey)
    TextView menuNewJourney;
    @Bind(R.id.journey_day_title_layout)
    LinearLayout titleLayout;
    public BDLocation bdLocation;
    private MyAdapter myAdapter;
    BottomSheetBehavior behavior;
    BaiduMap baiduMap;
    LocationClient locClient;
    BDLocationListener locListener;
    PoiSearch mPoiSearch;
    boolean isFirstLoc = true;
    boolean fabOpened=false;
    boolean change=false;

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
        menuLayout.setVisibility(View.GONE);
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
        UploadImage.setTouxiang(circleImagview,getActivity().getApplicationContext());
        journey_day_map_schedule_layout.setOnClickListener(this);
        journey_day_map_schedule_layout.setAlpha(0);
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
                if (newState==BottomSheetBehavior.STATE_COLLAPSED){
                    change=true;
                    Log.i(TAG, "true");
                }else if(newState==BottomSheetBehavior.STATE_HIDDEN){
                    journeyFab.setImageResource(R.drawable.ic_event_note_white_36dp);
                }else {
                    journeyFab.setImageResource(R.drawable.fab_plus_icon);
                }
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
                    mRecyclerview.setTranslationY(160*slideOffset);
                    if(slideOffset>0){
                        journey_day_map_schedule_layout.setAlpha(slideOffset);
                        if(change){
                            journey_day_map_schedule_imagview.setImageResource(R.drawable.ic_navigation_green_36dp);
                            journey_day_map_schedule_textview.setText("地图");
                            change = false;
                            Log.i(TAG,"地图模式");
                        }
                    }
                }
                if(slideOffset<=0){
                    searchView.setTranslationY(slideOffset*160);
                    titleLayout.setTranslationY(slideOffset*250);
                    if (slideOffset<0){
                        journey_day_map_schedule_layout.setAlpha(-slideOffset);
                        if(change){
                            journey_day_map_schedule_imagview.setImageResource(R.drawable.ic_schedule_green_36dp);
                            journey_day_map_schedule_textview.setText("日程");
                            change = false;
                            Log.i(TAG,"日程模式");
                        }
                    }
                }

                if(slideOffset==0||slideOffset>=0.99) titleLayout.setTranslationY(0);
            }
        });
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(SettingLab.getSettingLab(getActivity().getApplicationContext()).isTravelDayUpdate()){
                mRecyclerview.getAdapter().notifyDataSetChanged();
                SettingLab.getSettingLab(getActivity().getApplicationContext()).setTravelDayUpdate(false);
            }
            boolean touxiangUpdate=SettingLab.getSettingLab(getActivity().getApplicationContext()).isTouxiangUpdate();
            if (touxiangUpdate){
                UploadImage.setTouxiang(circleImagview,getActivity());
                SettingLab.getSettingLab(getActivity().getApplicationContext()).setTouxiangUpdate(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SettingLab.getSettingLab(getActivity().getApplicationContext()).isTravelDayUpdate()){
            mRecyclerview.getAdapter().notifyDataSetChanged();
            SettingLab.getSettingLab(getActivity().getApplicationContext()).setTravelDayUpdate(false);
        }
    }

    RoutePlanSearch routePlanSearch;
    LatLng latLng;
    SuggestionSearch suggestionSearch;
    UiSettings uiSettings;
    MyOrientationListener myOrientationListener;

    private void initMapview(){
        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);
        Point point=new Point(100,220);
        uiSettings=baiduMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        baiduMap.setCompassPosition(point);
//        baiduMap.setCompassIcon(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.ic_navigation_white_24dp));
        suggestionSearch=SuggestionSearch.newInstance();
        OnGetSuggestionResultListener suggestionResultListener=new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult result) {
                if (result == null || result.getAllSuggestions() == null) {
                    return;
                }
                SuggestionResult.SuggestionInfo suggestionInfo=result.getAllSuggestions().get(0);

                Log.i(TAG, "" + suggestionInfo.city + " " + suggestionInfo.key + " " + suggestionInfo.describeContents() + " " + suggestionInfo.district + " "+suggestionInfo.uid);
            }
        };
        suggestionSearch.setOnGetSuggestionResultListener(suggestionResultListener);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setSubmitButtonEnabled(true);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                .keyword(newText)
                .city("福州"));
                return true;
            }
        });
        initMapAndLoction();
    }


    private void initMapAndLoction(){
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
                MyLocation myLocation=MyLocation.getMyLocation(getActivity());
                myLocation.setCityName(location.getCity());
                myLocation.setLatitute(location.getLatitude());
                myLocation.setLongtitute(location.getLongitude());
                myLocation.setHasLocation(true);
                bdLocation=location;
                baiduMap.setMyLocationData(locationData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    latLng=ll;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        };
        locClient.registerLocationListener(locListener);
        initLocation();
        locClient.start();
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
                LatLng markLl=poiDetailResult.getLocation();
                BitmapDescriptor bitmap=BitmapDescriptorFactory.fromResource(R.drawable.location_red_2);
                OverlayOptions options = new MarkerOptions()
                        .position(markLl)
                        .icon(bitmap);
                baiduMap.addOverlay(options);
                Intent intent = new Intent(getActivity(), PlaceDetaiWebView.class);
                intent.putExtra("url", poiDetailResult.getDetailUrl());
                startActivity(intent);
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        routePlanSearch= RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener listener=new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(getActivity(), "抱歉，未找到结果1", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
                DrivingRouteLine line = result.getRouteLines().get(0);
                overlay.setData(line);
                overlay.addToMap();
                overlay.zoomToSpan();

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        routePlanSearch.setOnGetRoutePlanResultListener(listener);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (TravelPlanLab.get(getActivity().getApplicationContext()).getTravelPlans().size()==0){
                    try {
                        Log.i(TAG,"sleep");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"正在获取数据");
                        myAdapter = new MyAdapter();
                        mRecyclerview.setAdapter(myAdapter);
                    }
                }));


            }
        }).start();

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
            case R.id.journey_day_map_schedule_layout:
                int state2=behavior.getState();
                if(state2==BottomSheetBehavior.STATE_HIDDEN){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    journey_day_map_schedule_imagview.setImageResource(R.drawable.ic_navigation_green_36dp);
                    journey_day_map_schedule_textview.setText("地图");
                }else if(state2==BottomSheetBehavior.STATE_EXPANDED){
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    journey_day_map_schedule_imagview.setImageResource(R.drawable.ic_schedule_green_36dp);
                    journey_day_map_schedule_textview.setText("日程");
                }

        }
    }

    private void openMenu(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",0,-135);
        animator.setDuration(300);
        animator.start();
        final View hview = view;
        menuHideTv.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,0.7f);
        AlphaAnimation alphaAnimation2=new AlphaAnimation(0.0f,1.0f);
        alphaAnimation2.setDuration(300);
        alphaAnimation2.setFillAfter(true);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        menuHideTv.startAnimation(alphaAnimation);
        menuDayLayout.setVisibility(View.VISIBLE);
        menuJourneyLayout.setVisibility(View.VISIBLE);
        menuLayout.setVisibility(View.VISIBLE);
        menuNoteLayou.setVisibility(View.VISIBLE);
        menuDayLayout.startAnimation(alphaAnimation2);
        menuJourneyLayout.startAnimation(alphaAnimation2);
        menuNoteLayou.startAnimation(alphaAnimation2);
        fabOpened=true;
    }

    private void closeMenu(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",-135,0);
        animator.setDuration(300);
        animator.start();
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.7f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        menuHideTv.startAnimation(alphaAnimation);
        menuDayLayout.startAnimation(alphaAnimation);
        menuJourneyLayout.startAnimation(alphaAnimation);
        menuNoteLayou.startAnimation(alphaAnimation);
        menuDayLayout.setVisibility(View.GONE);
        menuJourneyLayout.setVisibility(View.GONE);
        menuNoteLayou.setVisibility(View.GONE);
        menuLayout.setVisibility(View.GONE);
        fabOpened=false;
    }


    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        TravlePlan travlePlan;
        private static final int TYPE_HEADER=0;
        private static final int TYPE_NORMAL=1;
        private static final int TYPE_FOOTER=2;

        public MyAdapter() {
            super();
            if(TravelPlanLab.get(getActivity().getApplicationContext()).getTravelPlans().size()==0){

            }else {
                travlePlan = TravelPlanLab.get(getActivity().getApplicationContext()).getTravelPlans().get(0);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_HEADER){
                return new HeaderHolder(LayoutInflater.from(getActivity()).inflate(R.layout.journey_recycler_header, parent, false));
            }
            if(viewType==TYPE_NORMAL){
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.journey_day_list_item, parent, false));
                return holder;
            }
            if(viewType==TYPE_FOOTER){
                return new FooterHolder(LayoutInflater.from(getActivity()).inflate(R.layout.journey_recycler_footer, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {

            if(viewholder instanceof MyViewHolder){
                int tempposition=position-1;
                final TravleDay travleDay=travlePlan.getTravleDays().get(tempposition);
                MyViewHolder holder=(MyViewHolder)viewholder;
                holder.city.setText(travlePlan.getCity().getCityName());
                holder.detail.setText(travleDay.getMemo());
                holder.detail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.i(TAG,"beforeTextChanged"+s);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.i(TAG,"onTextChanged:"+s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        travleDay.setMemo(s.toString());
                    }
                });
                holder.day.setText("Day " + travleDay.getDayNum());
                final String cityName=travlePlan.getCity().getCityName();
                holder.city.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),ScenicActivity.class);
                        intent.putExtra(ScenicActivity.EXTRA_CITY,cityName);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    }
                });
                holder.scenicRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                holder.scenicRecycler.setAdapter(new scenicAdapter(travleDay));
            }else if(viewholder instanceof HeaderHolder){
                HeaderHolder headerHolder=(HeaderHolder)viewholder;
            }

        }

        @Override
        public int getItemViewType(int position) {
            if(position==0){
                return TYPE_HEADER;
            }else if(position==travlePlan.getTravleDays().size()+1) {
                return TYPE_FOOTER;
            }else return TYPE_NORMAL;
        }

        @Override
        public int getItemCount() {
            if(travlePlan==null) return 2;
            return travlePlan.getTravleDays().size()+2;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView city;
            EditText detail;
            TextView day;

            RecyclerView scenicRecycler;
            public MyViewHolder(View itemView) {
                super(itemView);
                city = (TextView) itemView.findViewById(R.id.journey_day_city);
                detail = (EditText) itemView.findViewById(R.id.journey_day_detail);
                day = (TextView) itemView.findViewById(R.id.journey_day_day);
                scenicRecycler=(RecyclerView)itemView.findViewById(R.id.journey_day_normal_recycler);
            }


        }

        private class scenicAdapter extends RecyclerView.Adapter{
            ArrayList<Scenicspot> scenicspots;
            TravleDay travleDay;

            public scenicAdapter(TravleDay travleDay) {
                super();
                scenicspots=travleDay.getScenicspots();
                this.travleDay=travleDay;
                Log.i(TAG, "景点数:" + scenicspots.size());
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new scenicHolder(LayoutInflater.from(getActivity()).inflate(R.layout.journey_day_travel_item,parent,false));
            }

            @Override
            public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
                scenicHolder scenicHolder=(scenicAdapter.scenicHolder)holder;
                final Scenicspot scenicspot=scenicspots.get(position);
                scenicHolder.scenicName.setText(scenicspots.get(position).getScenicName());
                scenicHolder.scenicName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                        intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC_SER,scenicspot);
                        ActivityOptions activityOptions=null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            android.util.Pair<View, String> pair[] = new android.util.Pair[1];
                            pair[0] = new android.util.Pair<View, String>(((scenicHolder) holder).scenicName, "scenic_name");
                            activityOptions= ActivityOptions.makeSceneTransitionAnimation(getActivity(),pair);
                        }
                        startActivity(intent,activityOptions.toBundle());
                    }
                });
                scenicHolder.scenicName.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.i(TAG,"长按");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("提示");
                        builder.setMessage("是否从Day "+travleDay.getDayNum()+"删除"+scenicspot.getScenicName());
                        builder.setIcon(R.drawable.circle);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                travleDay.deleteScenicSpots(scenicspot);
                                scenicspots=travleDay.getScenicspots();
                                notifyItemRemoved(position);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                });
                final String endPlace=scenicspot.getScenicName();
                if(position!=0){
                    scenicHolder.addIcon.setVisibility(View.GONE);
                }else {
                    scenicHolder.addIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getActivity(),ScenicActivity.class);
                            intent.putExtra(ScenicActivity.EXTRA_CITY,MyLocation.getMyLocation(getActivity().getApplicationContext()).getCityName());
                            startActivity(intent);
                            Toast.makeText(getActivity(), "选择景点并点击\"到这去\"",Toast.LENGTH_SHORT).show();
                            getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                        }
                    });
                }
                scenicHolder.goIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "开始路径规划");
                        PlanNode st = PlanNode.withLocation(latLng);
                        PlanNode ed = PlanNode.withCityNameAndPlaceName("福州",endPlace);
                        routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(st).to(ed));
                        Toast.makeText(getActivity(),"开始路径规划,到"+endPlace,Toast.LENGTH_SHORT).show();
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        mRecyclerview.scrollToPosition(0);
                    }
                });

            }

            @Override
            public int getItemCount() {
                return scenicspots.size();
            }

            private class scenicHolder extends RecyclerView.ViewHolder{
                private TextView scenicName;
                ImageView addIcon;
                ImageView goIcon;
                RelativeLayout travelItemLayout;
                public scenicHolder(View itemView) {
                    super(itemView);
                    scenicName=(TextView)itemView.findViewById(R.id.journey_day_travel_item_name);
                    addIcon=(ImageView)itemView.findViewById(R.id.journey_day_travel_item_add);
                    goIcon = (ImageView) itemView.findViewById(R.id.journey_day_travel_item_go);
                    travelItemLayout=(RelativeLayout)itemView.findViewById(R.id.travel_item_layout);
                }
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

        private class FooterHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public FooterHolder(View itemView) {
                super(itemView);
            }
        }

    }

}
