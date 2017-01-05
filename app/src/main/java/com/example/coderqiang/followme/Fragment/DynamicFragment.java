package com.example.coderqiang.followme.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.UserUtil;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/4.
 */

public class DynamicFragment extends Fragment{
    private static final String TAG="DynamicFragment";
    ViewPager viewPager;
    @Bind(R.id.dynamic_recyclerView)
    RecyclerView mRecyclerview;
    @Bind(R.id.twinklingrefreshLayout)
    TwinklingRefreshLayout refreshLayout;
//    @Bind(R.id.dynamic_swipeRefesh)
//    SwipeRefreshLayout pullToRefreshView;
//    @Bind(R.id.swipeToLoad)
//    SwipeToLoadLayout swipeToLoadLayout;
    ArrayList<String> str = new ArrayList<>();
    ArrayList<Dynamic> dynamics;
    ArrayList<Dynamic> dynamics_2;

    interface ImgListener{
        void onClick(int position);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dynamic, container, false);
        ButterKnife.bind(this,v);
        initView();
        return v;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden==false){
            initView();
        }
    }

    private void initView(){
        dynamics = new ArrayList<Dynamic>();
        dynamics_2 = new ArrayList<Dynamic>();
        for (int i=0;i<5;i++){
            Dynamic dynamic=new Dynamic("aa","a","dym1605041234","去丽江","10/12",null);
            dynamics.add(dynamic);
            dynamics_2.add(dynamic);
        }
        LinearLayoutManager linearLayout=new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayout);
        MyAdapter adapter=new MyAdapter();
        mRecyclerview.setAdapter(new DynamicFragment.MyAdapter());
    }


    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
       private int TYPE_NORMAL=2;


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DynamicFragment.MyAdapter.MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.dynamic_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyViewHolder)holder).userNameTv.setText(dynamics.get(position).getUserName());
        }


        @Override
        public int getItemViewType(int position) {
            return TYPE_NORMAL;
        }

        @Override
        public int getItemCount() {
            return dynamics.size();
        }


        private class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView userNameTv;
            public MyViewHolder(View itemView) {
                super(itemView);
                userNameTv=(TextView)itemView.findViewById(R.id.dynamic_username);
            }
        }


    }
}
