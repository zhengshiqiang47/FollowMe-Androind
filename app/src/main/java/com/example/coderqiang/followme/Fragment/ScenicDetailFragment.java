package com.example.coderqiang.followme.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/11.
 */

public class ScenicDetailFragment extends Fragment {
    private static final String TAG = "ScenicDetailFragment";
    Scenicspot scenicspot;
    @Bind(R.id.scenic_detail_item_intro)
    TextView introTv;
    @Bind(R.id.scenic_detail_item_counttime)
    TextView countTime;
    @Bind(R.id.scenic_detail_item_price)
    TextView price;
    @Bind(R.id.scenic_detail_item_traffic)
    TextView traffic;
    @Bind(R.id.scenic_detail_item_openTime)
    TextView openTime;
    public static ScenicDetailFragment newInstance(Scenicspot scenicspot){
        ScenicDetailFragment scenicDetailFragment=new ScenicDetailFragment();
        scenicDetailFragment.scenicspot=scenicspot;
        return scenicDetailFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenic_detail_intro, container, false);
        ButterKnife.bind(this,view);
        Log.i(TAG, "创建view" + scenicspot.getScenicName()+"");
        introTv.setText(scenicspot.getIntroduction()+"");
        countTime.setText(scenicspot.getCountTime()+"");
        price.setText(scenicspot.getTicket()+"");
        traffic.setText(scenicspot.getTraffic()+"");
        openTime.setText(scenicspot.getOpenTime()+"");
        return view;
    }
}
