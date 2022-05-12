package com.example.coderqiang.followme.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Service.LocationService;
import com.example.coderqiang.followme.Util.GetPermission;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.Util.ServerUtil;
import com.example.coderqiang.followme.Util.UserUtil;
import com.example.coderqiang.followme.View.JellyInterpolator;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
//import com.tencent.TIMCallBack;
//import com.tencent.TIMManager;
//import com.tencent.TIMUser;

import org.greenrobot.eventbus.EventBus;

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
    private static final int REQUEST_SIGNUP=1;
    @Bind(R.id.login_name_edit)
    EditText name_edit;
    @Bind(R.id.login_passwd_edit)
    EditText passwd_edit;
    @Bind(R.id.login_btn)
    TextView loginButton;
    @Bind(R.id.sign_up_btn)
    TextView signUpButton;
    @Bind(R.id.input_layout)
    View mInputLayout;
    @Bind(R.id.layout_progress)
    View progress;
    Activity context;

    LocationClient locClient;
    BDLocationListener locListener;
    LocationService locationService;
    BDLocation bdLocation;
    BDLocationListener bdLocationListener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData locationData=new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            MyLocation myLocation=MyLocation.getMyLocation(getApplicationContext());
            if (location.getCity()!=null){
                myLocation.setHasLocation(true);
                myLocation.setCityName(location.getCity());
                myLocation.setLatitute(location.getLatitude());
                myLocation.setLongtitute(location.getLongitude());
                Log.i(TAG, "定位成功" + myLocation.getCityName());
                if(isFirstGet){
                    CityLab.get(getApplicationContext()).setCurrentCity(CityLab.get(getApplicationContext()).isContain(location.getCity()));
                    EventBus.getDefault().post("定位改变");
                    MyLocation.getMyLocation(getApplicationContext()).setBdLocation(location);
                    isFirstGet=false;
                }

            }else {
                Log.i(TAG,"定位中");
                myLocation.setHasLocation(false);
                CityLab.get(getApplicationContext()).setCurrentCity(CityLab.get(getApplicationContext()).isContain("杭州"));
            }
            bdLocation=location;

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_2);
        context=this;
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        GetPermission.getAllPermission(this);
        initView();
    }

    int width;
    int height;
    boolean isFirstGet=true;

    private void initView(){
        width=loginButton.getWidth();
        height=mInputLayout.getHeight();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_edit.getText().toString().length() >= 1 && passwd_edit.getText().toString().length() >= 1) {
                    loginAccount(name_edit.getText().toString(), passwd_edit.getText().toString());
                } else {
                    loginAccount("zhengshiqiang", "zsqqq1996424");
                }
                inputAnimator(mInputLayout,width,height);

            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivityForResult(intent,REQUEST_SIGNUP);
            }
        });
        locClient = new LocationClient(getApplicationContext());

        locListener=bdLocationListener;
        locClient.registerLocationListener(locListener);
        initLocation();
        locClient.start();
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                ServerUtil.getAllCity(getApplicationContext());
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

    private int flag=0;

    private void loginAccount(final String username, final String password){
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.i(TAG,"登录成功");
                flag++;
                if (flag==2){
                    Intent intent = new Intent(context, MainActivity.class);;
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context.getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                        }

                    });
                    User.get(getApplicationContext()).setName(username);
                    User.get(getApplicationContext()).setPassword(password);
                    onDestroy();
                }

            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG,"登录失败"+i+" "+s);
                final String res=s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context.getApplicationContext(),"登录失败"+res,Toast.LENGTH_LONG).show();
                        inputAnimatorCancle(mInputLayout,width,height);
                    }
                });

            }
            @Override
            public void onProgress(int i, String s) {

            }
        });

        Observable.create(new Observable.OnSubscribe<Object>() {

            @Override
            public void call(Subscriber<? super Object> subscriber) {
                boolean isLogin=UserUtil.login(getApplicationContext(),username,password);
                if(isLogin){
                    subscriber.onCompleted();
                }else{
                    subscriber.onNext(null);
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

            @Override
            public void onCompleted() {
                flag++;
                if (flag==2){
                    Intent intent = new Intent(context, MainActivity.class);;
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    Toast.makeText(context.getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                    User.get(getApplicationContext()).setName(username);
                    User.get(getApplicationContext()).setPassword(password);
                    onDestroy();
                }
            }

            @Override
            public void onError(Throwable e) {
                flag--;
            }

            @Override
            public void onNext(Object o) {
                Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT);
                inputAnimatorCancle(mInputLayout,width,height);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_SIGNUP){
                loginAccount(User.get(getApplicationContext()).getName(),User.get(getApplicationContext()).getPassword());
            }
        }
    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.05f);
        set.setDuration(700);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }


    private void inputAnimatorCancle(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(w, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 0.5f, 1.0f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.INVISIBLE);
                mInputLayout.setVisibility(View.VISIBLE);
                name_edit.setVisibility(View.VISIBLE);
                passwd_edit.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locClient.unRegisterLocationListener(bdLocationListener);
        locListener=null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
