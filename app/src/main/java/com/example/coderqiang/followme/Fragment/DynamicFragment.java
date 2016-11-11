package com.example.coderqiang.followme.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.example.coderqiang.followme.R;

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
            Dynamic dynamic=new Dynamic("Jane_"+i,"fm123","dym1605041234","去丽江","10/12",null);
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
        private int TYPE_HEADER=1;
        private int TYPE_NORMAL=2;


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.i(TAG,"viewType:"+viewType);
            if(viewType==TYPE_HEADER){
                return new HeaderHolder(LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview_header, parent, false));
            }else {
                return new DynamicFragment.MyAdapter.MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.dynamic_list_item, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof HeaderHolder){
                return ;
            }else if(holder instanceof MyViewHolder){
                ((MyViewHolder)holder).userNameTv.setText(dynamics.get(position-1).getUserName());
            }
        }


        @Override
        public int getItemViewType(int position) {
            if(position==0){
                return TYPE_HEADER;
            }else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public int getItemCount() {
            return dynamics.size()+1;
        }


        private class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView userNameTv;
            public MyViewHolder(View itemView) {
                super(itemView);
                userNameTv=(TextView)itemView.findViewById(R.id.dynamic_username);
            }
        }

        private class HeaderHolder extends RecyclerView.ViewHolder {

            private RecyclerView headerRecy;
            public HeaderHolder(View itemView) {
                super(itemView);
                headerRecy=(RecyclerView)itemView.findViewById(R.id.header_recyclerview);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                headerRecy.setLayoutManager(linearLayoutManager);
                headerAdapter headerAdapter = new headerAdapter();
                headerRecy.setAdapter(headerAdapter);
            }



            class headerAdapter extends RecyclerView.Adapter<headerAdapter.headerItemHolder>{
                ImgListener imgListener;

                @Override
                public headerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new headerItemHolder(LayoutInflater.from(getActivity()).inflate(R.layout.header_item_layout, parent, false));
                }

                @Override
                public void onBindViewHolder(final headerItemHolder holder, final int position) {
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dynamics_2.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,3);
                        }
                    });
                }

                @Override
                public int getItemCount() {
                    return dynamics_2.size();
                }

                public void setImgListener(ImgListener imgListener){
                    this.imgListener = imgListener;
                }

                class headerItemHolder extends RecyclerView.ViewHolder{
                    ImageView imageView;
                    public headerItemHolder(final View itemView) {
                        super(itemView);
                        imageView=(ImageView) itemView.findViewById(R.id.header_item_img);
                    }
                }
            }

        }

    }
}
