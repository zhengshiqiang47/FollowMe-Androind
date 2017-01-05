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
import com.example.coderqiang.followme.View.AddScenicDialog;
import com.example.coderqiang.followme.View.CornersTransform;
import com.example.coderqiang.followme.View.ViewPagerScaleTransformer;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

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
        context=this;
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
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                Observable.create(new Observable.OnSubscribe<Object>() {

                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext(null);
                        HttpParse httpParse=new HttpParse();
                        Log.i(TAG,city.getCityName()+"Page:"+city.getScenicPage());
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
                String imgUrl;
                if(scenicspots.get(position).getImgUrls().size()>0){
                    imgUrl = scenicspots.get(position).getImgUrls().get(0).getBigImgUrl();
                } else imgUrl = scenicspots.get(position).getFirstImg();
                String name = scenicspots.get(position).getScenicName();
                String intro = scenicspots.get(position).getBrightPoint();
                String countComment=scenicspots.get(position).getCommentCount();
                String countImage=scenicspots.get(position).getImgUrls().size()+"";
                final Scenicspot scenicspot = scenicspots.get(position);
                ImageView imageView=myViewHolder.imageView;
                Glide.with(getActivity().getApplication()).load(imgUrl).override(800,600).diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true).centerCrop().into(imageView);
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
            httpParse.getScenicspot(getActivity(),city.getCityName(),city.getScenicPage()+"");
            if(!city.getCityName().equals( MyLocation.getMyLocation(getActivity().getApplicationContext()).getCityName()))
                city.addscenicPage();
            httpParse.getAllScenicDetails(getActivity(),city.getCityName());
            httpParse.getWeather(context.getActivity().getApplicationContext(),city);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i(TAG,"开始初始化view");
            myAdapter.refresh();

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

    private void getPlace(final ImageView imageView){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                HttpParse httpParese=new HttpParse();
                httpParese.getPlace(city);
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
}
