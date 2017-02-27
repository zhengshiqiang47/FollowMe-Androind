package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.SetStatusColor;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/2/14.
 */

public class TestActivity extends Activity {
    private static final String TAG="MainActivity";
    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.enter)
    Button enterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
//        Observer<String> observer=new Observer<String>() {
//            @Override
//            public void onCompleted() {
//                Log.i (TAG, "On Completed");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i(TAG, "On Error");
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.i(TAG, "Next"+s);
//            }
//        };
//        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello");
//                subscriber.onNext("Hi");
//                subscriber.onNext("Aloha");
//                subscriber.onCompleted();
//            }
//        });
//        observable.subscribe(observer);
//        final String[] names = {"name1", "name2", "name3"};
//        Observable.from(names)
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        Log.i(TAG, s);
//                    }
//                });
//        final int drawableRes=R.drawable.bird;
//        Observable.create(new Observable.OnSubscribe<Drawable>() {
//            @Override
//            public void call(Subscriber<? super Drawable> subscriber) {
//                Drawable drawable = getTheme().getDrawable(drawableRes);
//                subscriber.onNext(drawable);
//                subscriber.onCompleted();
//            }
//        })
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Observer<Drawable>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNext(Drawable drawable) {
//                if(mImageView==null) Log.i(TAG, "NULL");
//                mImageView.setImageDrawable(drawable);
//                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
//            }
//        });
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),TransitionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
            }
        });
        SetStatusColor.MIUISetStatusBarLightMode(getWindow(), true);
    }
}
