package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.DateUtil;
import com.example.coderqiang.followme.Util.UserUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.Date;

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

public class EditUserInfoActivity extends Activity {
    private static final String TAG = "EditUserinfo";

    @Bind(R.id.edituserinfo_cancle)
    ImageView edituserinfoCancle;
    @Bind(R.id.edituserinfo_right)
    ImageView edituserinfoRight;
    @Bind(R.id.edituserinfo_title)
    LinearLayout edituserinfoTitle;
    @Bind(R.id.edituserinfo_nickname)
    MaterialEditText nickname;
    @Bind(R.id.edituserinfo_datePicker)
    DatePicker datePicker;
    @Bind(R.id.edituserinfo_birthday)
    TextView birthday;
    @Bind(R.id.edituserinfo_yes)
    Button yes;
    @Bind(R.id.edituserinfo_datePicker_layout)
    LinearLayout dateLayout;
    @Bind(R.id.edituserinfo_radiogroup)
    RadioGroup sexGroup;


    int selYear;int year;
    int selMon;int mon;
    int selDay;int day;
    int selectSex=0;
    FMUser fmUser;

    @Bind(R.id.edituserinfo_password)
    MaterialEditText edituserinfoPassword;
    @Bind(R.id.edituserinfo_phone)
    MaterialEditText edituserinfoPhone;
    @Bind(R.id.edituserinfo_email)
    MaterialEditText edituserinfoEmail;
    @Bind(R.id.edituserinfo_signature)
    MaterialEditText edituserinfoSignature;
    @Bind(R.id.radiogroup_man)
    RadioButton radiogroupMan;
    @Bind(R.id.radiogroup_woman)
    RadioButton radiogroupWoman;
    @Bind(R.id.edituserinfo_publicButton)
    Button publicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituserinfo);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.journey_green));
        initView();
    }

    private void initView() {
        fmUser=User.get(getApplicationContext()).getFmUser();
        selectSex=fmUser.getSex();
        nickname.setText(fmUser.getNickName());
        edituserinfoPassword.setText(fmUser.getPassword());
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nickname.validate("[0-9a-zA-Z]", "只能是数字或者字母");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(fmUser.getPhone()!=null){
            edituserinfoPhone.setText(fmUser.getPhone());
        }
        if (fmUser.getEmail() != null) {
            edituserinfoEmail.setText(fmUser.getEmail());
        }
        if (fmUser.getSignature() != null) {
            edituserinfoSignature.setText(fmUser.getSignature());
        }
        if(fmUser.getSex()==0){
            sexGroup.check(R.id.radiogroup_man);
        }else sexGroup.check(R.id.radiogroup_woman);
        final Calendar calendar = Calendar.getInstance();
        Long early=new Long("-2199254400");
        if(fmUser.getBirthDay()>early&&fmUser.getBirthDay()!=0){
            year=Integer.parseInt(DateUtil.getDateToStringYear(fmUser.getBirthDay()));
            mon=Integer.parseInt(DateUtil.getDateToStringMon(fmUser.getBirthDay()))-1;
            day = Integer.parseInt(DateUtil.getDateToStringDay(fmUser.getBirthDay()));
        }else {
            year = calendar.get(Calendar.YEAR);
            mon = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateLayout.setVisibility(View.GONE);
                birthday.setText(selYear + "年" + selMon + "月" + selDay + "日");
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateLayout.setVisibility(View.VISIBLE);
                AlphaAnimation alpha = new AlphaAnimation(0, 1.0f);
                alpha.setDuration(500);
                dateLayout.setAnimation(alpha);
                datePicker.init(year, mon, day, new DatePicker.OnDateChangedListener() {
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

        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radiogroup_man:
                        selectSex=0;
                        break;
                    case R.id.radiogroup_woman:
                        selectSex=1;
                        break;
                }
            }
        });

        publicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FMUser user= User.get(getApplicationContext()).getFmUser();
                String date=new String(selYear+"-"+selMon+"-"+selDay);
                Long time=DateUtil.getStringToDate(date);
                Log.i(TAG,"time"+time +" name:"+nickname.getText().toString()+" password"+edituserinfoPassword.getText().toString()+" sex"+selectSex+" phone"+edituserinfoPhone.getText().toString()+edituserinfoEmail.getText().toString()+edituserinfoSignature.getText().toString());
                user.setBirthDay(time);
                user.setPassword(edituserinfoPassword.getEditableText().toString());
                user.setEmail(edituserinfoEmail.getText().toString());
                user.setCity(CityLab.get(getApplicationContext()).getCurrentCity().getCityName());
                user.setNickName(nickname.getText().toString());
                user.setPhone(edituserinfoPhone.getText().toString());
                user.setEmail(edituserinfoEmail.getText().toString());
                user.setSex(selectSex);
                user.setSignature(edituserinfoSignature.getText().toString());
                Observable.create(new Observable.OnSubscribe<Boolean>() {

                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        boolean isTrue=UserUtil.updateUser(user);
                        subscriber.onNext(isTrue);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean bool) {
                        if(bool){
                            Toast.makeText(getApplicationContext(),"信息修改成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"信息修改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

}
