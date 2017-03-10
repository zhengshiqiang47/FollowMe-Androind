package com.example.coderqiang.followme.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Fragment.DynamicFragment;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.DynamicComment;
import com.example.coderqiang.followme.Model.DynamicImage;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.DateUtil;
import com.example.coderqiang.followme.Util.ServerUtil;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/3/8.
 */

public class DynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG="DynamicAdapter";
    Activity context;
    private ArrayList<Dynamic> dynamics;

    public DynamicAdapter(Activity context, ArrayList<Dynamic> dynamics) {
        this.context = context;
        this.dynamics = dynamics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DynamicAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.dynamic_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextWatcher textWatcher;
        final int tempPosition=position;
        final DynamicAdapter.MyViewHolder myViewHolder = (DynamicAdapter.MyViewHolder) holder;
        final Dynamic dynamic = dynamics.get(position);
        myViewHolder.dynamicItemRecyclerView.setAdapter(null);
        myViewHolder.dynamicUsername.setText(dynamic.getUserName());
        if(myViewHolder.dynamicCommentEditText.getTag() instanceof TextWatcher)
            myViewHolder.dynamicCommentEditText.removeTextChangedListener((TextWatcher) (((DynamicAdapter.MyViewHolder) holder).dynamicCommentEditText.getTag()));
        myViewHolder.dynamicCommentEditText.setText(dynamic.getMemo());
        Glide.with(context).load("http://123.206.195.52:8080/day_30/upload/"+dynamic.getUserName()+".png").asBitmap().skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE).override(300,300).placeholder(R.drawable.em_default_avatar).centerCrop().into(myViewHolder.dynamicAvatar);
        textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG,"设置memo"+s);
                dynamics.get(tempPosition).setMemo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        myViewHolder.dynamicCommentEditText.addTextChangedListener(textWatcher);
        myViewHolder.dynamicCommentEditText.setTag(textWatcher);
        myViewHolder.dynamicCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DynamicComment dynamicComment = new DynamicComment();
                dynamicComment.setCommenterId(User.get(context.getApplicationContext()).getId());
                dynamicComment.setCommenterName(User.get(context.getApplicationContext()).getFmUser().getNickName());
                dynamicComment.setDynamicId(dynamic.getDynamicID());
                dynamicComment.setContent(myViewHolder.dynamicCommentEditText.getText().toString());
                dynamicComment.setTimestamp(System.currentTimeMillis());
                dynamicComment.setUserId(dynamic.getUserID());
                dynamicComment.setUserName(dynamic.getUserName());
                dynamicComment.setTime(DateUtil.getDateToStringNormal(System.currentTimeMillis()));
                DynamicAdapter.CommentAdapter commentAdapter=(DynamicAdapter.CommentAdapter) myViewHolder.commentRecycler.getAdapter();
                if(commentAdapter.getComments()==null){
                    commentAdapter.setComments(new ArrayList<DynamicComment>());
                    dynamic.setDynamicComments(new ArrayList<DynamicComment>());
                }
                //隐藏键盘和情况输入栏
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                myViewHolder.dynamicCommentEditText.setText("");

                dynamic.getDynamicComments().add(dynamicComment);
                commentAdapter.notifyDataSetChanged();
                //上传评论
                Observable.create(new Observable.OnSubscribe<Object>() {

                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
//                            Log.i(TAG, "上传评论:" + dynamicComment.getContent()+" id"+dynamic.getUserID());
                        ServerUtil.uploadDynamicComment(context, dynamicComment);
                    }
                }).subscribeOn(Schedulers.io()).subscribe();
            }
        });
        myViewHolder.dynamicLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Dynamic dynamic1:dynamics){
                    Log.e(TAG, "imageName:"+dynamic1.getImageName());
                }
            }
        });
        myViewHolder.dynamicContent.setText(dynamic.getContent());
        myViewHolder.dynamicLocation.setText(dynamic.getAddress());
        myViewHolder.dynamicTime.setText(DateUtil.getDateToStringNormal(dynamic.getTimeStamp()));
        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ArrayList<DynamicImage> dynamicImages=dynamic.getDynamicImages();
        myViewHolder.dynamicItemRecyclerView.setLayoutManager(gridLayoutManager);
        myViewHolder.dynamicItemRecyclerView.setAdapter(new ImageAddAdapter(ImageAddAdapter.VIEW_DYNAMIC,null,context,dynamicImages));
        myViewHolder.commentRecycler.setLayoutManager(new LinearLayoutManager(context));
        myViewHolder.commentRecycler.setAdapter(new DynamicAdapter.CommentAdapter(dynamic.getDynamicComments()));
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
        RecyclerView commentRecycler;

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
            commentRecycler = (RecyclerView) itemView.findViewById(R.id.dynamic_comment_recycler);
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter{
        private ArrayList<DynamicComment> comments=new ArrayList<>();

        public CommentAdapter(ArrayList<DynamicComment> comments) {
            this.comments = comments;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DynamicAdapter.CommentAdapter.CommentHolder(LayoutInflater.from(context).inflate(R.layout.item_dynamic_comment_recycler,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CommentHolder commentHolder=(CommentHolder)holder;
            DynamicComment dynamicComment = comments.get(position);
            commentHolder.name.setText(dynamicComment.getCommenterName()+":");
            commentHolder.content.setText(dynamicComment.getContent());
            commentHolder.time.setText(DateUtil.getDateToStringNormal(dynamicComment.getTimestamp()));
        }

        public ArrayList<DynamicComment> getComments(){
            return comments;
        }

        public void setComments(ArrayList<DynamicComment> comments) {
            this.comments = comments;
        }

        @Override
        public int getItemCount() {
            return comments==null? 0:comments.size();
        }

        private class CommentHolder extends RecyclerView.ViewHolder{
            private TextView name;
            private TextView content;
            private TextView time;

            public CommentHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.item_comment_name);
                content = (TextView) itemView.findViewById(R.id.item_comment_content);
                time = (TextView) itemView.findViewById(R.id.item_comment_time);
            }
        }
    }
}
