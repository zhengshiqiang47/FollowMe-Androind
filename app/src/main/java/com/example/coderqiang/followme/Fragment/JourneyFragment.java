package com.example.coderqiang.followme.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
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
import com.example.coderqiang.followme.Activity.ScenicActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Adapter.SearchResultAdapter;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.MapRelate.DrivingRouteOverlay;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.LocationInfo;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.MySearchResult;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.SettingLab;
import com.example.coderqiang.followme.Model.TravelPlanLab;
import com.example.coderqiang.followme.Model.TravleDay;
import com.example.coderqiang.followme.Model.TravlePlan;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Service.MyOrientationListener;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.Util.UploadImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    FloatingActionButton journeyFab;
    @Bind(R.id.journey_day_search)
    SearchView searchView;
    @Bind(R.id.map_my_location)
    ImageView myLocationIcon;
    FrameLayout menuHideTv;
    LinearLayout menuDayLayout;
    LinearLayout menuLayout;
    LinearLayout menuJourneyLayout;
    LinearLayout menuNoteLayou;
    @Bind(R.id.journey_day_mainlayout)
    RelativeLayout mainLayout;
    TextView menuNewJourney;
    @Bind(R.id.journey_day_title_layout)
    LinearLayout titleLayout;
    @Bind(R.id.journey_search_reslut_recy)
    RecyclerView searchRecycler;
    LinearLayout menuAddScenicLayout;
    LinearLayout mapMakerLayout;
    public BDLocation bdLocation;
    private MyAdapter myAdapter;
    BottomSheetBehavior behavior;
    private View rootView;

    //地图和定位
    BaiduMap baiduMap;
    MyBottomSheetCallback bottomSheetCallback;
    MyGetRoutePlanResultListener getRoutePlanResultListener;
    LocationClient locClient;
    BitmapDescriptor myLocationIconBD;
    BDLocationListener locListener;
    PoiSearch mPoiSearch;
    MyOrientationListener orientationListener;
    //覆盖物相关
    private BitmapDescriptor makerIcon;
    //地图检索相关
    private PoiDetailResult currentPoiResutl;

    boolean isFirstLoc = true;
    boolean isFirstGet=true;
    boolean fabOpened=false;
    boolean change=false;
    private float currentX;
    int windowWidth;
    int windowHeight;

    TravlePlan currentPlan;

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
        EventBus.getDefault().register(this);
        rootView=v;
        initView();
        initMapview();
        return v;
    }


    private void initView(){
        WindowManager windowManager=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowWidth=windowManager.getDefaultDisplay().getWidth();
        windowHeight=windowManager.getDefaultDisplay().getHeight();
        orientationListener=new MyOrientationListener(getActivity());
        orientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                currentX=x;
                if(bdLocation!=null){
                    MyLocationData locationData=new MyLocationData.Builder()
                            .accuracy(bdLocation.getRadius())
                            .direction(currentX)
                            .latitude(bdLocation.getLatitude())
                            .longitude(bdLocation.getLongitude())
                            .build();
                    baiduMap.setMyLocationData(locationData);
                }

            }
        });
        journeyFab=(FloatingActionButton)getActivity().findViewById(R.id.journey_day_fab);
        menuLayout=(LinearLayout)getActivity().findViewById(R.id.journey_day_menu_layout);
        menuHideTv=(FrameLayout)getActivity().findViewById(R.id.journey_menu_hide_text);
        menuDayLayout=(LinearLayout)getActivity().findViewById(R.id.menu_new_day_layout);
        menuJourneyLayout=(LinearLayout)getActivity().findViewById(R.id.menu_new_journey_layout);
        menuNoteLayou=(LinearLayout)getActivity().findViewById(R.id.menu_new_journey_note_layout);
        menuNewJourney=(TextView) getActivity().findViewById(R.id.menu_new_journey);
        menuAddScenicLayout=(LinearLayout)getActivity().findViewById(R.id.menu_add_scenic_layout);
        myLocationIconBD=BitmapDescriptorFactory.fromResource(R.mipmap.ic_navigation_blue_36dp);
        searchRecycler.setAdapter(new SearchResultAdapter(getActivity().getApplicationContext(),new ArrayList<PoiInfo>(),baiduMap));
        searchRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        bottomSheetCallback=new MyBottomSheetCallback();
        behavior.setBottomSheetCallback(bottomSheetCallback);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myLocationIcon.setOnClickListener(this);
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
        mapView.onResume();
    }
