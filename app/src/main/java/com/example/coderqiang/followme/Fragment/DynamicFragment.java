package com.example.coderqiang.followme.Fragment;

import static com.example.coderqiang.followme.Util.ServerUtil.BASE_URL;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Adapter.ImageAddAdapter;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.Comment;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.DynamicComment;
import com.example.coderqiang.followme.Model.DynamicImage;
import com.example.coderqiang.followme.Model.DynamicLab;
import com.example.coderqiang.followme.Model.SettingLab;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.DateUtil;
import com.example.coderqiang.followme.Util.DynamicComparator;
import com.example.coderqiang.followme.Util.ServerUtil;
import com.example.coderqiang.followme.Util.UploadImage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    ArrayList<String> str = new ArrayList<>();
    ArrayList<Dynamic> dynamics;

    Fragment context;

    interface ImgListener {
        void onClick(int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dynamic, container, false);
        ButterKnife.bind(this, v);
        context=this;
        initData();
        initView();
        return v;
    }

    private void initData() {
        dynamics=DynamicLab.get(getActivity().getApplicationContext()).getDynamics();
        getDynamic();
    }

    private void getDynamic() {
        dynamics.clear();
        if(dynamics==null||dynamics.size()==0)
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    ServerUtil.getDynamic(getActivity().getApplicationContext(), User.get(getActivity()).getFmUser());
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                @Override
                public void onCompleted() {
                    Log.i(TAG,"获取动态成功");
                    initView();
                    refreshLayout.finishRefreshing();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {

                }
            });
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
//        Collections.sort(dynamics,new DynamicComparator());
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayout);
        MyAdapter adapter = new MyAdapter();
        mRecyclerview.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        ServerUtil.getDynamic(getActivity().getApplicationContext(), User.get(getActivity()).getFmUser());
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG,"获取动态成功");
                        initView();
                        refreshLayout.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
                User.get(getActivity().getApplicationContext()).getFmUser().setDynamicIndex(1);
                getDynamic();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        ServerUtil.getDynamic(getActivity().getApplicationContext(), User.get(getActivity()).getFmUser());
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG,"获取更多动态成功");
                        initView();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            TextWatcher textWatcher;
            final int tempPosition=position;
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            final Dynamic dynamic = dynamics.get(position);

            myViewHolder.dynamicItemRecyclerView.setAdapter(null);
            myViewHolder.dynamicUsername.setText(dynamic.getUserName());
            if(myViewHolder.dynamicCommentEditText.getTag() instanceof TextWatcher)
                myViewHolder.dynamicCommentEditText.removeTextChangedListener((TextWatcher) (((MyViewHolder) holder).dynamicCommentEditText.getTag()));
            myViewHolder.dynamicCommentEditText.setText(dynamic.getMemo());
            Glide.with(context).load(BASE_URL + "upload/"+dynamic.getUserName()+".png").asBitmap().skipMemoryCache(false)
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
                    dynamicComment.setCommenterId(User.get(getActivity().getApplicationContext()).getId());
                    dynamicComment.setCommenterName(User.get(getActivity().getApplicationContext()).getFmUser().getNickName());
                    dynamicComment.setDynamicId(dynamic.getDynamicID());
                    dynamicComment.setContent(myViewHolder.dynamicCommentEditText.getText().toString());
                    dynamicComment.setTimestamp(System.currentTimeMillis());
                    dynamicComment.setUserId(dynamic.getUserID());
                    dynamicComment.setUserName(dynamic.getUserName());
                    dynamicComment.setTime(DateUtil.getDateToStringNormal(System.currentTimeMillis()));
                    CommentAdapter commentAdapter=(CommentAdapter) myViewHolder.commentRecycler.getAdapter();
                    if(commentAdapter.getComments()==null){
                        commentAdapter.setComments(new ArrayList<DynamicComment>());
                        dynamic.setDynamicComments(new ArrayList<DynamicComment>());
                    }
                    //隐藏键盘和情况输入栏
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    myViewHolder.dynamicCommentEditText.setText("");

                    dynamic.getDynamicComments().add(dynamicComment);
                    commentAdapter.notifyDataSetChanged();
                    //上传评论
                    Observable.create(new Observable.OnSubscribe<Object>() {

                        @Override
                        public void call(Subscriber<? super Object> subscriber) {
//                            Log.i(TAG, "上传评论:" + dynamicComment.getContent()+" id"+dynamic.getUserID());
                            ServerUtil.uploadDynamicComment(getActivity(), dynamicComment);
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
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            ArrayList<DynamicImage> dynamicImages=dynamic.getDynamicImages();
            myViewHolder.dynamicItemRecyclerView.setLayoutManager(gridLayoutManager);
            myViewHolder.dynamicItemRecyclerView.setAdapter(new ImageAddAdapter(ImageAddAdapter.VIEW_DYNAMIC,null,getActivity(),dynamicImages));
            myViewHolder.commentRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            myViewHolder.commentRecycler.setAdapter(new CommentAdapter(dynamic.getDynamicComments()));
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
                return new CommentHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_dynamic_comment_recycler,parent,false));
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
}
