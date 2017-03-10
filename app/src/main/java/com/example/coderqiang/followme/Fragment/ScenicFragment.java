package com.example.coderqiang.followme.Fragment;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Activity.MainActivity;
import com.example.coderqiang.followme.Activity.ScenicActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.Util.ServerUtil;
import com.example.coderqiang.followme.View.AddScenicDialog;
import com.example.coderqiang.followme.View.CornersTransform;
import com.example.coderqiang.followme.View.ViewPagerScaleTransformer;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
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
 * Created by CoderQiang on 2016/11/6.
 */

public class ScenicFragment extends Fragment{
    private static final String TAG="ScenicFragment";
    Fragment context;
    @Bind(R.id.scenic_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.scenic_twinklingrefresh)
    TwinklingRefreshLayout twinklingRefreshLayout;
    @Bind(R.id.scenic_progress_layout)
    RelativeLayout progressLayout;
    LinearLayout addSceLayout;

    public City city;
    ArrayList<Scenicspot> scenicspots;
    MyAdapter myAdapter;
    boolean isFirstRefresh=true;
    int count;

    public static ScenicFragment newInstance(City city){
        ScenicFragment scenicFragment=new ScenicFragment();
        scenicFragment.city=city;
        return scenicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scenic, container, false);
        ButterKnife.bind(this, v);
        EventBus.getDefault().register(this);
        context=this;
        initPoi();
        initData();
        return v;
    }

    private void initData(){
        initView();
        new getScenicDetail().execute();
    }

    private void initView(){
        scenicspots=city.getScenicspots();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                Observable.create(new Observable.OnSubscribe<Object>() {

                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext(null);
                        HttpParse httpParse=new HttpParse();
                        httpParse.getScenicspot(getActivity().getApplicationContext(),city.getCityName(),city.getScenicPage()+"",ServerUtil.LOAD_SUCCESS);
//                        city.addscenicPage();
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
                initView();
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        int TYPE_HEADER=1;
        int TYPE_NORMAL=2;
        HeaderHolder headerHolder;

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
//            if(holder instanceof HeaderHolder) {
//                Log.i(TAG,"onviewRecycled");
//                isRecyc=false;
//                headerHolder=(HeaderHolder) holder;
//                return;
//            }
            super.onViewRecycled(holder);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_HEADER){
                headerHolder=new HeaderHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_recycler_header_layout, parent, false));
                return headerHolder;
            }
            return new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_recycler_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyViewHolder){
                position-=1;
                final MyViewHolder myViewHolder = (MyViewHolder) holder;
                final String imgUrl=scenicspots.get(position).getImgUrls().get(0).getBigImgUrl();
                String name = scenicspots.get(position).getScenicName();
                String intro = scenicspots.get(position).getBrightPoint();
                String countComment=scenicspots.get(position).getCommentCount();
                String countImage=scenicspots.get(position).getImgUrls().size()+"";
                final Scenicspot scenicspot = scenicspots.get(position);
                ImageView imageView=myViewHolder.imageView;
                Glide.with(getActivity().getApplication()).load(imgUrl).override(800,600).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imageView);
                if(intro.length()>=1)
                    myViewHolder.introTv.setText(intro.substring(1,intro.length()));
//                Log.i(TAG, "名字"+name+" 亮点" + intro);
                myViewHolder.rankTv.setText(scenicspot.getRank());
                myViewHolder.nameTv.setText(name);
                myViewHolder.nameTVbottom.setText(name);
                myViewHolder.countComment.setText(countComment);
                myViewHolder.countImage.setText(countImage);
//                final int scePosition=position;
                if(scenicspot.getDistance()==null){
                    getDistance(myViewHolder.distance,scenicspot);
                }else {
                    myViewHolder.distance.setText(scenicspot.getDistance());
                }
                myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                        intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC_SER,scenicspot);
                        intent.putExtra(ScenicDetailActivity.EXTRA_URL, imgUrl);
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
            }
            else if(holder instanceof HeaderHolder){
                headerHolder= (HeaderHolder) holder;
                headerHolder.title.setText(city.getCityName());
                getPlace(headerHolder.imageView);

            }

        }

        public void refresh(){
            notifyDataSetChanged();
            if(city.getWeather().getReason().equals("查询成功")||city.getWeather().getReason().equals("successed!")){
                headerHolder.maxTv.setText(city.getWeather().getResult().getData().getWeather().get(0).getInfo().getDay().get(2));
                headerHolder.minTv.setText(city.getWeather().getResult().getData().getWeather().get(0).getInfo().getNight().get(2));
                headerHolder.weatherInfo.setText(city.getWeather().getResult().getData().getWeather().get(0).getInfo().getDay().get(1));
            }
        }


        @Override
        public int getItemViewType(int position) {
            if(position==0){
                return TYPE_HEADER;
            }
            return TYPE_NORMAL;
        }

        @Override
        public int getItemCount() {
            return scenicspots.size()+1;
        }



        private class HeaderHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView weatherIcon;
            LinearLayout weatherLayout;
            TextView maxTv;
            TextView minTv;
            TextView weatherInfo;
            TextView title;

            public HeaderHolder(View itemView) {
                super(itemView);
                imageView=(ImageView)itemView.findViewById(R.id.scenic_normal_header_imageview);
                weatherIcon=(ImageView)itemView.findViewById(R.id.scenic_normal_header_weather_icon);
                weatherLayout=(LinearLayout)itemView.findViewById(R.id.scenic_normal_header_weather);
                maxTv = (TextView) itemView.findViewById(R.id.weather_maxtemp);
                minTv = (TextView) itemView.findViewById(R.id.weather_mintemp);
                weatherInfo=(TextView)itemView.findViewById(R.id.scenic_normal_header_weater_info);
                title = (TextView) itemView.findViewById(R.id.scenic_normal_header_city_name);
            }
        }
    }

    private class getScenicDetail extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HttpParse httpParse=new HttpParse();
            httpParse.getScenicspot(getActivity(),city.getCityName(),city.getScenicPage()+"", ServerUtil.LOAD_SUCCESS);
            if(!city.getCityName().equals( MyLocation.getMyLocation(getActivity()).getCityName()))
//                city.addscenicPage();
//            httpParse.getAllScenicDetails(getActivity(),city.getCityName());
            httpParse.getWeather(context.getContext().getApplicationContext(),city);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i(TAG,"开始初始化view");


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
        TextView distance;

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
            distance = (TextView) itemView.findViewById(R.id.scenic_item_distance);
            addSceLayout=addScenicLayout;
        }
    }

    private void getPlace(final ImageView imageView){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
//                HttpParse httpParese=new HttpParse();
//                httpParese.getPlace(city);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

            @Override
            public void onCompleted() {
                Glide.with(context).load(city.getIamgeUrls().get(1)).into(imageView);
                hideProgress();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private void hideProgress(){
        progressLayout.setVisibility(View.GONE);
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
                scenicspot.setDistance(""+(1.0f*(int) DistanceUtil.getDistance(latLng,myLl))/1000);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void overrideTransitionEventBus(String msg) {
        if(msg.equals("loadSuccess")){
            if(isFirstRefresh){
                myAdapter = new MyAdapter();
                recyclerView.setAdapter(myAdapter);
                isFirstRefresh=false;
            }
            myAdapter.refresh();
            scenicspots=city.getScenicspots();
            recyclerView.getAdapter().notifyItemChanged(count);
            count=scenicspots.size();
            twinklingRefreshLayout.finishLoadmore();
        }
    }
}
