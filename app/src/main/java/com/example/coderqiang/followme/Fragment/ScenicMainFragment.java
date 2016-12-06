package com.example.coderqiang.followme.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Activity.MainActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.View.ViewPagerScaleTransformer;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/12/4.
 */

public class ScenicMainFragment extends Fragment {
    private static final String TAG="ScenicMainFragment";
    @Bind(R.id.scenic_main_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.scenic_main_twinkRefresh)
    TwinklingRefreshLayout twinklingRefreshLayout;
    ArrayList<ScenicHeaderFragment> fragments;
    Fragment context;
    ArrayList<Scenicspot> scenicspots;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenic_main, container, false);
        context=this;
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){
        scenicspots = ScenicspotLab.get(getActivity().getApplicationContext()).getScenicspots();
        twinklingRefreshLayout.setPureScrollModeOn(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new MyAdapter());
        ArrayList<Scenicspot> scenicspots= ScenicspotLab.get(getActivity()).getScenicspots();
        fragments = new ArrayList<ScenicHeaderFragment>();
        for (int i=0;i<5;i++){
            fragments.add(ScenicHeaderFragment.newInstance(scenicspots.get(i)));
        }
    }

    private class MyAdapter extends RecyclerView.Adapter{
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_INTRO=1;
        private static final int TYPE_NORMAL=2;
        private boolean isRecyc=true;
        HeaderHolder headerHolder;
        IntroHolder introHolder;
        MyViewHolder myViewHolder;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_HEADER){
                headerHolder=new HeaderHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_recycler_header_layout, parent, false));
                return headerHolder;
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
            if(holder instanceof HeaderHolder){
                if(isRecyc){
                    headerHolder= (HeaderHolder) holder;
                    ((HeaderHolder) holder).viewPager.setAdapter(new FragAdapter(((MainActivity)getActivity()).getSupportFragmentManager(),fragments));
                }

//                    ((HeaderHolder) holder).viewPager.setCurrentItem(current);
            }else if(holder instanceof MyViewHolder){
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                String imgUrl;
                if(scenicspots.get(position).getImgUrls().size()>0){
                    imgUrl = scenicspots.get(position).getImgUrls().get(0).getBigImgUrl();
                } else imgUrl = scenicspots.get(position).getFirstImg();
                String name = scenicspots.get(position).getScenicName();
                String intro = scenicspots.get(position).getBrightPoint();
                final Scenicspot scenicspot = scenicspots.get(position);
                ImageView imageView=myViewHolder.imageView;
                Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true).into(imageView);
                myViewHolder.introTv.setText(intro);
//                Log.i(TAG, "名字"+name+" 亮点" + intro);
                myViewHolder.nameTv.setText(scenicspot.getScenicName());
                final int scePosition=position;
                myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                        intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC, scePosition);
                        startActivity(intent);
                    }
                });
                myViewHolder.detailTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                        intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC, scePosition);
                        startActivity(intent);
                    }
                });
            }
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
        public int getItemViewType(int position) {
            if (position==0) return TYPE_HEADER;
            else if(position==1) return TYPE_INTRO;
            else return TYPE_NORMAL;
        }

        @Override
        public int getItemCount() {
            return 2+scenicspots.size();
        }

        private class HeaderHolder extends RecyclerView.ViewHolder {
            ViewPager viewPager;
            public HeaderHolder(View itemView) {
                super(itemView);
                viewPager = (ViewPager) itemView.findViewById(R.id.scenic_recycler_header_viewpager);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                viewPager.setPageTransformer(false,new ViewPagerScaleTransformer());

            }
        }

        private class IntroHolder extends RecyclerView.ViewHolder{

            public IntroHolder(View itemView) {
                super(itemView);

            }
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

    }

    class FragAdapter extends FragmentPagerAdapter {
        private List<ScenicHeaderFragment> fragments;

        public FragAdapter(android.support.v4.app.FragmentManager fm, List<ScenicHeaderFragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

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

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
