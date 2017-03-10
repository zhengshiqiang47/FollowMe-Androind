package com.example.coderqiang.followme.Fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.trace.T;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Activity.ChinaActivity;
import com.example.coderqiang.followme.Activity.MainActivity;
import com.example.coderqiang.followme.Activity.ScenicActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Activity.WebViewActivity;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.Util.ServerUtil;
import com.example.coderqiang.followme.Util.TravelPlanUtil;
import com.example.coderqiang.followme.Util.UploadImage;
import com.example.coderqiang.followme.View.AddScenicDialog;
import com.example.coderqiang.followme.View.CitySeletctDialog;
import com.example.coderqiang.followme.View.ViewPagerScaleTransformer;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * Created by CoderQiang on 2016/12/4.
 */

public class ScenicMainFragment extends Fragment {
    private static final String TAG="ScenicMainFragment";
    @Bind(R.id.scenic_main_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.scenic_main_twinkRefresh)
    TwinklingRefreshLayout twinklingRefreshLayout;
    @Bind(R.id.scenic_main_touxiang)
    CircleImagview touxiang;
    @Bind(R.id.scenic_main_spinner)
    Spinner spinner;
    @Bind(R.id.scenic_main_progress)
    ProgressBar progressBar;
    @Bind(R.id.scenic_main_progress_tv)
    TextView progressTv;
    @Bind(R.id.scenic_main_search)
    SearchView searchView;
    @Bind(R.id.scenic_main_title_top_bg)
    TextView title_bg;
    @Bind(R.id.scenic_main_toolbar_bg)
    TextView toolbar_bg;
    @Bind(R.id.scenic_main_top_layout)
    RelativeLayout topLayout;

    LinearLayout addSceLayout;

    Banner banner;

