package com.example.coderqiang.followme.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Activity.PictureActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.Comment;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.View.CornersTransform;
import com.example.coderqiang.followme.View.CustomViewPager;
import com.example.coderqiang.followme.View.GlideCircleTransform;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
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
    CommentsAdapter adapter;

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
        ButterKnife.bind(this, v);
        vp.setObjectForPosition(v,1);
        context=this;
        recyclerView = (RecyclerView) v.findViewById(R.id.scenic_comments_recylerview);
        adapter = new CommentsAdapter();
//        Log.i(TAG,"itemCount:"+adapter.getItemCount());
        LinearLayoutManager linearLayout=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        scenicspot.setPageNum(2);
    }

    private class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_FOOTER = 1;
        private static final int TYPE_NORMALE=0;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_NORMALE)
                return new CommentsHolder(LayoutInflater.from(getActivity()).inflate(R.layout.comments_item, parent, false));
            else if (viewType == TYPE_FOOTER) {
                return new FooterHolder(LayoutInflater.from(getActivity()).inflate(R.layout.scenic_comment_foot_item, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            Log.i(TAG, "bind" + position);
            if (viewHolder instanceof CommentsHolder) {
                CommentsHolder holder=(CommentsHolder)viewHolder;
                Comment comment = comments.get(position);
                Log.i(TAG, "" + comment.getContent());
                Glide.with(context).load(comment.getOwnerImag()).transform(new GlideCircleTransform(getActivity())).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.touxiang);
                holder.content.setText(comment.getContent());
                holder.content.setAlpha(1.0f);
                holder.name.setText(comment.getCommentName());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
                holder.imagRecyclerView.setLayoutManager(gridLayoutManager);
                holder.imagRecyclerView.setAdapter(new headerAdapter(comments.get(position).getImgSmals(), comments.get(position).getImages(), position));
                holder.date.setText(comment.getTime());
            }else if(viewHolder instanceof FooterHolder){
                final FooterHolder holder = (FooterHolder) viewHolder;
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    int size;
                    @Override
                    public void onClick(View v) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                        holder.button.setVisibility(View.INVISIBLE);
                        size=comments.size();
                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                HttpParse httpPare=new HttpParse();
                                scenicspot.pageNumPlus();
                                httpPare.getMoreComment(context.getActivity(), comments, scenicspot, scenicspot.getPageNum() + "");
                                subscriber.onNext("");
                                subscriber.onCompleted();
                            }
                        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {
                                adapter.notifyItemRangeChanged(size-1,comments.size()-1);
                                holder.progressBar.setVisibility(View.INVISIBLE);
                                holder.button.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return comments.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position==comments.size()){
                return TYPE_FOOTER;
            }
            return TYPE_NORMALE;
        }

        private class CommentsHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private ImageView touxiang;
            private ExpandableTextView content;
            private RecyclerView imagRecyclerView;
            private TextView date;
            private LinearLayout linearLayout;


            public CommentsHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.scenic_comments_item_name);
                touxiang = (ImageView) itemView.findViewById(R.id.scenic_comments_item_touxiang);
                content = (ExpandableTextView) itemView.findViewById(R.id.scenic_comments_item_expand_textview);
                imagRecyclerView = (RecyclerView) itemView.findViewById(R.id.scenic_comments_item_recylerview);
                date=(TextView)itemView.findViewById(R.id.scenic_comments_item_time);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.scenic_comments_item_layout);
            }
        }

        private class FooterHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;
            ImageView button;
            RelativeLayout layout;
            public FooterHolder(View itemView) {
                super(itemView);
                layout = (RelativeLayout) itemView.findViewById(R.id.scenic_comments_footer_layout);
                button = (ImageView) itemView.findViewById(R.id.scenic_comments_footer_more);
                progressBar = (ProgressBar) itemView.findViewById(R.id.scenic_comments_footer_progress);
            }
        }

        class headerAdapter extends RecyclerView.Adapter<headerAdapter.headerItemHolder>{
            int currentPos=0;
            ArrayList<String> commentImags;
            ArrayList<String> bigCommentImages;

            public headerAdapter(ArrayList<String> strings,ArrayList<String> bigCommentImages,int position) {
                super();
                commentImags=strings;
                this.bigCommentImages=bigCommentImages;
                currentPos=position;
            }

            @Override
            public headerAdapter.headerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new headerAdapter.headerItemHolder(LayoutInflater.from(getActivity()).inflate(R.layout.header_item_layout, parent, false));
            }

            @Override
            public void onBindViewHolder(final headerAdapter.headerItemHolder holder, int position) {
                final int comPosition=position;
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PictureActivity.class);
                        intent.putStringArrayListExtra(PictureActivity.EXTRA_IMGURLS,bigCommentImages);
                        intent.putExtra(PictureActivity.EXTRA_POSITION,comPosition);
                        intent.putExtra(PictureActivity.EXTRA_DESCRIPTION, comments.get(currentPos).getContent());
                        startActivity(intent);
                    }
                });
                Glide.with(context).load(commentImags.get(position)).skipMemoryCache(true).transform(new CornersTransform(getActivity())).into(holder.imageView);
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
