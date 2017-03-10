package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LocationMode;
import com.baidu.trace.T;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.example.coderqiang.followme.Model.RealtimeTrackData;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;
import com.baidu.trace.OnGeoFenceListener;
import com.baidu.trace.OnEntityListener;
import com.example.coderqiang.followme.Service.MyOrientationListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderQiang on 2017/3/7.
 */

public class TraceActivity extends Activity {
    private static final String TAG="TraceActivity";

    @Bind(R.id.trace_mapview)
    MapView traceMapview;
    @Bind(R.id.trace_btn)
    Button traceBtn;

    BitmapDescriptor myLocationIconBD;
    BaiduMap map;

    int gatherInterval=3;
    int packInterval=30;

    long serviceId=135358;
    String entityName = "mycar";
    int traceType=2;
    Trace trace;
    LBSTraceClient client ;
    boolean isFirstGet=true;
    boolean isFirstLoc=true;
    private static OnStartTraceListener startTraceListener;
    private static List<LatLng> points = new ArrayList<>();
    MyOrientationListener orientationListener;
    private static OnEntityListener listener;
    MyLocationData myLocationData;
    private RefreshThread refreshThread;
    private MapStatusUpdate msUpdate;
    private static PolylineOptions polyline = null;
    private static OverlayOptions overlay;
    OnStopTraceListener stopTraceListener;

    float currentX;
    double curLat=0;
    double curLng=0;
    boolean isStrat=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_trace);
        ButterKnife.bind(this);
        initView();
        initTrace();

    }

    @Override
    protected void onStart() {
        super.onStart();
        orientationListener.start();
    }

    private void initTrace() {
        entityName= User.get(getApplicationContext()).getName();
        client= new LBSTraceClient(getApplicationContext());
        trace= new Trace(getApplicationContext(), serviceId, entityName, traceType);
        startTraceListener=new OnStartTraceListener() {
            @Override
            public void onTraceCallback(int i, String s) {
                Log.i(TAG,"onTraceCallback:"+s);
                if (i == 0 || i == 10006) {
                    startRefreshThread(true);
                }
            }

            @Override
            public void onTracePushCallback(byte b, String s) {

            }
        };
        int protocolType = 1;
        client.setInterval(gatherInterval, packInterval);
        client.setLocationMode(LocationMode.High_Accuracy);
        client.setProtocolType (protocolType);

    }

    private void initView() {
        traceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStrat){
                    client.startTrace(trace,startTraceListener);
                    Toast.makeText(getApplicationContext(),"开始记录轨迹",Toast.LENGTH_SHORT).show();
                    traceBtn.setText("停止记录");
                }else {
                    client.stopTrace(trace,stopTraceListener);
                    Toast.makeText(getApplicationContext(),"停止记录轨迹",Toast.LENGTH_SHORT).show();
                    traceBtn.setText("开始记录");
                }

            }
        });
        map=traceMapview.getMap();
        map.setMyLocationEnabled(true);
        myLocationIconBD= BitmapDescriptorFactory.fromResource(R.mipmap.ic_navigation_blue_36dp);
        listener=new OnEntityListener() {
            @Override
            public void onQueryEntityListCallback(String s) {
                Log.i(TAG,"onQueryEntityList:"+s);
                showRealtimeTrack(s);
            }

            @Override
            public void onReceiveLocation(TraceLocation traceLocation) {
                Log.i(TAG,"getLocation"+traceLocation.getLatitude());
                MyLocationData locationData=new MyLocationData.Builder()
                        .accuracy((float) traceLocation.getRadius())
                        .direction(currentX)
                        .latitude(traceLocation.getLatitude())
                        .longitude(traceLocation.getLongitude())
                        .build();
                myLocationData=locationData;
                curLat=traceLocation.getLatitude();
                curLng=traceLocation.getLongitude();
                map.setMyLocationData(locationData);
                if (isFirstGet&&traceLocation.getLatitude()!=0) {
                    Log.i(TAG, "setIcon");
                    MyLocationConfiguration config=new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,myLocationIconBD);
                    //设置自定义图标
                    map.setMyLocationConfigeration(config);
                    map.setMyLocationEnabled(true);

                    isFirstGet=false;
                }
                Log.i(TAG, "Lat:" + traceLocation.getLatitude() + " " + "Lng" + traceLocation.getLongitude());
                if (isFirstLoc&&traceLocation.getLatitude()!=0) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(traceLocation.getLatitude(),
                            traceLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }

            }

            @Override
            public void onRequestFailedCallback(String s) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "轨迹下载失败" + s, Toast.LENGTH_SHORT).show();
                Log.i(TAG,"onRequestFailed");
                Looper.loop();
            }
        };

