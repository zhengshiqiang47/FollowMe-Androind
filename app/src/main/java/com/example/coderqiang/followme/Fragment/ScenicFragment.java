package com.example.coderqiang.followme.Fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Activity.MainActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.View.ViewPagerScaleTransformer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class ScenicFragment extends Fragment {
    private static final String TAG="ScenicFragment";
    @Bind(R.id.scenic_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.scenic_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ScenicHeaderFragment> fragments;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scenic, container, false);
        ButterKnife.bind(this, v);
        fragments=new ArrayList<ScenicHeaderFragment>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        MyAdapter myAdapter = new MyAdapter(this);
        recyclerView.setAdapter(myAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        initView();
        return v;
    }

    private void initView(){
        ArrayList<Scenicspot> scenicspots=ScenicspotLab.get(getActivity()).getScenicspots();
        for(int i=0;i<5;i++){
            fragments.add(ScenicHeaderFragment.newInstance(scenicspots.get(i)));
        }
    }

    class FragAdapter extends FragmentPagerAdapter {
        private List<ScenicHeaderFragment> fragments;

        public FragAdapter(android.support.v4.app.FragmentManager fm, List<ScenicHeaderFragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(TAG, "destroy");
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public ScenicHeaderFragment getItem(int position) {
            return fragments.get(position);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private ArrayList<Scenicspot> scenicspots;
        ScenicFragment scenicFragment;
        int TYPE_HEADER=1;
        int TYPE_NORMAL=2;
        int current=1;
        boolean isRecyc=true;
        HeaderHolder headerHolder;

        public MyAdapter(ScenicFragment scenicFragment) {
            super();
            this.scenicFragment = scenicFragment;
            scenicspots= ScenicspotLab.get(getActivity()).getScenicspots();
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            if(holder instanceof HeaderHolder) {
                Log.i(TAG,"onviewRecycled");
                isRecyc=false;
                headerHolder=(HeaderHolder) holder;
                return;
            }
            super.onViewRecycled(holder);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_HEADER){
                if(isRecyc){
                    headerHolder=new HeaderHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_recycler_header_layout, parent, false));
                }
                return headerHolder;
            }
            return new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_recycler_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            position-=1;
            if(holder instanceof MyViewHolder){
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                String imgUrl;
                if(scenicspots.get(position).getImgUrls()!=null){
                    imgUrl = scenicspots.get(position).getImgUrls().get(0);
                }else imgUrl=scenicspots.get(position).getFirstImg();
                String name = scenicspots.get(position).getScenicName();
                String intro = scenicspots.get(position).getShotIntro();
                final Scenicspot scenicspot = scenicspots.get(position);
                ImageView imageView=myViewHolder.imageView;
                Glide.with(this.scenicFragment).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
                myViewHolder.introTv.setText(intro);
                myViewHolder.nameTv.setText(name);
                myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                        intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC, (Serializable) scenicspot);
                        startActivity(intent);
                    }
                });
                myViewHolder.detailTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                        intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC, scenicspot);
                        startActivity(intent);
                    }
                });
            }else if(holder instanceof HeaderHolder){
                if(isRecyc){
                    headerHolder= (HeaderHolder) holder;
                    ((HeaderHolder) holder).viewPager.setAdapter(new FragAdapter(((MainActivity)getActivity()).getSupportFragmentManager(),fragments));
                    ((HeaderHolder) holder).viewPager.setCurrentItem(current);
                }

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

        private class MyViewHolder extends RecyclerView.ViewHolder{
            LinearLayout linearLayout;
            ImageView imageView;
            TextView nameTv;
            TextView introTv;
            TextView detailTv;
            public MyViewHolder(View itemView) {
                super(itemView);
                nameTv = (TextView) itemView.findViewById(R.id.scenic_item_name_tv);
                introTv=(TextView)itemView.findViewById(R.id.scenic_item_short_tv);
                imageView = (ImageView) itemView.findViewById(R.id.scenic_recycler_item_img);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.scenic_recycler_linearLayout);
                detailTv = (TextView) itemView.findViewById(R.id.scenic_recycler_detailTv);
            }
        }

        private class HeaderHolder extends RecyclerView.ViewHolder {
            ViewPager viewPager;
            public HeaderHolder(View itemView) {
                super(itemView);
                viewPager = (ViewPager) itemView.findViewById(R.id.scenic_recycler_header_viewpager);
                viewPager.setPageMargin(dp2px(getActivity(),-80));
                viewPager.setOffscreenPageLimit(3);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        current = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                viewPager.setPageTransformer(false,new ViewPagerScaleTransformer());

            }
        }
    }

    private class getScenicDetail extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpParse httpParse=new HttpParse();
            httpParse.getAllScenicDetails(getActivity());
            return null;
        }
    }

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
