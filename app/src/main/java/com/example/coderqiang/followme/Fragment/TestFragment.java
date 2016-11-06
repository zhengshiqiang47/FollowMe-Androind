package com.example.coderqiang.followme.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coderqiang.followme.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/4.
 */

public class TestFragment extends android.support.v4.app.Fragment {
   @Bind(R.id.square_recyclerview)
    RecyclerView mRecyclerview;
    ArrayList<String> str = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this,v);

        for (int i=0;i<20;i++){
            str.add("Item "+i);
        }
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(new MyAdapter());
        return v;
    }

    class MyAdapter extends RecyclerView.Adapter<TestFragment.MyAdapter.MyViewHolder>{
        @Override
        public TestFragment.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TestFragment.MyAdapter.MyViewHolder holder = new TestFragment.MyAdapter.MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.dynamic_list_item, parent, false));
            return holder;

        }

        @Override
        public void onBindViewHolder(TestFragment.MyAdapter.MyViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return str.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(View itemView) {
                super(itemView);
            }
        }


    }
}
