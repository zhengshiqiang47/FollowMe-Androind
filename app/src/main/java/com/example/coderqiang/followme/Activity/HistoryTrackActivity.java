package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.T;
import com.example.coderqiang.followme.Model.HistoryTrackData;
import com.example.coderqiang.followme.Model.RealtimeTrackData;
import com.example.coderqiang.followme.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/3/7.
 */

public class HistoryTrackActivity extends Activity {
    private static final String TAG = "HistoryTrackAcivity";

    @Bind(R.id.historytrack_mapview)
    MapView historytrackMapview;
    @Bind(R.id.historytrack_query_button)
    Button historytrackQueryButton;

    BaiduMap baiduMap;
    BitmapDescriptor myLocationIconBD;
    BitmapDescriptor startIcon;
    BitmapDescriptor endIcon;
    private MapStatusUpdate msUpdate;
    private static PolylineOptions polyline = null;
    private static OverlayOptions overlay;
    private OverlayOptions startPointOverlay=null;
    private OverlayOptions endPointOverlay=null;

    LBSTraceClient client ;
    LatLng startLatlng;
    LatLng endLatlng;
    long serviceId=135358;
    String entityName = "mycar";
    int traceType=2;
    int simpleReturn=0;
    int isProcessed=1;
    String processOption="need_denoise=1,need_vacuate=1,need_mapmatch=0";
    int startTime;
    int endTime;
    int pageSize=1000;
    int pageIndex=1;
    List<LatLng> pointList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historytrack);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        baiduMap = historytrackMapview.getMap();
        try{
            startTime=Integer.parseInt(new String(System.currentTimeMillis()+"").substring(0,10))-5*60*60;
            endTime=Integer.parseInt(new String(System.currentTimeMillis()+"").substring(0,10));
            Log.i(TAG, "start: " + startTime + " end" +  endTime);
            client = new LBSTraceClient(getApplicationContext());
            myLocationIconBD= BitmapDescriptorFactory.fromResource(R.mipmap.ic_navigation_blue_36dp);
            startIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_startpoint);
            endIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_endpoint);
            historytrackQueryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queryHistroyTrack(pageIndex);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void queryHistroyTrack(int pageIndex) {

        OnTrackListener trackListener = new OnTrackListener() {

            @Override
            public void onQueryHistoryTrackCallback(String s) {
                Log.i(TAG, s.toString());
                StringBuilder builder=new StringBuilder(s);
                parseData(builder);
            }

            @Override
            public void onRequestFailedCallback(String s) {
                Log.i(TAG, "轨迹查询失败");
            }
        };
        client.queryHistoryTrack(serviceId,entityName,simpleReturn,isProcessed,processOption,startTime,endTime,pageSize,pageIndex,trackListener);
    }

    private void parseData(StringBuilder json) {
        try{
            Gson gson=new Gson();
            HistoryTrackData historyTrackData = gson.fromJson(json.toString(), HistoryTrackData.class);
            if(historyTrackData != null && historyTrackData.getStatus() ==0){
                List<HistoryTrackData.PointsBean> points=historyTrackData.getPoints();
                Log.i(TAG, "size:" + points.size());
                //第一个点
                HistoryTrackData.EndPointBean end_point=historyTrackData.getEnd_point();
                endLatlng = new LatLng(end_point.getLatitude(), end_point.getLongitude());
                HistoryTrackData.StartPointBean start_point=historyTrackData.getStart_point();
                startLatlng = new LatLng(start_point.getLatitude(), start_point.getLongitude());
                HistoryTrackData.StartPointBean firstpoint=historyTrackData.getStart_point();
                LatLng firstLatlng=new LatLng(firstpoint.getLatitude(),firstpoint.getLongitude());
                for (HistoryTrackData.PointsBean point : points) {
                    LatLng latLng =new LatLng(point.getLocation().get(1),point.getLocation().get(0));
                    if(latLng != null){
                        pointList.add(latLng);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "当前无轨迹点", Toast.LENGTH_LONG).show();
                    }
                }
                drawRealtimePoint(firstLatlng);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 画出实时线路点
     * @param point
     */
    private void drawRealtimePoint(LatLng point){
        baiduMap.clear();
        MapStatus mapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        msUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
//        overlay = new MarkerOptions().position(point)
//                .icon(myLocationIconBD).zIndex(9).draggable(true);

        startPointOverlay = new MarkerOptions().position(startLatlng).icon(startIcon).zIndex(9).draggable(true);
        endPointOverlay = new MarkerOptions().position(endLatlng).icon(endIcon).zIndex(9).draggable(true);
        if(pointList.size() >= 2  && pointList.size() <= 1000){
            polyline = new PolylineOptions().width(10).color(getResources().getColor(R.color.tencent_blue)).points(pointList);
        }
        addMarker();
    }

    private void addMarker(){

        if(msUpdate != null){
            baiduMap.setMapStatus(msUpdate);
        }

        if(polyline != null){
            baiduMap.addOverlay(polyline);
            baiduMap.addOverlay(startPointOverlay);
            baiduMap.addOverlay(endPointOverlay);
        }

        if(overlay != null){
            baiduMap.addOverlay(overlay);
        }

    }
}
