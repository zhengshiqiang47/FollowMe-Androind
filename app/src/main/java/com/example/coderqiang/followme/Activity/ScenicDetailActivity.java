package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coderqiang.followme.Fragment.ScenicCommentsFragment;
import com.example.coderqiang.followme.Fragment.ScenicDetailFragment;
import com.example.coderqiang.followme.Model.ScenicImg;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.View.BlurTransformation2;
import com.example.coderqiang.followme.View.CustomViewPager;
import com.example.coderqiang.followme.View.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2016/11/10.
 */

public class ScenicDetailActivity extends FragmentActivity implements View.OnClickListener{
    private static final String TAG = "ScenicDetailActivity";
    public static final String EXTRA_SCENIC="Scenic";
    private Context context;
    @Bind(R.id.scenic_detail_nestscroll)
    NestedScrollView nestedScrollView;
    @Bind(R.id.scenic_detail_fab)
    FloatingActionButton fab;
    @Bind(R.id.scenic_detail_back_button)
    Button backButton;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.scenic_detail_appbar)
    AppBarLayout appBarLayout;
    @Bind(R.id.scenic_detail_tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.scenic_detail_viewpager)
    CustomViewPager viewPager;
    @Bind(R.id.scenic_detail_img)
    ImageView detailImageView;
    @Bind(R.id.scenic_detail_img_bg)
    ImageView detailImageViewBg;
    @Bind(R.id.scenic_detail_name_header)
    TextView nameHeaderTv;
    @Bind(R.id.scenic_detail_name_right)
    TextView nameRightTv;
    @Bind(R.id.scenic_detail_mark)
    TextView markTv;
    @Bind(R.id.scenic_detail_range)
    TextView rangeTv;
    List<android.support.v4.app.Fragment> fragments;
    PagerAdapter mAdapter;
    Scenicspot scenicspot;
    int scenicPositon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenic_detail);
        scenicPositon =getIntent().getIntExtra(EXTRA_SCENIC,0);
        Log.i(TAG, "position" + scenicPositon);
        scenicspot = ScenicspotLab.get(getApplicationContext()).getScenicspots().get(scenicPositon);
        context=this;
        Log.i(TAG, scenicspot.getScenicName());
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initData();
    }

    private void initData(){
        rangeTv.setText(scenicspot.getManyA());
        markTv.setText(scenicspot.getMark());
        nameRightTv.setText(scenicspot.getScenicName());
        backButton.setOnClickListener(this);
        fab.setOnClickListener(this);
        nestedScrollView.setSmoothScrollingEnabled(true);
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
                Glide.with(context).load(scenicspot.getImgUrls().get(0).getBigImgUrl()).centerCrop().into(detailImageView);
                Glide.with(context).load(scenicspot.getImgUrls().get(0).getBigImgUrl()).transform(new BlurTransformation2(context)).into(detailImageViewBg);
                initViewPager();
                initTabLayout();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float offset=1.0f+verticalOffset*1.0f/(appBarLayout.getHeight()-toolbar.getHeight()-Dp2Px(getApplicationContext(),16));
//                Log.i(TAG, "offset" +offset );
                detailImageView.setAlpha(offset);
            }
        });
        detailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);
                ArrayList<String> images = new ArrayList<String>();
                for (ScenicImg scenicImg : scenicspot.getImgUrls()) {
                    images.add(scenicImg.getBigImgUrl());
                }
                intent.putStringArrayListExtra(PictureActivity.EXTRA_IMGURLS,images);
                intent.putExtra(PictureActivity.EXTRA_POSITION,0);
                intent.putExtra(PictureActivity.EXTRA_DESCRIPTION, scenicspot.getBrightPoint());
                startActivity(intent);
            }
        });
    }
    private void initTabLayout() {
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewPager);
    }

    private void initViewPager() {
        fragments = new ArrayList<Fragment>();
        fragments.add(ScenicDetailFragment.newInstance(scenicspot,viewPager));
        fragments.add(ScenicCommentsFragment.newInstance(scenicspot,viewPager));
        Log.e(TAG, "Fragment大小"+fragments.size());
        mAdapter = new ScenicDetailActivity.FragAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition=0;
            boolean shouldReset=true;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "position:" + position + " positionOffset" + positionOffset);
                if(currentPosition==1){
                    if(positionOffset==0&&shouldReset){
                        viewPager.resetHeight(1);
                        shouldReset=false;
                        Log.i(TAG,"重置高度");
                    }
                }else if(currentPosition==0){
                    if(positionOffset==0&&shouldReset){
                        viewPager.resetHeight(0);
                        shouldReset=false;
                        Log.i(TAG,"重置高度");
                    }
                }
            }
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                shouldReset=true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scenic_detail_back_button:
                super.onBackPressed();
                finish();
                break;
        }
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



    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}