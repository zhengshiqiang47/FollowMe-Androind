package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.coderqiang.followme.Adapter.DynamicAdapter;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.ServerUtil;
import com.example.coderqiang.followme.Util.UserUtil;

import net.qiujuer.genius.graphics.Blur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/3/8.
 */

public class UserDetailInfoActivity extends Activity {
    public static final String EXTRA_USERID = "userid";
    private static final String TAG = "UserInfoDetailInfo";

    @Bind(R.id.userdetail_bg)
    ImageView userdetailBg;
    @Bind(R.id.userdetail_name)
    TextView userdetailName;
    @Bind(R.id.userdetail_signature)
    TextView userdetailSignature;
    @Bind(R.id.userinfo_concern)
    TextView userinfoConcern;
    @Bind(R.id.userinfo_follower)
    TextView userinfoFollower;
    @Bind(R.id.userinfo_travel)
    TextView userinfoTravel;
    @Bind(R.id.textView7)
    TextView textView7;
    @Bind(R.id.userdetail_top_layout)
    RelativeLayout userdetailTopLayout;
    @Bind(R.id.userdetail_avator)
    CircleImagview userdetailAvator;
    @Bind(R.id.userdetail_recycler)
    RecyclerView userdetailRecycler;
    @Bind(R.id.userdetail_id)
    TextView useridName;
    @Bind(R.id.userdetail_sex)
    ImageView userdetailSex;
    @Bind(R.id.userinfo_city)
    TextView userinfoCity;

    private Activity activity;
    private int userid;
    private FMUser user;
    private Bitmap avator;
    private ArrayList<Dynamic> dynamics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetailinfo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        ButterKnife.bind(this);
        activity = this;
        userid = getIntent().getIntExtra(EXTRA_USERID, 0);
        initDate();
    }

    private void initDate() {
        if (userid == 0) {
            super.onBackPressed();
        } else {
            Observable.create(new Observable.OnSubscribe<Object>() {

                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    user = UserUtil.getOtherUser(getApplicationContext(), userid);
                    subscriber.onCompleted();
                }

            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

                @Override
                public void onCompleted() {
                    initView();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {

                }
            });
        }
    }

    private void initView() {
        Log.i(TAG, "nickName" + user.getConcern());
        showDynamic();
        showTitleBg();
        try {
            userdetailName.setText(user.getNickName());
            useridName.setText(user.getUserName());
            userinfoConcern.setText(user.getConcern());
            userinfoFollower.setText(user.getFollower());
            userdetailSignature.setText(user.getSignature());
            userinfoTravel.setText(user.getTravle());
            userinfoCity.setText(user.getCity());
            if(user.getSex()==0){
                userdetailSex.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_man_white));
            }else {
                userdetailSex.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_woman_white));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showDynamic() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                Log.i("UserInfo", "开始获取动态");
                dynamics = ServerUtil.getDynamicMine(getApplicationContext(), user);
                Log.i("UserInfo", "dyanmicSize:" + dynamics.size());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

            @Override
            public void onCompleted() {
                userdetailRecycler.setLayoutManager(new LinearLayoutManager(activity));
                userdetailRecycler.setAdapter(new DynamicAdapter(activity, dynamics));
                userinfoTravel.setText(dynamics.size());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private void showTitleBg() {
        Glide.with(this).load("http://123.206.195.52:8080/day_30/upload/" + user.getUserName() + ".png").asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                avator = resource;
                userdetailAvator.setImageBitmap(avator);
                Bitmap bitmap = compressImage(resource);
                bitmap = Blur.onStackBlur(bitmap, 60);
                userdetailBg.setImageBitmap(bitmap);
            }
        });
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, options1);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
