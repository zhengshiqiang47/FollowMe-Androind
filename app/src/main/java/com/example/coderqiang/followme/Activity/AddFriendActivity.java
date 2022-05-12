package com.example.coderqiang.followme.Activity;

import static com.example.coderqiang.followme.Util.ServerUtil.BASE_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.FriendsLab;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.ServerUtil;
import com.example.coderqiang.followme.Util.UploadImage;
import com.example.coderqiang.followme.Util.UserUtil;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/3/5.
 */

public class AddFriendActivity extends Activity {
    private static final String TAG = "AddFriendActivity";
    @Bind(R.id.addfriend_back)
    ImageView addfriendBack;
    @Bind(R.id.imagefilter_right)
    ImageView imagefilterRight;
    @Bind(R.id.imagefilter_title)
    LinearLayout imagefilterTitle;
    @Bind(R.id.addfriend_search)
    SearchView addfriendSearch;
    @Bind(R.id.addfriend_recycler)
    RecyclerView addfriendRecycler;

    ArrayList<FMUser> fmUsers=new ArrayList<>();
    ArrayList<FMUser> friends=new ArrayList<>();
    Activity activtiy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_addfriend);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.journey_green));
        activtiy = this;
        ButterKnife.bind(this);
        getFriend();
        initView();
    }

    private void initView() {
        addfriendRecycler.setLayoutManager(new LinearLayoutManager(activtiy));
        addfriendRecycler.setAdapter(new FriendAdapter());
        addfriendSearch.setIconifiedByDefault(true);
        addfriendSearch.setFocusable(true);
        addfriendSearch.setIconified(false);
        addfriendSearch.requestFocusFromTouch();
        addfriendSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getUser(newText);
                return false;
            }

        });
    }

    private void getUser(String newText) {
        try {
            final int id=Integer.parseInt(newText);
            fmUsers.clear();
            Observable.create(new Observable.OnSubscribe<FMUser>() {

                @Override
                public void call(Subscriber<? super FMUser> subscriber) {
                    FMUser fmuser= UserUtil.getFMUser(id);
                    Log.i(TAG, "fmuser:" + fmuser.getUserName());
                    subscriber.onNext(fmuser);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<FMUser>() {

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(FMUser fmuser) {
                    if (fmuser != null) {
                        boolean flag=true;
                        for (FMUser user:friends){
                            if(user.getUserName().equals(fmuser.getUserName())) flag=false;
                        }
                        if (flag) fmUsers.add(fmuser);
                    }
                    addfriendRecycler.getAdapter().notifyDataSetChanged();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getFriend() {
        Observable.create(new Observable.OnSubscribe<FMUser>() {
            @Override
            public void call(Subscriber<? super FMUser> subscriber) {
                friends= UserUtil.getFriend(User.get(activtiy.getApplicationContext()).getFmUser().getId());
                FriendsLab.get(getApplicationContext()).setFMUsers(friends);
                Log.i(TAG, "frends.size:" + friends.size());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<FMUser>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FMUser fmuser) {
            }
        });
    }

    public class FriendAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(activtiy).inflate(R.layout.item_addfriend, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemHolder itemHolder=(ItemHolder) holder;
            final FMUser fmuser = fmUsers.get(position);
            itemHolder.itemFriendName.setText(fmuser.getUserName()+"");
            Glide.with(activtiy).load(BASE_URL+"upload/"+fmuser.getUserName()+".png").asBitmap().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE).override(300,300).placeholder(R.drawable.em_default_avatar).centerCrop().into(itemHolder.itemFriendAvator);
            itemHolder.itemFriendAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriend(fmuser);
                }
            });
            itemHolder.itemFriendName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activtiy, UserDetailInfoActivity.class);
                    intent.putExtra(UserDetailInfoActivity.EXTRA_USERID,fmuser.getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return fmUsers.size();
        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            CircleImagview itemFriendAvator;
            TextView itemFriendName;
            ImageView itemFriendAdd;

            public ItemHolder(View itemView) {
                super(itemView);
                itemFriendAvator=(CircleImagview)itemView.findViewById(R.id.item_friend_avator);
                itemFriendName = (TextView) itemView.findViewById(R.id.item_friend_name);
                itemFriendAdd = (ImageView) itemView.findViewById(R.id.item_friend_add);
            }
        }
    }

    private void addFriend(final FMUser fmuser) {

        Observable.create(new Observable.OnSubscribe<Object>() {

            @Override
            public void call(Subscriber<? super Object> subscriber) {
                FMUser user= User.get(getApplicationContext()).getFmUser();
                boolean isAdd=UserUtil.addFriend(user.getId(),fmuser.getId(),user.getUserName(),fmuser.getUserName());
                if (isAdd) {
                    try {
                        Log.i(TAG,"EMAdd");
                        EMClient.getInstance().contactManager().addContact(fmuser.getUserName(), "");
                        subscriber.onCompleted();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
                else
                    subscriber.onNext(null);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

            @Override
            public void onCompleted() {
                Toast.makeText(activtiy,"添加成功",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                Toast.makeText(activtiy,"添加失败",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
