package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Service.LocationService;
import com.example.coderqiang.followme.Util.HttpParse;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
//import com.tencent.TIMCallBack;
//import com.tencent.TIMManager;
//import com.tencent.TIMUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by CoderQiang on 2016/11/26.
 */

public class LoginActivity extends Activity {
    private static final String TAG="LoginActivity";
    @Bind(R.id.login_name_edit)
    EditText name_edit;
    @Bind(R.id.login_passwd_edit)
    EditText passwd_edit;
    @Bind(R.id.login_btn)
    TextView loginButton;

    LocationClient locClient;
    BDLocationListener locListener;
    LocationService locationService;
    BDLocation bdLocation;

    Activity context;

    String num="zhengshiqiang";
    String pwd="zsqqq1996424";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_2);

        context=this;
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
    }

    private void initView(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

        locClient = new LocationClient(getApplicationContext());
        locListener=new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyLocationData locationData=new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(100)
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                MyLocation myLocation=MyLocation.getMyLocation(getApplicationContext());
                myLocation.setCityName(location.getCity());
                myLocation.setLatitute(location.getLatitude());
                myLocation.setLongtitute(location.getLongitude());
                myLocation.setHasLocation(true);
                bdLocation=location;

            }
        };
        locClient.registerLocationListener(locListener);
        initLocation();
        locClient.start();
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                HttpParse httpParse=new HttpParse();
                httpParse.getAllCityId(getApplicationContext());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });

    }

    private void loginAccount(){
        EMClient.getInstance().login(num, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.i(TAG,"登录成功");
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG,"登录失败"+i+" "+s);
            }
            @Override
            public void onProgress(int i, String s) {

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
}