//        traceBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // entity标识列表
//                String entityNames = "mycar";
//                //返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
//                int returnType = 0;
//                // 活跃时间
//                int activeTime = 0;
//                // 分页大小
//                int pageSize = 100;
//                // 分页索引
//                int pageIndex =1;
//                String columnKey="";
//                //查询entity
//                client.queryRealtimeLoc(serviceId,listener);
//                client.queryEntityList(serviceId, entityNames, columnKey, returnType, activeTime, pageSize, pageIndex, listener);
//            }
//        });

        orientationListener=new MyOrientationListener(this);
        orientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                currentX=x;
                if(myLocationData!=null){
                    MyLocationData locationData=new MyLocationData.Builder()
                            .accuracy(myLocationData.accuracy)
                            .direction(currentX)
                            .latitude(myLocationData.latitude)
                            .longitude(myLocationData.longitude)
                            .build();
                    map.setMyLocationData(locationData);
                }
            }
        });
    }





    @Override
    protected void onResume() {
        super.onResume();
        traceMapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        traceMapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        traceMapview.onDestroy();
        orientationListener.stop();
        stopTraceListener=new OnStopTraceListener() {
            @Override
            public void onStopTraceSuccess() {
               Log.i(TAG,"StopSuccess");
            }

            @Override
            public void onStopTraceFailed(int i, String s) {
                Log.i(TAG,"StopFailed");
            }
        };
        client.stopTrace(trace,stopTraceListener);
    }

    protected void showRealtimeTrack(String realtimeTrack){

        if(refreshThread == null || !refreshThread.refresh){
            return;
        }

        //数据以JSON形式存取
        RealtimeTrackData realtimeTrackData = new Gson().fromJson(realtimeTrack,RealtimeTrackData.class);

        if(realtimeTrackData != null && realtimeTrackData.getStatus() ==0){
            RealtimeTrackData.EntitiesBean.RealtimePointBean point=realtimeTrackData.getEntities().get(0).getRealtime_point();
            LatLng latLng =new LatLng(point.getLocation().get(1),point.getLocation().get(0));
            if(latLng != null){
                points.add(latLng);
                drawRealtimePoint(latLng);
            }
            else{
                Toast.makeText(getApplicationContext(), "当前无轨迹点", Toast.LENGTH_LONG).show();
            }

        }

    }

    /**
     * 画出实时线路点
     * @param point
     */
    private void drawRealtimePoint(LatLng point){

        map.clear();
        MapStatus mapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        msUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        overlay = new MarkerOptions().position(point)
                .icon(myLocationIconBD).zIndex(9).draggable(true);

        if(points.size() >= 2  && points.size() <= 1000){
            polyline = new PolylineOptions().width(10).color(Color.RED).points(points);
        }

        addMarker();

    }

    private void addMarker(){

        if(msUpdate != null){
            map.setMapStatus(msUpdate);
        }

        if(polyline != null){
            map.addOverlay(polyline);
        }

        if(overlay != null){
            map.addOverlay(overlay);
        }

    }

    /**
     * 启动刷新线程
     * @param isStart
     */
    private void startRefreshThread(boolean isStart){

        if(refreshThread == null){
            refreshThread = new RefreshThread();
        }

        refreshThread.refresh = isStart;

        if(isStart){
            if(!refreshThread.isAlive()){
                refreshThread.start();
            }
        }
        else{
            refreshThread = null;
        }

    }



    private class RefreshThread extends Thread{

        protected boolean refresh = true;

        public void run(){

            while(refresh){
                Log.i(TAG, "追踪中...");
                queryRealtimeTrack();
                try{
                    Thread.sleep(packInterval * 1000);
                }catch(InterruptedException e){
                    System.out.println("线程休眠失败");
                }
            }

        }
    }

    private void queryRealtimeTrack(){

        String entityName = this.entityName;
        String columnKey = "";
        int returnType = 0;
        int activeTime = 0;
        int pageSize = 10;
        int pageIndex = 1;

        this.client.queryEntityList(
                serviceId,
                entityName,
                columnKey,
                returnType,
                activeTime,
                pageSize,
                pageIndex,
                listener
        );

    }

}
