package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.example.coderqiang.followme.View.ObservableScrollView;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

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

public class ScenicDetailActivity extends SwipeBackActivity implements View.OnClickListener,ObservableScrollView.ScrollViewListener{
    private static final String TAG = "ScenicDetailActivity";
    public static final String EXTRA_SCENIC="Scenic";
    public static final String EXTRA_URL="url";
    public static final String EXTRA_SCENIC_SER="Scenicser";
    private Context context;
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
    @Bind(R.id.scenic_detail_name_header)
    TextView nameHeaderTv;
    @Bind(R.id.scenic_detail_name_right)
    TextView nameRightTv;
    @Bind(R.id.scenic_detail_mark)
    TextView markTv;
    @Bind(R.id.scenic_detail_range)
    TextView rangeTv;
    @Bind(R.id.scenic_detail_scroll)
    ObservableScrollView scrollView;
    @Bind(R.id.scenic_detail_tkRefresh)
    TwinklingRefreshLayout twinklingRefreshLayout;
    @Bind(R.id.scenic_detail_toolbar_bg)
    TextView toolbarBg;
    @Bind(R.id.scenic_detail_layout)
    RelativeLayout relativeLayout;
    @Bind(R.id.show_gallery)
    RelativeLayout galleryLayout;
    List<android.support.v4.app.Fragment> fragments;
    PagerAdapter mAdapter;
    Scenicspot scenicspot;
    int scenicPositon;
    boolean isComment=false;
    boolean isEnter=true;
    public  boolean canBack=false;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenic_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        scenicspot=(Scenicspot)getIntent().getSerializableExtra(EXTRA_SCENIC_SER);
        url=getIntent().getStringExtra(EXTRA_URL);
//        Log.i(TAG,"序列化后的Name"+scenicspot.getScenicName());
//        Log.i(TAG, "position" + scenicPositon);
//        scenicspot = ScenicspotLab.get(getApplicationContext()).getScenicspots().get(scenicPositon);
        context=this;
        Log.i(TAG, "name:"+scenicspot.getScenicName()+" 简介:"+scenicspot.getIntroduction()+" comment:"+scenicspot.getComments().size());
        ButterKnife.bind(this);
        initData();
        this.setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                Log.i(TAG,"共享动画结束");
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                if (isEnter) {
//                    initViewPager();
                    initTabLayout();
                    isEnter=false;
                }

            }


        });
    }



    private void initData(){
        rangeTv.setText(scenicspot.getManyA());
        markTv.setText(scenicspot.getMark());
        nameRightTv.setText(scenicspot.getScenicName());
        backButton.setOnClickListener(this);
        toolbarBg.setAlpha(0);
        nameHeaderTv.setText(scenicspot.getScenicName());
        fab.setOnClickListener(this);
        Glide.with(context).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(false).into(detailImageView);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
//                HttpParse httpParse=new HttpParse();
//                httpParse.getScenicDetail(context.getApplicationContext(), scenicspot);
                subscriber.onNext("");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                initViewPager();
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String str) {

//                Glide.with(context).load(scenicspot.getImgUrls().get(0).getBigImgUrl()).transform(new BlurTransformation2(context)).into(detailImageViewBg);

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
                overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
            }
        });
        appBarLayout.setOnClickListener(new View.OnClickListener() {
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
        twinklingRefreshLayout.setPureScrollModeOn(true);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {
//                Log.i(TAG, "onPullingDown" + fraction);
                detailImageView.setTranslationY(fraction*200);
                detailImageView.setScaleY(1+fraction);
                detailImageView.setScaleX(1+fraction);
                super.onPullingDown(refreshLayout, fraction);
            }

            @Override
            public void onPullingUp(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullingUp(refreshLayout, fraction);
            }

            @Override
            public void onPullDownReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
//                Log.i(TAG, "onPullDownReleasing" + fraction);
                detailImageView.setTranslationY(fraction*250);
                detailImageView.setScaleY(1+fraction);
                detailImageView.setScaleX(1+fraction);
                super.onPullDownReleasing(refreshLayout, fraction);
            }

            @Override
            public void onPullUpReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
//                Log.i(TAG, "onPullUpReleasing" + fraction);
                super.onPullUpReleasing(refreshLayout, fraction);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if (isComment){

                }
                super.onRefresh(refreshLayout);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }

            @Override
            public void onFinishRefresh() {
                super.onFinishRefresh();
            }

            @Override
            public void onFinishLoadMore() {
                super.onFinishLoadMore();
            }
        });
        scrollView.setScrollViewListener(this);
    }
    private void initTabLayout() {
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewPager);
    }


    private void initViewPager() {
        fragments = new ArrayList<Fragment>();
        fragments.add(ScenicDetailFragment.newInstance(scenicspot,viewPager));
        fragments.add(ScenicCommentsFragment.newInstance(scenicspot,viewPager));
//        Log.e(TAG, "Fragment大小"+fragments.size());
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
                    isComment=true;
                }else if(currentPosition==0){
                    if(positionOffset==0&&shouldReset){
                        viewPager.resetHeight(0);
                        shouldReset=false;
                        Log.i(TAG,"重置高度");
                    }
                    isComment=false;
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
//        Animation translate=new TranslateAnimation(0,0,300,0);
//        translate.setDuration(500);
//        translate.setFillAfter(true);
//        viewPager.setAnimation(translate);
    }

    @Override
    public void onBackPressed() {
        if(canBack){
//            Animation translate=new TranslateAnimation(0,0,0,300);
//            translate.setDuration(500);
//            translate.setFillAfter(true);
//            viewPager.setAnimation(translate);
//            mTabLayout.setAnimation(translate);
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scenic_detail_back_button:
                super.onBackPressed();
                break;
            case R.id.scenic_detail_fab:
                ScenicspotLab.get(getApplicationContext()).getCollectionspots().add(scenicspot);
                Toast.makeText(context,"添加收藏成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//        Log.i(TAG,"y:"+y);
        if(y<550){
            float alpha = y*1.0f / 500;
            fab.setScaleX(1-alpha/2);
            fab.setScaleY(1-alpha/2);
            galleryLayout.setTranslationY(-alpha*200);
            toolbarBg.setAlpha(alpha);
        }else {
            toolbarBg.setAlpha(1.0f);
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