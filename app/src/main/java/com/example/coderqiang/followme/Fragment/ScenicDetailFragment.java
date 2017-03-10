package com.example.coderqiang.followme.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coderqiang.followme.Activity.PictureActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.View.CornersTransform;
import com.example.coderqiang.followme.View.CustomViewPager;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/11.
 */

public class ScenicDetailFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ScenicDetailFragment";
    Scenicspot scenicspot;
    @Bind(R.id.scenic_detail_linearlayout)
    public LinearLayout linearLayout;
    @Bind(R.id.scenic_detail_intro_expand_textView)
    TextView introTv;
    @Bind(R.id.scenic_detail_item_counttime)
    TextView countTime;
    @Bind(R.id.scenic_detail_item_price)
    TextView price;
    @Bind(R.id.scenic_detail_item_openTime)
    TextView openTime;
    @Bind(R.id.scenic_detail_album_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.scenic_detail_item_addr)
    TextView address;
    Fragment context;
    ArrayList<String> albumImg;
    CustomViewPager vp;
    boolean canBack=false;
    public static ScenicDetailFragment newInstance(Scenicspot scenicspot, CustomViewPager viewPager){
        ScenicDetailFragment scenicDetailFragment=new ScenicDetailFragment();
        scenicDetailFragment.scenicspot=scenicspot;
        scenicDetailFragment.vp=viewPager;
        return scenicDetailFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenic_detail_intro, container, false);
        ButterKnife.bind(this,view);
        context=this;
        this.vp.setObjectForPosition(view, 0);
        Log.i(TAG, "创建view" + scenicspot.getScenicName()+"");
        String intro = scenicspot.getIntroduction();
        intro = "   " + intro;
//        Log.i(TAG,"介绍"+intro);
        introTv.setText(intro);
        countTime.setText(scenicspot.getCountTime()+"");
        price.setText(scenicspot.getTicket()+" ");
//        Log.i(TAG, "门票" + scenicspot.getTicket());
        address.setText(scenicspot.getAddr());
        openTime.setText(scenicspot.getOpenTime()+"");
        albumImg = new ArrayList<String>();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_collapse:
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter{

        public MyAdapter() {
            super();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AlbumHolder(LayoutInflater.from(getActivity()).inflate(R.layout.header_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final AlbumHolder albumHolder=(AlbumHolder)holder;
            final int comPosition=position;
            albumHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                    ArrayList<String> images = new ArrayList<String>();
                    for (int i=0;i<scenicspot.getImgUrls().size();i++) {
                        images.add(scenicspot.getImgUrls().get(i).getBigImgUrl());
                    }
                    intent.putStringArrayListExtra(PictureActivity.EXTRA_IMGURLS,images);
                    intent.putExtra(PictureActivity.EXTRA_POSITION,comPosition);
                    intent.putExtra(PictureActivity.EXTRA_DESCRIPTION, scenicspot.getIntroduction());
                    Pair pair=new android.util.Pair<View, String>(albumHolder.imageView, "scenic_img");
                    startActivity(intent);
                }
            });
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(context).load(scenicspot.getImgUrls().get(comPosition).getSmallImgUrl()).skipMemoryCache(true).into(albumHolder.imageView);
                        ((ScenicDetailActivity)getActivity()).canBack=true;
                    }
                },800);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return scenicspot.getImgUrls().size();
        }

        private class AlbumHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public AlbumHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.header_item_img);
            }
        }

    }

}
