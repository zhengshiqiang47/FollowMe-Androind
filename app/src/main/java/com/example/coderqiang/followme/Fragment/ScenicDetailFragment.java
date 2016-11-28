package com.example.coderqiang.followme.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.View.CustomViewPager;
import com.ms.square.android.expandabletextview.ExpandableTextView;

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
    ExpandableTextView introTv;
    @Bind(R.id.scenic_detail_traffic_expand_textView)
    ExpandableTextView trafficTv;
    @Bind(R.id.scenic_detail_item_counttime)
    TextView countTime;
    @Bind(R.id.scenic_detail_item_price)
    TextView price;
    @Bind(R.id.scenic_detail_item_openTime)
    TextView openTime;
    CustomViewPager vp;
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
        this.vp.setObjectForPosition(view, 0);
        Log.i(TAG, "创建view" + scenicspot.getScenicName()+"");
        String intro = scenicspot.getIntroduction();
        intro = "   " + intro;
        intro=intro.replace("。","。\n");
        introTv.setText(intro);
        countTime.setText(scenicspot.getCountTime()+"");
        price.setText(scenicspot.getTicket()+"");
        trafficTv.setText(scenicspot.getTraffic()+"");
        openTime.setText(scenicspot.getOpenTime()+"");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_collapse:
                break;
        }
    }

    public int getCountHeight(){
        return introTv.getHeight()+trafficTv.getHeight()+countTime.getHeight()+price.getHeight()+openTime.getHeight()+ ScenicDetailActivity.Dp2Px(getActivity(),50);
    }
}
