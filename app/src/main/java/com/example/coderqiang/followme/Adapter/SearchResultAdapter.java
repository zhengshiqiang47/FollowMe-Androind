package com.example.coderqiang.followme.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.core.PoiInfo;
import com.example.coderqiang.followme.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2017/2/17.
 */

public class SearchResultAdapter extends RecyclerView.Adapter {
    public static final int TYPE_LOCATION=1;
    public static final int TYPE_USER=2;
    public static final int TYPE_SCENICSPOT=3;
    public static final int TYPE_DYNAMIC=4;
    public static final int TYPE_ADDRESS=5;

    private ArrayList<PoiInfo> PoiInfos;
    private BaiduMap baiduMap;
    Context context;

    public SearchResultAdapter(Context context, ArrayList<PoiInfo> PoiInfos, BaiduMap baiduMap) {
        super();
        this.PoiInfos = PoiInfos;
        this.context=context;
        this.baiduMap=baiduMap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultHolder(LayoutInflater.from(context).inflate(R.layout.item_search_result,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PoiInfo PoiInfo = PoiInfos.get(position);
        final ResultHolder resultHolder=(ResultHolder)holder;
        resultHolder.address.setText(PoiInfo.address);
        resultHolder.name.setText(PoiInfo.name);
        resultHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(PoiInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PoiInfos ==null?0: PoiInfos.size();
    }

    private class ResultHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView type;
        TextView address;
        LinearLayout linearLayout;
        public ResultHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.item_search_result_content);
            address=(TextView)itemView.findViewById(R.id.item_search_result_address);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.item_search_result_layout);
        }
    }

}
