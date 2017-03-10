package com.example.coderqiang.followme.Activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Fragment.ScenicFragment;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.View.CitySeletctDialog;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2016/12/30.
 */

public class ChinaActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.china_back)
    ImageView backIcon;
//    @Bind(R.id.china_twinkrefresh)
//    TwinklingRefreshLayout twinklingRefreshLayout;
    @Bind(R.id.china_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.china_progress_layout)
    RelativeLayout progressLayout;

    String selectProvinceName;
    ChinaActivity context;
    ArrayList<City> hotCities;
    ArrayList<City> currentCities;
    ArrayList<Scenicspot> hotScenicSpots;
    String currentProvince;
    ChinaAdapter adpter;
    int cityCount=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context=this;
        setContentView(R.layout.activity_china);
        ButterKnife.bind(this);
        initData();

    }

    private void initView(){
        hideProgress();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adpter=new ChinaAdapter();
        recyclerView.setAdapter(adpter);
//        twinklingRefreshLayout.setPureScrollModeOn(false);
        backIcon.setOnClickListener(this);

    }

    int count=0;

    private void initData(){
        currentProvince="浙江";
        currentCities = new ArrayList<City>();
        hotCities=new ArrayList<City>();
        hotScenicSpots= ScenicspotLab.get(getApplicationContext()).getHotScenicspots();
        hotCities.add(CityLab.get(context).isContain("大理"));
        hotCities.add(CityLab.get(context).isContain("厦门"));
        hotCities.add(CityLab.get(context).isContain("上海"));
        hotCities.add(CityLab.get(context).isContain("北京"));
        hotCities.add(CityLab.get(context).isContain("杭州"));
        hotCities.add(CityLab.get(context).isContain("拉萨"));
        hotCities.add(CityLab.get(context).isContain("哈尔滨"));
        hotCities.add(CityLab.get(context).isContain("三亚"));
        hotCities.add(CityLab.get(context).isContain("桂林"));
//        for (int i=0;i<hotCities.size();i++){
//            final City city=hotCities.get(i);
//            Observable.create(new Observable.OnSubscribe<Object>() {
//                @Override
//                public void call(Subscriber<? super Object> subscriber) {
////                    HttpParse httpParese=new HttpParse();
////                    httpParese.getPlace(city);
//                    subscriber.onCompleted();
//                }
//            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
//
//                @Override
//                public void onCompleted() {
//                    count++;
//                    if(count==hotCities.size()+currentCities.size())
//                        initView();
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(Object o) {
//
//                }
//            });
//        }
        ArrayList<String> cities=CityLab.get(context).getProviceCity(currentProvince);
        for (int i=0;i<cities.size();i++) {
            City city=CityLab.get(context).isContain(cities.get(i));
            currentCities.add(city);
        }
        initView();
//        for (int i=0;i<currentCities.size();i++){
//            final City city=currentCities.get(i);
//            Observable.create(new Observable.OnSubscribe<Object>() {
//                @Override
//                public void call(Subscriber<? super Object> subscriber) {
////                    HttpParse httpParese=new HttpParse();
////                    httpParese.getPlace(city);
//                    subscriber.onCompleted();
//                }
//            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
//
//                @Override
//                public void onCompleted() {
//                    count++;
//                    if(count==hotCities.size()+currentCities.size()) initView();
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(Object o) {
//
//                }
//            });
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.china_back:
                onBackPressed();
                return;
            case R.id.chongqing_layout:selectProvinceName="重庆";break;
            case R.id.shandong_layout:selectProvinceName="山东";break;
            case R.id.hunan_layout:selectProvinceName = "湖南";break;
            case R.id.zhejiang_layout:selectProvinceName="浙江";break;
            case R.id.xizang_layout:selectProvinceName="西藏";break;
            case R.id.hainan_layout:selectProvinceName = "海南";break;
            case R.id.yunnan_layout:selectProvinceName = "云南";break;
            default:break;
        }
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(v.getId());
        CitySeletctDialog dialog=new CitySeletctDialog(this,(int)relativeLayout.getX(),(int)relativeLayout.getY(),CityLab.get(getApplicationContext()).getProviceCity(selectProvinceName));
        View view=dialog.getCustomView();
        TextView title=(TextView)view.findViewById(R.id.city_select_dilog_title);
        title.setText(selectProvinceName);
        dialog.setDialogCallback(adpter);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }




    public class ChinaAdapter extends RecyclerView.Adapter implements  CitySeletctDialog.DialogCallback{

        public GridCityAdapter gridAdapter;
        TextView provinceTv;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==0)
                return new ItemHolder1(getLayoutInflater().inflate(R.layout.china_recycler_item_1, parent, false));
            else if(viewType==1)
                return new ItemHolder2(getLayoutInflater().inflate(R.layout.china_recycler_item_2_map, parent, false));
            else if(viewType==2)
                return new ItemHolder3(getLayoutInflater().inflate(R.layout.china_recycler_item_3_hot_city, parent, false));
            else if(viewType==3)
                return new ItemHolder4(getLayoutInflater().inflate(R.layout.china_recycler_item_4_province,parent,false));
            else if(viewType==4)
                return new ItemHolder5(getLayoutInflater().inflate(R.layout.china_recycler_item_5_hot_scenic,parent,false));
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof ItemHolder1){
                ItemHolder1 itemHolder1=(ItemHolder1)holder;
                Glide.with(context).load(R.drawable.hot_city_70).into(itemHolder1.topImage);

            }else if(holder instanceof  ItemHolder2){
                ItemHolder2 itemHolder2=(ItemHolder2)holder;
                itemHolder2.yunnan_Layout.setOnClickListener(context);
                itemHolder2.hainan_layout.setOnClickListener(context);
                itemHolder2.chongqing_layout.setOnClickListener(context);
                itemHolder2.shandong_layout.setOnClickListener(context);
                itemHolder2.hunan_layout.setOnClickListener(context);
                itemHolder2.zhejiang_layout.setOnClickListener(context);
                itemHolder2.xizang_layout.setOnClickListener(context);
            }else if (holder instanceof ItemHolder3){
                final ItemHolder3 itemHolder3=(ItemHolder3)holder;
                itemHolder3.banner.setPages(new CBViewHolderCreator<TopImageHolder>() {
                    @Override
                    public TopImageHolder createHolder() {
                        return new TopImageHolder();
                    }
                }, hotCities).setCanLoop(true);
                itemHolder3.banner.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        City city=hotCities.get(position);
                        Log.i("China","城市名:"+city.getCityName());
                        Intent intent=new Intent(context,ScenicActivity.class);
                        intent.putExtra(ScenicActivity.EXTRA_CITY,city.getCityName());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    }
                });
                itemHolder3.rightArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int current=itemHolder3.banner.getCurrentItem();
                        current=(current+1)%hotCities.size();
                        itemHolder3.banner.setcurrentitem(current);
                    }
                });
                itemHolder3.leftArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int current=itemHolder3.banner.getCurrentItem();
                        current=(current+hotCities.size()-1)%hotCities.size();
                        itemHolder3.banner.setcurrentitem(current);
                    }
                });
            }else if(holder instanceof ItemHolder4){
                ItemHolder4 itemHolder4=(ItemHolder4)holder;
                itemHolder4.provinceTv.setText(currentProvince);
                provinceTv=itemHolder4.provinceTv;
                itemHolder4.provinceTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView proTv=(TextView) findViewById(R.id.china_province_tv);
                        CitySeletctDialog dialog=new CitySeletctDialog(context,(int)proTv.getX(),(int)proTv.getY(),CityLab.get(context).getProvince());
                        View view=dialog.getCustomView();
                        TextView title=(TextView)view.findViewById(R.id.city_select_dilog_title);
                        title.setText(selectProvinceName);
                        dialog.setDialogCallback(adpter);
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(true);
                    }
                });
                itemHolder4.cityGridRecycler.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false));
                gridAdapter=new GridCityAdapter();
                itemHolder4.cityGridRecycler.setAdapter(gridAdapter);
                refreshCity();
            }else if(holder instanceof ItemHolder5){
                ItemHolder5 itemHolder5=(ItemHolder5)holder;
                itemHolder5.scenicRecycelr.setLayoutManager(new LinearLayoutManager(context));
                itemHolder5.scenicRecycelr.setAdapter(new HotScenicSpotAdapter());
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void refresh(String select, int t) {
            if(t==CitySeletctDialog.TYPE_PROVINCE){
                if (select != currentProvince) {
                    currentProvince=select;
                    provinceTv.setText(select);
                    refreshCity();
                }
            }else {
                Log.i("China","城市名:"+select);
                Intent intent=new Intent(context,ScenicActivity.class);
                intent.putExtra(ScenicActivity.EXTRA_CITY,select);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
            }

        }


        private void refreshCity(){
            if(cityCount>=0) cityCount=0;
            ArrayList<String> cities=CityLab.get(context).getProviceCity(currentProvince);
            currentCities.clear();
            for (int i=0;i<cities.size();i++) {
                City city=CityLab.get(context).isContain(cities.get(i));
                currentCities.add(city);
            }
//            for (int i=0;i<currentCities.size();i++){
//                final City city=currentCities.get(i);
//                Observable.create(new Observable.OnSubscribe<Object>() {
//                    @Override
//                    public void call(Subscriber<? super Object> subscriber) {
////                        HttpParse httpParese=new HttpParse();
////                        httpParese.getPlace(city);
//                        subscriber.onCompleted();
//                    }
//                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
//
//                    @Override
//                    public void onCompleted() {
//                        cityCount++;
//                        if(cityCount==currentCities.size()) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//
//                    }
//                });
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (cityCount!=currentCities.size()){
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            gridAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }).start();
            gridAdapter.notifyDataSetChanged();
        }
        private class ItemHolder1 extends RecyclerView.ViewHolder{
            private ImageView topImage;
            public ItemHolder1(View itemView) {
                super(itemView);
                topImage=(ImageView)itemView.findViewById(R.id.china_top_image);
            }
        }

        private class ItemHolder2 extends RecyclerView.ViewHolder{
            RelativeLayout yunnan_Layout;
            RelativeLayout chongqing_layout;
            RelativeLayout shandong_layout;
            RelativeLayout hunan_layout;
            RelativeLayout zhejiang_layout;
            RelativeLayout xizang_layout;
            RelativeLayout hainan_layout;

            public ItemHolder2(View itemView) {
                super(itemView);
                yunnan_Layout=(RelativeLayout)itemView.findViewById(R.id.yunnan_layout);
                chongqing_layout=(RelativeLayout)itemView.findViewById(R.id.chongqing_layout);
                shandong_layout=(RelativeLayout)itemView.findViewById(R.id.shandong_layout);
                hunan_layout=(RelativeLayout)itemView.findViewById(R.id.hunan_layout);
                zhejiang_layout=(RelativeLayout)itemView.findViewById(R.id.zhejiang_layout);
                xizang_layout=(RelativeLayout)itemView.findViewById(R.id.xizang_layout);
                hainan_layout=(RelativeLayout)itemView.findViewById(R.id.hainan_layout);
            }
        }
        private class ItemHolder3 extends RecyclerView.ViewHolder{
            ConvenientBanner banner;
            ImageView leftArrow;
            ImageView rightArrow;
            public ItemHolder3(View itemView) {
                super(itemView);
                banner=(ConvenientBanner)itemView.findViewById(R.id.china_banner);
                leftArrow = (ImageView) itemView.findViewById(R.id.china_hot_city_left_icon);
                rightArrow = (ImageView) itemView.findViewById(R.id.china_hot_city_right_icon);
            }
        }
        private class ItemHolder4 extends RecyclerView.ViewHolder{
            TextView provinceTv;
            RecyclerView cityGridRecycler;

            public ItemHolder4(View itemView) {
                super(itemView);
                provinceTv=(TextView)itemView.findViewById(R.id.china_province_tv);
                cityGridRecycler=(RecyclerView)itemView.findViewById(R.id.china_city_grid);
            }
        }
        private class ItemHolder5 extends RecyclerView.ViewHolder{
            RecyclerView scenicRecycelr;
            public ItemHolder5(View itemView) {
                super(itemView);
                scenicRecycelr=(RecyclerView)itemView.findViewById(R.id.china_hot_scenic_recycler);
            }
        }
    }

    private class GridCityAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new cityHolder(getLayoutInflater().from(context).inflate(R.layout.china_city_grid_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
            cityHolder cityHolder=(cityHolder)holder;
            final int tempPosition=position;
            cityHolder.textview.setText(currentCities.get(position).getCityName());
            Glide.with(context).load(currentCities.get(position).getIamgeUrls().get(1)).into(cityHolder.imageView);
            cityHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    City city=currentCities.get(tempPosition);
                    Log.i("China","城市名:"+city.getCityName());
                    Intent intent=new Intent(context,ScenicActivity.class);
                    intent.putExtra(ScenicActivity.EXTRA_CITY,city.getCityName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                }
            });
        }
        @Override
        public int getItemCount() {
            return currentCities.size();
        }

        private class cityHolder extends RecyclerView.ViewHolder{
            private ImageView imageView;
            private TextView textview;
            public cityHolder(View itemView) {
                super(itemView);
                imageView=(ImageView)itemView.findViewById(R.id.china_grid_image);
                textview=(TextView)itemView.findViewById(R.id.china_grid_tv);
            }
        }
    }

    private class TopImageHolder implements Holder<City> {
        private ImageView imageView;
        private TextView textView;
        @Override
        public View createView(Context context) {
            View v=getLayoutInflater().inflate(R.layout.china_city_banner_item,null);
            imageView=(ImageView)v.findViewById(R.id.china_banner_image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textView=(TextView)v.findViewById(R.id.china_banner_tv);
            return v;
        }

        @Override
        public void UpdateUI(Context context, int position, City data) {
            Glide.with(context).load(data.getIamgeUrls().get(0)).into(imageView);
            textView.setText(data.getCityName());
        }

    }

    private class HotScenicSpotAdapter extends RecyclerView.Adapter {
        private ArrayList<Scenicspot> scenicspots=hotScenicSpots;


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.scenic_recycler_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            String imgUrl;
            if(scenicspots.get(position).getImgUrls().size()>0){
                imgUrl = scenicspots.get(position).getImgUrls().get(0).getBigImgUrl();
            } else imgUrl = scenicspots.get(position).getFirstImage();
            String name = scenicspots.get(position).getScenicName();
            String intro = scenicspots.get(position).getBrightPoint();
            final Scenicspot scenicspot = scenicspots.get(position);
            ImageView imageView=myViewHolder.imageView;
            Glide.with(context.getApplication()).load(imgUrl).override(800,600).diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true).centerCrop().into(imageView);
            if(intro.length()>=1)
                myViewHolder.introTv.setText(intro.substring(1,intro.length()));
//                Log.i(TAG, "名字"+name+" 亮点" + intro);
            myViewHolder.rankTv.setText(scenicspot.getRank());
            myViewHolder.nameTv.setText(name);
            myViewHolder.nameTVbottom.setText(name);
            final int scePosition=position;
            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ScenicDetailActivity.class);
                    intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC_SER,scenicspot);
                    ActivityOptions activityOptions=null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        android.util.Pair<View, String> pair[] = new android.util.Pair[2];
                        pair[0] = new android.util.Pair<View, String>(myViewHolder.imageView, "scenic_img");
                        pair[1] = new android.util.Pair<View, String>(myViewHolder.nameTVbottom, "scenic_name");
                        activityOptions=ActivityOptions.makeSceneTransitionAnimation(context,pair);
                    }
                    startActivity(intent,activityOptions.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return hotScenicSpots.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        ImageView imageView;
        TextView nameTv;
        TextView introTv;
        TextView rankTv;
        TextView nameTVbottom;
        public MyViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.scenic_item_name_tv);
            introTv=(TextView)itemView.findViewById(R.id.scenic_item_short_tv);
            imageView = (ImageView) itemView.findViewById(R.id.scenic_recycler_item_img);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.scenic_recycler_linearLayout);
            rankTv = (TextView) itemView.findViewById(R.id.scenic_main_rank);
            nameTVbottom = (TextView) itemView.findViewById(R.id.scenic_recycler_item_name);
        }
    }

    private void hideProgress(){
        progressLayout.setVisibility(View.GONE);
    }

}