    City city;
    int count;
    ArrayList<ScenicHeaderFragment> fragments;
    ArrayList<String> topImages;
    Fragment context;
    ArrayList<Scenicspot> scenicspots;
    ArrayList<City> allCity=new ArrayList<City>();
    boolean shouldNext=true;
    int page=1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenic_main, container, false);
        context=this;
        ButterKnife.bind(this,view);
        initPoi();
        initView();
        init();
        return view;
    }

    private void initView(){
        hideAndShowProgress(0);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                Observable.create(new Observable.OnSubscribe<Object>() {

                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext(null);
                        HttpParse httpParse=new HttpParse();
                        httpParse.getScenicspot(getActivity().getApplicationContext(),city.getCityName(),city.getScenicPage()+"","loadSuccess");
//                        httpParse.getAllScenicDetails(getActivity().getApplicationContext(),city.getCityName());
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
            }
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                init();
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setSubmitButtonEnabled(true);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }

    private void init(){
        city = CityLab.get(getActivity().getApplicationContext()).getCurrentCity();
        scenicspots = city.getScenicspots();
        Log.i(TAG,"city:"+city.getCityName()+" "+city.getScenicspots().size());
        count=scenicspots.size();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isHide=false;
            int y=0;
            int countY=0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                y+=dy;
//                if(dy>0&&!isHide){
//                    countY+=dy;
//                    if(countY>=300){
//                        topLayout.setTranslationY(-(countY-300));
//                        if(countY>=300+title_bg.getHeight()+toolbar_bg.getHeight()){
//                            countY=0;
//                            isHide=true;
//                        }
//                    }
//                }
//                Log.i(TAG,"isHide"+isHide);
//                if(dy<0&&y>500&&isHide){
//                    countY+=dy;
//                    if (countY<-100){
//                        TranslateAnimation translate=new TranslateAnimation(0,0,0,title_bg.getHeight()+toolbar_bg.getHeight());
//                        translate.setDuration(1000);
//                        translate.setFillAfter(true);
//                        topLayout.setAnimation(translate);
//                        isHide=false;
//                    }
//                }

            }
        });
        fragments = new ArrayList<ScenicHeaderFragment>();
        topImages = new ArrayList<>();
        for (int i=0;i<5;i++){
            if(scenicspots.size()>0&&scenicspots.get(i).getImgUrls().size()>0){
                fragments.add(ScenicHeaderFragment.newInstance(scenicspots.get(i)));
                topImages.add(scenicspots.get(i).getImgUrls().get(0).getBigImgUrl());
                initSpinner();
            } else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (ScenicspotLab.get(getActivity().getApplicationContext()).getScenicspots().size()==0||scenicspots==null||scenicspots.size()==0||scenicspots.get(0).getImgUrls()==null||scenicspots.get(0).getImgUrls().size()==0){
                                try {
                                    Log.i(TAG,"sleep");
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        getActivity().runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(scenicspots.size()>0){
                                    init();
                                    initSpinner();
                                }
                            }
                        }));


                    }
                }).start();
            }
        }
        UploadImage.setTouxiang(touxiang,getActivity().getApplicationContext());
    }

    int defaultPosition=0;

    private void initSpinner() {
        ArrayList<String> cities=new ArrayList<String>();
        String province=null;
        if(CityLab.get(getActivity()).getCurrentCity()!=null){
            province=CityLab.get(getActivity()).getCurrentCity().getProvinceName();
        }
        int temp=0;
        for(int i=0;i<CityLab.get(getActivity()).getCities().size();i++){
            City spinCity=CityLab.get(getActivity()).getCities().get(i);
            if(spinCity.getProvinceName().equals(province)){
                cities.add(spinCity.getCityName());
                allCity.add(spinCity);
                if (spinCity.getCityName().equals(CityLab.get(getActivity()).getCurrentCity().getCityName()))
                    defaultPosition=temp;
                temp++;
            }
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.spinner_dropdown_item,cities);
        spinner.setAdapter(arrayAdapter);
        spinner.setDropDownHorizontalOffset(100);
        spinner.setSelection(defaultPosition,true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=defaultPosition){
                    defaultPosition=position;
                    refreshView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void refreshView(){
        city.setScenicPage(city.getCountPage());
        city=allCity.get(defaultPosition);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext(null);
                HttpParse httpParse=new HttpParse();
                Log.i(TAG,"城市名字"+city.getCityName());
                httpParse.getScenicspot(getActivity().getApplicationContext(),city.getCityName(),city.getScenicPage()+"","loadOtherCity");
//                city.addscenicPage();
//                httpParse.getAllScenicDetails(getActivity().getApplicationContext(),city.getCityName());
                scenicspots=city.getScenicspots();
                count=scenicspots.size();
                Log.i(TAG, "Complete");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "Complete2");
                Log.i(TAG,"通知item改变");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG,"OnNest");
                hideAndShowProgress(1);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void hideAndShowProgress(int type){
        if(type==0){
            progressBar.setVisibility(View.GONE);
            progressTv.setVisibility(View.GONE);
        }else {
            progressTv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private class MyAdapter extends RecyclerView.Adapter{
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_INTRO=1;
        private static final int TYPE_NORMAL=2;
        private boolean isRecyc=true;
//        HeaderHolder headerHolder;
        IntroHolder introHolder;
        MyViewHolder myViewHolder;
        BannerHolder bannerHolder;


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_HEADER){
                bannerHolder=new BannerHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_main_banner, parent, false));
                return bannerHolder;
            }else if(viewType==TYPE_INTRO){
                introHolder = new IntroHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_main_item_2, parent, false));
                return introHolder;
            } else if (viewType == TYPE_NORMAL) {
                myViewHolder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_recycler_item, parent, false));
                return myViewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            position-=2;
            if(holder instanceof BannerHolder){
                if(isRecyc){
                    bannerHolder= (BannerHolder) holder;
                    bannerHolder=(BannerHolder)holder;
                    banner=bannerHolder.banner;
                    bannerHolder.banner.setImageLoader(new GlideImageLoader());
                    bannerHolder.banner.setImages(topImages);
                    bannerHolder.banner.start();
                }
            } else if(holder instanceof MyViewHolder){
                final MyViewHolder myViewHolder = (MyViewHolder) holder;
                String imgUrl=null;
                String countComment=scenicspots.get(position).getCommentCount();
                String countImage=scenicspots.get(position).getImgUrls().size()+"";
                String name = scenicspots.get(position).getScenicName();
                String intro = scenicspots.get(position).getBrightPoint();
                final Scenicspot scenicspot = scenicspots.get(position);
                ImageView imageView=myViewHolder.imageView;
                final String url=scenicspots.get(position).getImgUrls().get(0).getBigImgUrl();
                Glide.with(context.getActivity()).load(scenicspots.get(position).getImgUrls().get(0).getBigImgUrl()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(imageView);
                if(intro.length()>=1)
                    myViewHolder.introTv.setText(intro.substring(1,intro.length()));
//                Log.i(TAG, "名字"+name+" 亮点" + intro);
                myViewHolder.rankTv.setText(scenicspot.getRank());
                myViewHolder.nameTv.setText(name);
                myViewHolder.nameTVbottom.setText(name);
                myViewHolder.countComment.setText(countComment);
                myViewHolder.countImage.setText(countImage);
                if(scenicspot.getDistance()==null){
                    getDistance(myViewHolder.distanceTv,scenicspot);
                }else {
                    myViewHolder.distanceTv.setText(scenicspot.getDistance());
                }
                final int scePosition=position;
                myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                        intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC_SER,scenicspot);
                        intent.putExtra(ScenicDetailActivity.EXTRA_URL,url);
                        ActivityOptions activityOptions=null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            android.util.Pair<View, String> pair[] = new android.util.Pair[2];
                            pair[0] = new android.util.Pair<View, String>(myViewHolder.imageView, "scenic_img");
                            pair[1] = new android.util.Pair<View, String>(myViewHolder.nameTVbottom, "scenic_name");
                            activityOptions=ActivityOptions.makeSceneTransitionAnimation(getActivity(),pair);
                        }
                        startActivity(intent,activityOptions.toBundle());
                    }
                });
                myViewHolder.addScenicLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG,"到这里去");
                        AddScenicDialog dialog=new AddScenicDialog(getActivity(),(int)addSceLayout.getX(),(int)addSceLayout.getY(),scenicspot);
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(true);
                    }
                });
            }else if(holder instanceof IntroHolder){
                IntroHolder introHolder=(IntroHolder)holder;
                introHolder.scenicNear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),ScenicActivity.class);
                        intent.putExtra(ScenicActivity.EXTRA_CITY,MyLocation.getMyLocation(getActivity().getApplicationContext()).getCityName());
                        startActivity(intent);
                        ((MainActivity)getActivity()).overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    }
                });
                introHolder.scenicIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ChinaActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                        ((MainActivity)getActivity()).overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    }
                });
                introHolder.travelIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "国内游记");
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra(WebViewActivity.TYPE,WebViewActivity.TYPE_URL);
                        intent.putExtra(WebViewActivity.WEB_URL,"http://you.ctrip.com/travels/china110000.html");
                        intent.putExtra(WebViewActivity.EXTRA_COUNT, 2);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    }
                });
                introHolder.travelOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra(WebViewActivity.TYPE,WebViewActivity.TYPE_URL);
                        intent.putExtra(WebViewActivity.WEB_URL,"http://you.ctrip.com/travels");
                        intent.putExtra(WebViewActivity.EXTRA_COUNT, 2);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);

                    }
                });
                introHolder.travelNear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        ArrayList<City> cities = CityLab.get(getActivity().getApplicationContext()).getCities();
                        String currentCity = MyLocation.getMyLocation(getActivity().getApplicationContext()).getCityName();
                        String ctripIdAndName="";
                        for (City city : cities) {
                            if(currentCity.contains(city.getCityName())){
                                ctripIdAndName = city.getCtripId();
                                break;
                            }
                        }
                        Log.i(TAG, "ctripName" + ctripIdAndName);
                        intent.putExtra(WebViewActivity.TYPE,WebViewActivity.TYPE_URL);
                        intent.putExtra(WebViewActivity.WEB_URL,"http://you.ctrip.com/travels/"+ctripIdAndName+".html");
                        intent.putExtra(WebViewActivity.EXTRA_COUNT, 2);
                        startActivity(intent);
                        ((MainActivity)getActivity()).overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    }
                });
            }
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            if(holder instanceof BannerHolder) {
                isRecyc=false;
                bannerHolder=(BannerHolder) holder;
                return;
            }
            super.onViewRecycled(holder);
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0) return TYPE_HEADER;
            else if(position==1) return TYPE_INTRO;
            else return TYPE_NORMAL;
        }

        @Override
        public int getItemCount() {
            return 2+scenicspots.size();
        }

        private class BannerHolder extends RecyclerView.ViewHolder{

            Banner banner;
            public BannerHolder(View itemView) {
                super(itemView);
                banner = (Banner) itemView.findViewById(R.id.scenic_main_banner);

            }
        }


