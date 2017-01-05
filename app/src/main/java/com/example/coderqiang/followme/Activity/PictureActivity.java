package com.example.coderqiang.followme.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Model.ScenicImg;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.View.HackyViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by CoderQiang on 2016/12/3.
 */

public class PictureActivity extends SwipeBackActivity {
    public static final String TYPE_SCENIC="scenic";
    public static final String TYPE_COMMENT="comment";
    public static final String EXTRA_TYPE="type";
    public static final String EXTRA_POSITION="position";
    public static final String EXTRA_IMGURLS = "imageUrls";
    public static final String EXTRA_DESCRIPTION="description";

    //photoview里面的自定义的方法  重写了onInterceptTouchEvent  onTouchEvent来处理事件
    private HackyViewPager mViewPager;
    //显示页数和当前页数
    private TextView picture_iv_index;
    //返回按钮
    private ImageView picture_iv_back;
    private ImageView picture_iv_like;
    private Activity context;
    private ArrayList<String> imgUrls;
    private int postion=0;
    private String description;
    private boolean isHide=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        imgUrls = getIntent().getStringArrayListExtra(EXTRA_IMGURLS);
        postion = getIntent().getIntExtra(EXTRA_POSITION, 0);
        description = ""+getIntent().getStringExtra(EXTRA_DESCRIPTION);
        context=this;
        initView();
        initParams();
    }


    //初始化参数
    private void initParams() {
        picture_iv_index.setText("1/" + imgUrls.size() + "  " + description);
        // 绑定适配器
        mViewPager.setAdapter(new ViewPagerAdapter());
        //设置可以滑动监听(viewpager改变的时候调用)
        mViewPager.setOnPageChangeListener(new ViewPagerChangeListener());
        mViewPager.setCurrentItem(postion);
        picture_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    //初始化布局控件
    private void initView() {
        mViewPager = (HackyViewPager) findViewById(R.id.photo_vp);
        picture_iv_back = (ImageView) findViewById(R.id.picture_iv_back);
        picture_iv_index = (TextView) findViewById(R.id.picture_iv_index);
        picture_iv_like = (ImageView) findViewById(R.id.picture_like);
    }

    private void hideTv(){
        if(!isHide){
            AlphaAnimation alpha=new AlphaAnimation(1.0f,0.0f);
            alpha.setDuration(500);
            alpha.setFillAfter(true);
            picture_iv_index.startAnimation(alpha);
            picture_iv_like.startAnimation(alpha);
            isHide = true;
        }
    }

    private void showTv(){
        if(isHide){
            AlphaAnimation alpha=new AlphaAnimation(0.0f,1.0f);
            alpha.setDuration(500);
            alpha.setFillAfter(true);
            picture_iv_index.startAnimation(alpha);
            picture_iv_like.startAnimation(alpha);
            isHide=false;
        }
    }
    @Override
    public void onBackPressed() {
        System.gc();
        super.onBackPressed();
        overridePendingTransition(R.anim.back_slide_exit,R.anim.back_slide_enter);
    }

    @Override
    protected void onDestroy() {
        Log.i("PictureActivity","OnDestroy");
        picture_iv_back=null;
        picture_iv_like=null;
        context=null;
        imgUrls=null;
        context=null;
        setContentView(R.layout.view_null);
        super.onDestroy();
    }

    // 查看大图viewpager适配器
    private class ViewPagerAdapter extends PagerAdapter {
        ArrayList<String> imgUrl;
        public ViewPagerAdapter() {
            super();
        }

        @SuppressLint("InflateParams")
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = getLayoutInflater().inflate(R.layout.picture_item, null);
            PhotoView picture_iv_item = (PhotoView) view.findViewById(R.id.picture_item_photoview);

            picture_iv_item.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(isHide) showTv();
                    else hideTv();
                }

            });
            // 给imageview设置一个tag，保证异步加载图片时不会乱序
            // AsyncImageLoader.getInstance(NewsPictureActivity.this).loadBitmaps(view, picture_iv_item, ConstantsUtil.IMAGE_URL + dataList.get(position).url, LocalApplication.getInstance().screenW, 0);
            final WeakReference<PhotoView> photoViewWeakReference = new WeakReference<PhotoView>(picture_iv_item);
            PhotoView target=photoViewWeakReference.get();
            if (target != null)
                Glide.with(context.getApplicationContext()).load(imgUrls.get(position)).asBitmap().skipMemoryCache(true).into(target);
            //把view加载到父容器中
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return imgUrls.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }


    // viewpager切换监听器
    private class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            //设置文字
            picture_iv_index.setText((arg0 + 1) + "/" + imgUrls.size()+"    "+description);
        }

    }


}