//检索相关
    RoutePlanSearch routePlanSearch;
    LatLng latLng;
    SuggestionSearch suggestionSearch;
    UiSettings uiSettings;
    PoiSearch poiSearch;

    private void initMapview(){
        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);
        final Point point=new Point(100,220);
        uiSettings=baiduMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        baiduMap.setCompassPosition(point);
//        baiduMap.setCompassIcon(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.ic_navigation_white_24dp));
        suggestionSearch=SuggestionSearch.newInstance();
        poiSearch=PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {

                if (poiResult == null||poiResult.getAllPoi()==null) return;
                for (PoiInfo poiInfo : poiResult.getAllPoi()) {
                    if(poiInfo.address==null) Log.i(TAG,"此条信息无用");
                    Log.i(TAG,poiInfo.name+" "+poiInfo.address+" "+poiInfo.city);
                }
                ArrayList<PoiInfo> poiInfos = (ArrayList<PoiInfo>) poiResult.getAllPoi();
                searchRecycler.setVisibility(View.VISIBLE);
                if(behavior.getState()==BottomSheetBehavior.STATE_HIDDEN){
                    searchRecycler.setTranslationY(-200f);
                }
                searchRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                searchRecycler.setAdapter(new SearchResultAdapter(getActivity().getApplicationContext(),poiInfos,baiduMap));
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        final OnGetSuggestionResultListener suggestionResultListener=new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult result) {
                if (result == null || result.getAllSuggestions() == null) {
                    Log.i(TAG,"搜索结果为空");
                    return;
                }
                ArrayList<MySearchResult> mySearchResults = new ArrayList<>();
                for (SuggestionResult.SuggestionInfo info:result.getAllSuggestions()){
                    if(info.uid==null||info.city==null) continue;
//                    Log.i(TAG, "" + info.city + " " + info.key + " " + info.district + " "+info.uid);
                    MySearchResult mySearchResult =new MySearchResult(1,info.key,info.district,info.city);
                    mySearchResults.add(mySearchResult);
                }
//                searchRecycler.setVisibility(View.VISIBLE);
//                searchRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//                searchRecycler.setAdapter(new SearchResultAdapter(getActivity().getApplicationContext(),mySearchResults,baiduMap));
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
                Log.e(TAG,"newText:"+newText);
                City city=CityLab.get(getActivity().getApplicationContext()).getCurrentCity();
                if (newText==null||newText.equals("")) {
                    Log.e(TAG,"关键字为空");
                    searchRecycler.setVisibility(View.GONE);
                }
                if(newText!=null&&latLng==null) return true;
//                poiSearch.searchInCity((new PoiNearbySearchOption().pageCapacity(15).keyword(newText).sortType(PoiSortType.distance_from_near_to_far)));
                poiSearch.searchNearby((new PoiNearbySearchOption().pageCapacity(15).pageNum(0).keyword(newText).sortType(PoiSortType.distance_from_near_to_far).location(latLng).radius(10000)));
//                suggestionSearch.requestSuggestion(new SuggestionSearchOption().keyword(newText).location(latLng).city(city.getCityName()));
                return true;
            }
        });
        initMapAndLoction();
        initMakers();
    }

    private void initMakers() {
        makerIcon= BitmapDescriptorFactory.fromResource(R.drawable.location_red);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return false;
            }
        });
    }


    private void initMapAndLoction(){
        locClient = new LocationClient(getActivity().getApplicationContext());
        locListener=new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyLocationData locationData=new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(currentX)
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                MyLocation myLocation=MyLocation.getMyLocation(getActivity());
                myLocation.setCityName(location.getCity());
                myLocation.setLatitute(location.getLatitude());
                myLocation.setLongtitute(location.getLongitude());
                myLocation.setHasLocation(true);
                bdLocation=location;
                if(myLocation.getCityName()!=null){
                    baiduMap.setMyLocationData(locationData);
                }
                if (location.getCity() != null) {
                    myLocation.setHasLocation(true);
                    myLocation.setCityName(location.getCity());
                    myLocation.setLatitute(location.getLatitude());
                    myLocation.setLongtitute(location.getLongitude());
                    if (isFirstGet) {
                        MyLocationConfiguration config=new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,myLocationIconBD);
                        //设置自定义图标
                        baiduMap.setMyLocationConfigeration(config);
                        CityLab.get(getActivity().getApplicationContext()).setCurrentCity(CityLab.get(getActivity().getApplicationContext()).isContain(location.getCity()));
                        EventBus.getDefault().post("定位改变");
                        MyLocation.getMyLocation(getActivity().getApplicationContext()).setBdLocation(location);
                        isFirstGet = false;
                    }
                }else {
                    Log.i(TAG,"定位中");
                    myLocation.setHasLocation(false);
                    CityLab.get(getActivity().getApplicationContext()).setCurrentCity(CityLab.get(getActivity().getApplicationContext()).isContain("杭州"));
                }
                if (isFirstLoc&&myLocation.getCityName()!=null) {
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
                getPoiDetail(poiDetailResult);
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        routePlanSearch= RoutePlanSearch.newInstance();
        getRoutePlanResultListener=new MyGetRoutePlanResultListener();
        OnGetRoutePlanResultListener listener= getRoutePlanResultListener;
        routePlanSearch.setOnGetRoutePlanResultListener(listener);
    }

    private void getPoiDetail(final PoiDetailResult poiDetailResult) {
        currentPoiResutl=poiDetailResult;
        Log.i(TAG, "PoiDetailResult-->Name:" + poiDetailResult.getName()+" Address:"+poiDetailResult.getAddress()+" Url:"+poiDetailResult.getDetailUrl()+" \n shopHours:"+poiDetailResult.getShopHours()+" ImageNum"+poiDetailResult.getImageNum()+" "+poiDetailResult.getTelephone()+" "+poiDetailResult.getCommentNum()+" "+poiDetailResult.getEnvironmentRating()+" "+poiDetailResult.getTechnologyRating()+" "+poiDetailResult.getType());
        LatLng markLl=poiDetailResult.getLocation();
        BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.ic_place_red_48dp);
        LocationInfo info=new LocationInfo();
        info.setLatitude(markLl.latitude);
        info.setLongtitude(markLl.longitude);
        info.setName(poiDetailResult.getName());
        String uid=poiDetailResult.getUid();
        String type=poiDetailResult.getType();
        addOverlays(info,bitmap);
        showMakerInfo(poiDetailResult,uid,type);
