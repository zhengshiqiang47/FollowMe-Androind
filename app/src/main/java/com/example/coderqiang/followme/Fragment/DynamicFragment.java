package com.example.coderqiang.followme.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coderqiang.followme.Adapter.ImageAddAdapter;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.DynamicLab;
import com.example.coderqiang.followme.Model.SettingLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.DateUtil;
import com.example.coderqiang.followme.Util.UploadImage;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/4.
 */

public class DynamicFragment extends Fragment {
    private static final String TAG = "DynamicFragment";
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

    interface ImgListener {
        void onClick(int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dynamic, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden == false) {
            initView();
        }
    }

    private void initView() {
        dynamics = DynamicLab.get(getActivity().getApplicationContext()).getDynamics();
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayout);
        MyAdapter adapter = new MyAdapter();
        mRecyclerview.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                dynamics = DynamicLab.get(getActivity().getApplicationContext()).getDynamics();
                mRecyclerview.getAdapter().notifyDataSetChanged();
            }
        });
    }


    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private int TYPE_NORMAL = 2;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.dynamic_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            TextWatcher textWatcher;
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Dynamic dynamic = dynamics.get(position);
            myViewHolder.dynamicUsername.setText(dynamic.getUserName());
            Log.e(TAG,"position:"+position+" "+dynamic.getMemo());
            if(myViewHolder.dynamicCommentEditText.getTag() instanceof TextWatcher)
                myViewHolder.dynamicCommentEditText.removeTextChangedListener((TextWatcher) (((MyViewHolder) holder).dynamicCommentEditText.getTag()));
            myViewHolder.dynamicCommentEditText.setText(dynamic.getMemo());
            UploadImage.setTouxiang(myViewHolder.dynamicAvatar,getActivity());
            textWatcher=new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.e(TAG,"设置memo"+s);
                    dynamics.get(position).setMemo(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
            myViewHolder.dynamicCommentEditText.addTextChangedListener(textWatcher);
            myViewHolder.dynamicCommentEditText.setTag(textWatcher);
            myViewHolder.dynamicLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Dynamic dynamic1:dynamics){
                        Log.e(TAG, "memo"+dynamic1.getMemo());
                    }
                }
            });
            myViewHolder.dynamicContent.setText(dynamic.getContent());
            myViewHolder.dynamicLocation.setText(dynamic.getAddress());
            myViewHolder.dynamicTime.setText(DateUtil.getDateToStringMD(dynamic.getTimeStamp()));
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            Log.i(TAG,"uri.size"+dynamic.getImagURL().size());
            myViewHolder.dynamicItemRecyclerView.setLayoutManager(gridLayoutManager);
            myViewHolder.dynamicItemRecyclerView.setAdapter(new ImageAddAdapter(ImageAddAdapter.VIEW_DYNAMIC,dynamic.getImagURL(),getActivity()));
        }

        @Override
        public int getItemCount() {
            return dynamics.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
//            @Bind(R.id.dynamic_avatar)
            CircleImagview dynamicAvatar;
//            @Bind(R.id.dynamic_username)
            TextView dynamicUsername;
//            @Bind(R.id.dynamic_time)
            TextView dynamicTime;
//            @Bind(R.id.dynamic_content)
            TextView dynamicContent;
//            @Bind(R.id.dynamic_item_recyclerView)
            RecyclerView dynamicItemRecyclerView;
//            @Bind(R.id.dynamic_location)
            TextView dynamicLocation;
//            @Bind(R.id.dynamic_love)
            ImageView dynamicLove;
//            @Bind(R.id.dynamic_share)
            ImageView dynamicShare;
//            @Bind(R.id.dynamic_comment)
            ImageView dynamicComment;
//            @Bind(R.id.dynamic_comment_editText)
            EditText dynamicCommentEditText;
//            @Bind(R.id.dynamic_comment_send)
            ImageView dynamicCommentSend;

            private MyViewHolder(View itemView) {
                super(itemView);
                dynamicAvatar=(CircleImagview)itemView.findViewById(R.id.dynamic_avatar);
                dynamicUsername=(TextView)itemView.findViewById(R.id.dynamic_username);
                dynamicTime=(TextView)itemView.findViewById(R.id.dynamic_time);
                dynamicContent=(TextView)itemView.findViewById(R.id.dynamic_content);
                dynamicItemRecyclerView=(RecyclerView)itemView.findViewById(R.id.dynamic_item_recyclerView);
                dynamicLocation=(TextView)itemView.findViewById(R.id.dynamic_location);
                dynamicLove=(ImageView) itemView.findViewById(R.id.dynamic_love);
                dynamicShare=(ImageView)itemView.findViewById(R.id.dynamic_share);
                dynamicComment=(ImageView)itemView.findViewById(R.id.dynamic_comment);
                dynamicCommentEditText=(EditText)itemView.findViewById(R.id.dynamic_comment_editText);
                dynamicCommentSend=(ImageView)itemView.findViewById(R.id.dynamic_comment_send);
            }
        }
    }
}
