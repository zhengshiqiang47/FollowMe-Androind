package com.example.coderqiang.followme.Activity;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.example.coderqiang.followme.Fragment.DynamicFragment;
import com.example.coderqiang.followme.Fragment.ScenicDetailFragment;
import com.example.coderqiang.followme.Fragment.ScenicFragment;
import com.example.coderqiang.followme.Fragment.SquarFragment;
import com.example.coderqiang.followme.Fragment.TestFragment;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2016/11/10.
 */

public class ScenicDetailActivity extends FragmentActivity {
    private static final String TAG = "ScenicDetailActivity";
    public static final String EXTRA_SCENIC="Scenic";
    private Context context;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.scenic_detail_tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.scenic_detail_viewpager)
    ViewPager viewPager;
    @Bind(R.id.scenic_detail_img)
    ImageView detailImageView;
    @Bind(R.id.scenic_detail_name_header)
    TextView nameHeaderTv;
    @Bind(R.id.scenic_detail_name_right)
    TextView nameRightTv;
    List<android.support.v4.app.Fragment> fragments;
    PagerAdapter mAdapter;
    Scenicspot scenicspot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenic_detail);
        scenicspot =(Scenicspot) getIntent().getSerializableExtra(EXTRA_SCENIC);
        context=this;
        Log.i(TAG, scenicspot.getScenicName());
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initData();
    }

    private void initData(){
        nameRightTv.setText(scenicspot.getScenicName());
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                HttpParse httpParse=new HttpParse();
                httpParse.getScenicDetail(context,scenicspot);
                subscriber.onNext("");
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
            public void onNext(String str) {
                Glide.with(context).load(scenicspot.getImgUrls().get(0)).centerCrop().into(detailImageView);
                initViewPager();
                initTabLayout();
            }
        });
    }
    private void initTabLayout() {
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewPager);
    }

    private void initViewPager() {
        fragments = new ArrayList<Fragment>();
        fragments.add(ScenicDetailFragment.newInstance(scenicspot));
        fragments.add(ScenicDetailFragment.newInstance(scenicspot));
        mAdapter = new ScenicDetailActivity.FragAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
    }

    class FragAdapter extends FragmentPagerAdapter {
        private List<android.support.v4.app.Fragment> fragments;

        public FragAdapter(android.support.v4.app.FragmentManager fm, List<android.support.v4.app.Fragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }



        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "简介";
            else if (position == 1) return "评论";
            else return "其它";
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

    private class getDetail extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HttpParse httpParse=new HttpParse();
            httpParse.getScenicDetail(context,scenicspot);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Glide.with(context).load(scenicspot.getImgUrls().get(0)).centerCrop().into(detailImageView);
            initTabLayout();
            initViewPager();
        }
    }
}
