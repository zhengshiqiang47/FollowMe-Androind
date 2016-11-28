package com.example.coderqiang.followme.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.Comment;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.View.CustomViewPager;
import com.example.coderqiang.followme.View.GlideCircleTransform;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2016/11/21.
 */

public class ScenicCommentsFragment extends Fragment {
    private static final String TAG="ScenicCommentsFragment";
    ArrayList<Comment> comments;
    public RecyclerView recyclerView;
    Scenicspot scenicspot;
    Fragment context;
    CustomViewPager vp;

    public static ScenicCommentsFragment newInstance(Scenicspot scenicspot, CustomViewPager vp){
        ScenicCommentsFragment fragment=new ScenicCommentsFragment();
        fragment.scenicspot=scenicspot;
        fragment.vp=vp;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        comments=scenicspot.getComments();
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        vp.setObjectForPosition(v,1);
        context=this;
        recyclerView = (RecyclerView) v.findViewById(R.id.scenic_comments_recylerview);
        CommentsAdapter adapter=new CommentsAdapter();
        Log.i(TAG,"itemCount:"+adapter.getItemCount());
        LinearLayoutManager linearLayout=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);
        return v;
    }

    private class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {


        @Override
        public CommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommentsHolder(LayoutInflater.from(getActivity()).inflate(R.layout.comments_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CommentsHolder holder, int position) {
            Log.i(TAG, "bind" + position);
            Comment comment = comments.get(position);
            Log.i(TAG, "" + comment.getContent());
            Glide.with(context).load(comment.getOwnerImag()).transform(new GlideCircleTransform(getActivity())).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.touxiang);
            holder.content.setText(comment.getContent());
            holder.content.setAlpha(1.0f);
            holder.name.setText(comment.getCommentName());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
            holder.imagRecyclerView.setLayoutManager(gridLayoutManager);
            holder.imagRecyclerView.setAdapter(new headerAdapter(comments.get(position).getImages()));
            holder.date.setText(comment.getTime());
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        class CommentsHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private ImageView touxiang;
            private ExpandableTextView content;
            private RecyclerView imagRecyclerView;
            private TextView date;


            public CommentsHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.scenic_comments_item_name);
                touxiang = (ImageView) itemView.findViewById(R.id.scenic_comments_item_touxiang);
                content = (ExpandableTextView) itemView.findViewById(R.id.scenic_comments_item_expand_textview);
                imagRecyclerView = (RecyclerView) itemView.findViewById(R.id.scenic_comments_item_recylerview);
                date=(TextView)itemView.findViewById(R.id.scenic_comments_item_time);
            }
        }

        class headerAdapter extends RecyclerView.Adapter<headerAdapter.headerItemHolder>{
            DynamicFragment.ImgListener imgListener;
            ArrayList<String> commentImags;

            public headerAdapter(ArrayList<String> strings) {
                super();
                commentImags=strings;
            }

            @Override
            public headerAdapter.headerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new headerAdapter.headerItemHolder(LayoutInflater.from(getActivity()).inflate(R.layout.header_item_layout, parent, false));
            }

            @Override
            public void onBindViewHolder(final headerAdapter.headerItemHolder holder, final int position) {
                Glide.with(context).load(commentImags.get(position)).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imageView);
            }

            @Override
            public int getItemCount() {
                return commentImags.size();
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
