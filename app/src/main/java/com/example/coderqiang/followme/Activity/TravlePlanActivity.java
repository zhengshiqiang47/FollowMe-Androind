package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.TravelDay;
import com.example.coderqiang.followme.Model.TravelPlan;
import com.example.coderqiang.followme.Model.TravlePlanLab;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.DateUtil;
import com.example.coderqiang.followme.Util.ServerUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/3/9.
 */

public class TravlePlanActivity extends Activity {
    private static final String TAG = "TravlePlanActivity";

    @Bind(R.id.travlePlan_cancle)
    ImageView travlePlanCancle;
    @Bind(R.id.travlePlan_right)
    ImageView travlePlanRight;
    @Bind(R.id.travelPlan_title)
    LinearLayout travelPlanTitle;
    @Bind(R.id.travelPlan_name)
    MaterialEditText travelPlanName;
    @Bind(R.id.travelPlan_time)
    TextView travelPlanTime;
    @Bind(R.id.travelPlan_daycount)
    MaterialEditText travelPlanDaycount;
    @Bind(R.id.travelPlan_city)
    MaterialEditText travelPlanCity;
    @Bind(R.id.travelPlan_memo)
    MaterialEditText travelPlanMemo;
    @Bind(R.id.travlePlan_datePicker)
    DatePicker travlePlanDatePicker;
    @Bind(R.id.travlePlan_yes)
    Button travlePlanYes;
    @Bind(R.id.travlePlan_datePicker_layout)
    LinearLayout travlePlanDatePickerLayout;
    @Bind(R.id.edituserinfo_publicButton)
    Button edituserinfoPublicButton;

    int selYear;
    int year;
    int selMon;
    int mon;
    int selDay;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelplan);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.journey_green));
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        mon = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        travlePlanYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travlePlanDatePickerLayout.setVisibility(View.GONE);
                travelPlanTime.setText(selYear + "年" + selMon + "月" + selDay + "日");
            }
        });
        travelPlanTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travlePlanDatePickerLayout.setVisibility(View.VISIBLE);
                AlphaAnimation alpha = new AlphaAnimation(0, 1.0f);
                alpha.setDuration(500);
                travlePlanDatePickerLayout.setAnimation(alpha);
                travlePlanDatePicker.init(year, mon, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.i(TAG, "year" + year + " m:" + monthOfYear + " day:" + dayOfMonth);
                        selYear = year;
                        selMon = monthOfYear + 1;
                        selDay = dayOfMonth;

                    }
                });
            }
        });
        edituserinfoPublicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TravelPlan plan=new TravelPlan();
                int dayCount;
                try {
                   dayCount=Integer.parseInt(travelPlanDaycount.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"天数格式有误",Toast.LENGTH_SHORT).show();
                    return;
                }
                String cityname=travelPlanCity.getText().toString();
                Boolean exist=CityLab.get(getApplicationContext()).isExist(cityname);
                if(exist==false){
                    Toast.makeText(getApplicationContext(),"城市名不合法",Toast.LENGTH_SHORT).show();
                    return;
                }
                plan.setUserId(User.get(getApplicationContext()).getId());
                plan.setCityName(cityname);
                plan.setTravleName(travelPlanName.getText().toString());
                plan.setDayCount(dayCount);
                String date=new String(selYear+"-"+selMon+"-"+selDay);
                Long time= DateUtil.getStringToDate(date);
                plan.setTime(time);
                plan.setCityName(travelPlanCity.getText().toString());
                plan.setBeginMemo(travelPlanMemo.getText().toString());
                plan.setCity(CityLab.get(getApplicationContext()).isContain(plan.getCityName()));
                for (int i=0;i<plan.getDayCount();i++){
                    TravelDay travelDay =new TravelDay();
                    travelDay.setDayNum(i+1);
                    travelDay.setTime(plan.getTime()+24*60*60*1000);
                    plan.getTravelDays().add(travelDay);
                }
                TravlePlanLab.get(getApplicationContext()).setCurrentPlan(plan);
                TravlePlanLab.get(getApplicationContext()).getTravelPlans().add(plan);
                Observable.create(new Observable.OnSubscribe<Object>() {

                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        int id = ServerUtil.uploadTravelPlan(getApplicationContext(), plan);
                        if(id==0){
                            subscriber.onNext(null);
                        }else
                            subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

                    @Override
                    public void onCompleted() {
                        Toast.makeText(getApplicationContext(),"创建成功",Toast.LENGTH_SHORT).show();
                        TravlePlanLab.get(getApplication()).setCurrentPlan(plan);
                        EventBus.getDefault().post("plan");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Toast.makeText(getApplicationContext(),"上传失败，稍后重试",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