//        Point point=baiduMap.getProjection().toScreenLocation(markLl);
//        mapMakerLayout.setVisibility(View.VISIBLE);
//        mapMakerLayout=(LinearLayout) rootView.findViewById(R.id.map_marker_layout);
//        refreshMakerPosition(point);
//        baiduMap.setOnMarkerDragListener(this);
//        Intent intent = new Intent(getActivity(), PlaceDetaiWebView.class);
//        intent.putExtra("url", poiDetailResult.getDetailUrl());
//        Log.i(TAG,"url:"+poiDetailResult.getDetailUrl());
//        startActivity(intent);
    }

    /**
     * 显示详细信息弹窗
     * @param uid
     * @param type
     */
    private void showMakerInfo(PoiDetailResult poiResult, final String uid, final String type) {
        final LatLng markLl=poiResult.getLocation();
        final RelativeLayout layout=(RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.map_maker_layout,null);
        final ImageView imageView=(ImageView)layout.findViewById(R.id.map_marker_image);
        TextView name=(TextView) layout.findViewById(R.id.map_marker_name);
        TextView address=(TextView)layout.findViewById(R.id.map_marker_address);
        ImageView close=(ImageView)layout.findViewById(R.id.map_marker_closeIcon);
        ImageView go=(ImageView)layout.findViewById(R.id.map_marker_goIcon);
        close.setOnClickListener(this);
        go.setOnClickListener(this);
        name.setText(poiResult.getName());
        address.setText(poiResult.getAddress());
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                HttpParse parse=new HttpParse();
                String imageUrl=parse.getBaiduImage(getActivity().getApplicationContext(),uid,type);
                subscriber.onNext(imageUrl);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG,s);
                Glide.with(getContext()).load(s).placeholder(R.drawable.loadingbg).error(R.drawable.ic_broken_image_black_48dp).into(imageView);
                if(s!=null){

                }else {
                    Log.i(TAG,"此marker无图片");
                }

            }
        });
        InfoWindow infoWindow=new InfoWindow(layout,markLl,-100);
        baiduMap.showInfoWindow(infoWindow);
    }