//
//        private class HeaderHolder extends RecyclerView.ViewHolder {
//            ViewPager viewPager;
//            public HeaderHolder(View itemView) {
//                super(itemView);
//            }
//        }

        private class IntroHolder extends RecyclerView.ViewHolder{
            LinearLayout travelNear;
            LinearLayout travelIn;
            LinearLayout travelOut;
            LinearLayout scenicNear;
            LinearLayout scenicIn;
            LinearLayout scenicOut;
            public IntroHolder(View itemView) {
                super(itemView);
                travelNear=(LinearLayout)itemView.findViewById(R.id.scenic_main_travel_near);
                travelIn = (LinearLayout) itemView.findViewById(R.id.scenic_main_travel_in);
                travelOut = (LinearLayout) itemView.findViewById(R.id.scenic_main_travel_out);
                scenicNear = (LinearLayout) itemView.findViewById(R.id.scenic_main_near);
                scenicIn = (LinearLayout) itemView.findViewById(R.id.scenic_main_in);
                scenicOut = (LinearLayout) itemView.findViewById(R.id.scenic_main_out);
            }
        }

        private class MyViewHolder extends RecyclerView.ViewHolder{
            LinearLayout linearLayout;
            ImageView imageView;
            TextView nameTv;
            TextView introTv;
            TextView rankTv;
            TextView nameTVbottom;
            TextView countImage;
            TextView countComment;
            LinearLayout addScenicLayout;
            TextView distanceTv;

            public MyViewHolder(View itemView) {
                super(itemView);
                nameTv = (TextView) itemView.findViewById(R.id.scenic_item_name_tv);
                introTv=(TextView)itemView.findViewById(R.id.scenic_item_short_tv);
                imageView = (ImageView) itemView.findViewById(R.id.scenic_recycler_item_img);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.scenic_recycler_linearLayout);
                rankTv = (TextView) itemView.findViewById(R.id.scenic_main_rank);
                nameTVbottom = (TextView) itemView.findViewById(R.id.scenic_recycler_item_name);
                countImage=(TextView)itemView.findViewById(R.id.scenic_item_count_img);
                countComment=(TextView)itemView.findViewById(R.id.scenic_item_count_commment);
                addScenicLayout=(LinearLayout) itemView.findViewById(R.id.scenic_item_go);
                distanceTv = (TextView) itemView.findViewById(R.id.scenic_item_distance);
                addSceLayout=addScenicLayout;
            }
        }

    }

    GeoCoder geoCoder;

    private void initPoi(){
        geoCoder= GeoCoder.newInstance();
    }

    public String getDistance(final TextView textView, final Scenicspot scenicspot){
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult){
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    Log.i(TAG,"地理编码出错");
                }
                LatLng latLng=geoCodeResult.getLocation();
                LatLng myLl = new LatLng(MyLocation.getMyLocation(getActivity()).getLatitute(), MyLocation.getMyLocation(getActivity()).getLongtitute());
                if(myLl.latitude==0){
                    Log.i(TAG,"纬度为0");
                }
                scenicspot.setDistance(""+(1.0f*(int)DistanceUtil.getDistance(latLng,myLl))/1000);
                if (textView!=null){
                    textView.setText("(距我 "+scenicspot.getDistance()+" km)");
                    scenicspot.setDistance("(距我 "+scenicspot.getDistance()+" km)");
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

            }
        };
        geoCoder.setOnGetGeoCodeResultListener(listener);
        geoCoder.geocode(new GeoCodeOption().city(scenicspot.getCityName()).address(scenicspot.getAddr()));
        return null;
    }

    private class TopImageHolder implements Holder<String>{
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).into(imageView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        geoCoder.destroy();
    }

    //    class FragAdapter extends FragmentPagerAdapter {
//        private List<ScenicHeaderFragment> fragments;
//
//        public FragAdapter(android.support.v4.app.FragmentManager fm, List<ScenicHeaderFragment> fragments) {
//            super(fm);
//            this.fragments=fragments;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.size();
//        }
//
//
//        @Override
//        public ScenicHeaderFragment getItem(int position) {
//            return fragments.get(position);
//        }
//    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);

        }

        //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    }

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void overrideTransitionEventBus(String msg) {
        if(msg.equals("景点数据获取完成")){
            Log.i(TAG,"初始化布局"+msg);
            init();
        }else if(msg.equals("定位改变")) {
            Log.i(TAG,"重新改变布局");
            init();
        }else if(msg.equals(ServerUtil.LOAD_SUCCESS)){
            Log.i(TAG, "hiden:" + isHidden());
            if(!isHidden()){
                scenicspots=city.getScenicspots();
                recyclerView.getAdapter().notifyItemChanged(count);
                Log.e(TAG, "conunt" + count);
                count=scenicspots.size();
                twinklingRefreshLayout.finishLoadmore();
            }
        } else if (msg.equals(ServerUtil.LOAD_OTHERCITY)) {
            hideAndShowProgress(0);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
        }
    }

}
