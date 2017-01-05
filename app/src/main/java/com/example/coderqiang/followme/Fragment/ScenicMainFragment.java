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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.SuggestionSearchOption;
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
import com.example.coderqiang.followme.Util.UploadImage;
import com.example.coderqiang.followme.View.AddScenicDialog;
import com.example.coderqiang.followme.View.CitySeletctDialog;
import com.example.coderqiang.followme.View.ViewPagerScaleTransformer;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

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
    LinearLayout addSceLayout;

    ConvenientBanner banner;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenic_main, container, false);
        context=this;
        ButterKnife.bind(this,view);
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
                        httpParse.getScenicspot(getActivity().getApplicationContext(),city.getCityName(),city.getScenicPage()+"");
                        city.addscenicPage();
                        httpParse.getAllScenicDetails(getActivity().getApplicationContext(),city.getCityName());
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        scenicspots=city.getScenicspots();
                        recyclerView.getAdapter().notifyItemChanged(count);
                        count=scenicspots.size();
                        twinklingRefreshLayout.finishLoadmore();
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
        if(!MyLocation.getMyLocation(getActivity()).isHasLocation()){
            city=CityLab.get(getActivity()).isContain("北京");
            Log.i(TAG,"未定位");
        }else {
            city = CityLab.get(getActivity().getApplicationContext()).getCurrentCity();
        }

        scenicspots = city.getScenicspots();
        Log.i(TAG,"city:"+city.getCityName()+" "+city.getScenicspots().size());
        count=scenicspots.size();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new MyAdapter());
        fragments = new ArrayList<ScenicHeaderFragment>();
        topImages = new ArrayList<>();
        Log.i(TAG,"能到这");
        for (int i=0;i<5;i++){
            if(scenicspots.size()>0&&scenicspots.get(i).getImgUrls().size()>0){
                fragments.add(ScenicHeaderFragment.newInstance(scenicspots.get(i)));
                topImages.add(scenicspots.get(i).getImgUrls().get(0).getBigImgUrl());
                initSpinner();
            } else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (ScenicspotLab.get(getActivity().getApplicationContext()).getScenicspots().size()==0||scenicspots.get(0).getImgUrls().size()==0){
                            try {
                                Log.i(TAG,"sleep");
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        getActivity().runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(scenicspots.size()>0){
                                    for (int i=0;i<5;i++)
                                        topImages.add(scenicspots.get(i).getImgUrls().get(0).getBigImgUrl());
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
        }else province="福建";
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
                httpParse.getScenicspot(getActivity().getApplicationContext(),city.getCityName(),city.getScenicPage()+"");
                city.addscenicPage();
                httpParse.getAllScenicDetails(getActivity().getApplicationContext(),city.getCityName());
                scenicspots=city.getScenicspots();
                count=scenicspots.size();
                Log.i(TAG, "Complete");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "Complete2");
                hideAndShowProgress(0);
                recyclerView.getAdapter().notifyDataSetChanged();
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
        try {
            if(banner.isTurning())
                banner.stopTurning();
        }catch (Exception e){
            Log.e(TAG,"onPause异常",e);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner!=null&&!banner.isTurning())
            banner.startTurning(5000);
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
                    bannerHolder.convenientBanner.setPages(new CBViewHolderCreator<TopImageHolder>() {
                        @Override
                        public TopImageHolder createHolder() {
                            return new TopImageHolder();
                        }
                    },topImages).setPageIndicator(new int[]{R.drawable.circle_indicator_white,R.drawable.circle_indicator_selcet})
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT).setScrollDuration(1000);
                    banner=bannerHolder.convenientBanner;
                    banner.setcurrentitem(0);
                    banner.startTurning(5000);
                }
            } else if(holder instanceof MyViewHolder){
                final MyViewHolder myViewHolder = (MyViewHolder) holder;
                String imgUrl;
                if(scenicspots.get(position).getImgUrls().size()>0){
                    imgUrl = scenicspots.get(position).getImgUrls().get(0).getBigImgUrl();
                } else imgUrl = scenicspots.get(position).getFirstImg();
                String countComment=scenicspots.get(position).getCommentCount();
                String countImage=scenicspots.get(position).getImgUrls().size()+"";
                String name = scenicspots.get(position).getScenicName();
                String intro = scenicspots.get(position).getBrightPoint();
                final Scenicspot scenicspot = scenicspots.get(position);
                ImageView imageView=myViewHolder.imageView;
                Glide.with(context.getActivity().getApplication()).load(imgUrl).override(800,600).diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true).centerCrop().into(imageView);
                if(intro.length()>=1)
                    myViewHolder.introTv.setText(intro.substring(1,intro.length()));
//                Log.i(TAG, "名字"+name+" 亮点" + intro);
                myViewHolder.rankTv.setText(scenicspot.getRank());
                myViewHolder.nameTv.setText(name);
                myViewHolder.nameTVbottom.setText(name);
                myViewHolder.countComment.setText(countComment);
                myViewHolder.countImage.setText(countImage);
                final int scePosition=position;
                myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                        intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC_SER,scenicspot);
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
                        getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
                    }
                });
                introHolder.scenicIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ChinaActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
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
                Log.i(TAG,"onviewRecycled");
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

            ConvenientBanner convenientBanner;
            public BannerHolder(View itemView) {
                super(itemView);
                convenientBanner = (ConvenientBanner) itemView.findViewById(R.id.scenic_main_banner);

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
                addSceLayout=addScenicLayout;
            }
        }

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

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}