//    Point point=baiduMap.getProjection().toScreenLocation(latLng);

    private void addOverlays(LocationInfo info, BitmapDescriptor bitmap) {
        baiduMap.clear();
        LatLng latLng=new LatLng(info.getLatitude(),info.getLongtitude());
        Marker maker=null;
        OverlayOptions options;
        options=new MarkerOptions().position(latLng)
                .draggable(true)
                .icon(bitmap)
                .zIndex(5).animateType(MarkerOptions.MarkerAnimateType.grow);
        maker=(Marker) baiduMap.addOverlay(options);
        Bundle bundle=new Bundle();
        bundle.putSerializable("info",info);
        maker.setExtraInfo(bundle);
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
    public void onPause() {
        super.onPause();
        mapView.onPause();
        orientationListener.stop();
        if(fabOpened)
            closeMenu(journeyFab);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!locClient.isStarted())
            locClient.start();
        orientationListener.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.journey_day_fab:
//                Log.i(TAG,"监听");
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
                break;
            case R.id.map_my_location:
                centerToMyLocation();
                break;
            case R.id.map_marker_closeIcon:
                baiduMap.hideInfoWindow();
                break;
            case R.id.map_marker_goIcon:
                go();
                break;

        }
    }

    private void go() {
        Log.i(TAG, "开始路径规划");
        PlanNode st = PlanNode.withLocation(latLng);
        PlanNode ed = PlanNode.withLocation(currentPoiResutl.getLocation());
        routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(st).to(ed));
        Toast.makeText(getActivity(),"开始路径规划,到"+currentPoiResutl.getName(),Toast.LENGTH_SHORT).show();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mRecyclerview.scrollToPosition(0);
        NaviParaOption option=new NaviParaOption();
        option.startPoint(latLng);
        option.endPoint(currentPoiResutl.getLocation());
        option.startName("从这开始");
        option.endName("到这里结束");
        try {
            BaiduMapNavigation.openBaiduMapBikeNavi(option,getActivity());
        }catch (Exception e){
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("您尚未安装百度地图app或app版本过低，请手动下载最新版本");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
        }
    }

    /**
     * 定位到我的位置
     */
    private void centerToMyLocation() {
        if (latLng!=null){
            MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(msu);
        }else {
            Toast.makeText(getActivity(),"定位中请稍后",Toast.LENGTH_SHORT).show();
        }
    }

    private void centerToPoi(LatLng latLng) {
        if (latLng!=null){
            MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(msu);
        }
    }

    private void openMenu(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",0,-135);
        animator.setDuration(600);
        animator.start();
        final View hview = view;
        menuHideTv.setVisibility(View.VISIBLE);
        menuHideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu(journeyFab);
            }
        });
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,1.0f);
        AlphaAnimation alphaAnimation2=new AlphaAnimation(0.0f,1.0f);
        alphaAnimation2.setDuration(400);
        alphaAnimation2.setFillAfter(true);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        TranslateAnimation translateAnimation=new TranslateAnimation(400,0,0,0);
        translateAnimation.setDuration(400);
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation2);

        Animator revealAnim= ViewAnimationUtils.createCircularReveal(menuHideTv,(int)journeyFab.getX()+journeyFab.getWidth()/2,(int)journeyFab.getY()+journeyFab.getHeight()/2,journeyFab.getWidth(),windowHeight);
        revealAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuHideTv.setAlpha(1.0f);
                Log.i(TAG,"动画开始"+menuHideTv.getWidth()+" "+menuHideTv.getHeight());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG,"动画结束");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i(TAG,"动画取消");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i(TAG,"动画重复");
            }
        });
        revealAnim.setDuration(600);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.start();

        menuLayout.startAnimation(animationSet);

        menuLayout.setVisibility(View.VISIBLE);
        menuDayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravleDay travleDay=new TravleDay();
                travleDay.setDayNum(currentPlan.getTravleDays().size()+1);
                travleDay.setMemo("备注");
                currentPlan.getTravleDays().add(travleDay);
                mRecyclerview.getAdapter().notifyItemChanged(currentPlan.getTravleDays().size()-1);
                mRecyclerview.smoothScrollToPosition(currentPlan.getTravleDays().size());
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                closeMenu(journeyFab);

            }
        });
        menuAddScenicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ScenicActivity.class);
                intent.putExtra(ScenicActivity.EXTRA_CITY,currentPlan.getCity().getCityName());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                closeMenu(journeyFab);
            }
        });
        fabOpened=true;
    }

    private void closeMenu(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",-135,0);
        animator.setDuration(600);
        animator.start();
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        Animator revealAnim= ViewAnimationUtils.createCircularReveal(menuHideTv,(int)journeyFab.getX()+journeyFab.getWidth()/2,(int)journeyFab.getY()+journeyFab.getHeight()/2,windowHeight,journeyFab.getWidth()/2);
        revealAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuHideTv.setAlpha(1.0f);
                Log.i(TAG,"动画开始"+menuHideTv.getWidth()+" "+menuHideTv.getHeight());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG,"动画结束");
                menuHideTv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i(TAG,"动画取消");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i(TAG,"动画重复");
            }
        });
        revealAnim.setDuration(600);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.start();
        TranslateAnimation translateAnimation=new TranslateAnimation(0,400,0,0);
        translateAnimation.setDuration(600);
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        menuLayout.startAnimation(animationSet);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menuHideTv.setAlpha(0.0f);
                menuLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
                currentPlan=travlePlan;
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
                if(holder.detail.getTag() instanceof  TextWatcher){
                    holder.detail.removeTextChangedListener((TextWatcher) holder.detail.getTag());
                }
                holder.detail.setText(travleDay.getMemo());
                TextWatcher textWatcher=new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.i(TAG,"beforeTextChanged"+s);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.i(TAG,"onTextChanged:"+s);
                        travleDay.setMemo(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.i(TAG,"afterChanged:"+s);
                    }
                };

                holder.detail.addTextChangedListener(textWatcher);
                holder.detail.setTag(textWatcher);
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
                holder.scenicRecycler.setLayoutManager(new LinearLayoutManager(getActivity()){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
                holder.scenicRecycler.setAdapter(new scenicAdapter(travleDay));
            }else if(viewholder instanceof HeaderHolder){
                HeaderHolder headerHolder=(HeaderHolder)viewholder;
            }else if(viewholder instanceof FooterHolder){
                FooterHolder footerHolder=(FooterHolder)viewholder;
                footerHolder.moreLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TravleDay travleDay=new TravleDay();
                        travleDay.setDayNum(travlePlan.getTravleDays().size()+1);
                        travleDay.setMemo("");
                        travlePlan.getTravleDays().add(travleDay);
                        mRecyclerview.getAdapter().notifyItemChanged(travlePlan.getTravleDays().size());
                        mRecyclerview.smoothScrollToPosition(travlePlan.getTravleDays().size()-1);
                    }
                });
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
            RelativeLayout moreLayout;
            public FooterHolder(View itemView) {
                super(itemView);
                moreLayout=(RelativeLayout)itemView.findViewById(R.id.journey_recy_more_layout);
            }
        }

    }

    private class MyBottomSheetCallback extends BottomSheetBehavior.BottomSheetCallback{
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
                try {
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
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
//                Log.i(TAG,"slideOffset:"+slideOffset);
            slideUIUpadte(slideOffset);
        }

        private void slideUIUpadte(float slideOffset) {
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
                if(searchRecycler.getVisibility()==View.VISIBLE){
                    searchRecycler.setTranslationY(-slideOffset*200);
                }
            }
            if(slideOffset<=0){
                searchView.setTranslationY(slideOffset*160);
                if(searchRecycler.getVisibility()==View.VISIBLE){
                    searchRecycler.setTranslationY(slideOffset*160);
                }
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
    }

    private class MyGetRoutePlanResultListener implements OnGetRoutePlanResultListener {
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateInfowindow(PoiInfo poiInfo) {
        Log.i(TAG,"更新infowindow");
        mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poiInfo.uid));
        searchRecycler.setVisibility(View.GONE);
        centerToPoi(poiInfo.location);
    }

}
