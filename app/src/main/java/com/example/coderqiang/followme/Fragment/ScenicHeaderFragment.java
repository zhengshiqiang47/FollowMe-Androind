package com.example.coderqiang.followme.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2016/11/10.
 */

public class ScenicHeaderFragment extends Fragment {
    private static final String TAG = "ScenicHeaderFragment";
    ImageView imageView;
    String res;
    Scenicspot scenicspot;
    Context context;
    boolean isFirst=true;
    public static ScenicHeaderFragment newInstance(Scenicspot scenicspot){
        ScenicHeaderFragment scenicHeaderFragment = new ScenicHeaderFragment();
        scenicHeaderFragment.scenicspot=scenicspot;
        return scenicHeaderFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scenic_recycler_header_item, container, false);
        imageView = (ImageView) v.findViewById(R.id.scenic_recycler_header_img);
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                HttpParse httpParse=new HttpParse();
                httpParse.getScenicDetail(getActivity(),scenicspot);
                subscriber.onNext(scenicspot.getImgUrls().get(0).getBigImgUrl());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "-->OnNext");
                Glide.with(context).load(s).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
            }
        });
        Log.i("ScenicHeaderFragment","创建view");
        return v;
    }
}
