package com.example.coderqiang.followme.Activity;

import static com.example.coderqiang.followme.Util.ServerUtil.BASE_URL;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyInfo;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.bumptech.glide.Glide;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.UserUtil;
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
 * Created by CoderQiang on 2017/3/10.
 */

public class NearbyActivity extends Activity implements RadarSearchListener {
    @Bind(R.id.imagefilter_cancle)
    ImageView imagefilterCancle;
    @Bind(R.id.nearby_back)
    LinearLayout nearbyBack;
    @Bind(R.id.nearby_recycler)
    RecyclerView nearbyRecycler;
    Activity activity;

    private ArrayList<FMUser> fmusers=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);
        activity=this;
        initView();

    }

    private void initView() {
        RadarNearbySearchOption option=new RadarNearbySearchOption().centerPt(new LatLng(MyLocation.getMyLocation(getApplicationContext()).getLatitute(),MyLocation.getMyLocation(getApplicationContext()).getLongtitute()))
                .pageNum(0).radius(3000);
        MainActivity.mManager.nearbyInfoRequest(option);
        MainActivity.mManager.addNearbyInfoListener(this);
        MainActivity.mManager.setUserID(User.get(getApplicationContext()).getId()+"");
        RadarUploadInfo info=new RadarUploadInfo();
        info.comments=User.get(getApplicationContext()).getName();
        info.pt=new LatLng(MyLocation.getMyLocation(this).getLatitute(),MyLocation.getMyLocation(this).getLongtitute());
        Log.i("Nearby",MyLocation.getMyLocation(this).getLatitute()+"");
        MainActivity.mManager.uploadInfoRequest(info);
        nearbyRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    public void onGetNearbyInfoList(RadarNearbyResult radarNearbyResult, RadarSearchError error) {
          if (error == RadarSearchError.RADAR_NO_ERROR) {
          		Toast.makeText(activity, "查询周边成功", Toast.LENGTH_LONG).show();
              Log.i("Nearby", radarNearbyResult.infoList.size()+" ");
          		//获取成功，处理数据
              for (int i=0;i<radarNearbyResult.infoList.size();i++){
                  RadarNearbyInfo info=radarNearbyResult.infoList.get(i);
                  FMUser fmuser=new FMUser();
                  fmuser.setDistance(info.distance);
                  fmuser.setId(Integer.parseInt(info.userID));
                  fmuser.setUserName(info.comments);
                  if (fmuser.getId()==User.get(getApplicationContext()).getId()){
                      continue;
                  }
                  fmusers.add(fmuser);

              }
              NearbyAdapter adapter=new NearbyAdapter();
              nearbyRecycler.setAdapter(adapter);
          	} else {
          		//获取失败
          		Toast.makeText(activity, "查询周边失败"+error.name(), Toast.LENGTH_LONG).show();
          	}

    }

    @Override
    public void onGetUploadState(RadarSearchError radarSearchError) {

    }

    @Override
    public void onGetClearInfoState(RadarSearchError radarSearchError) {

    }

    private class NearbyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NearbyHolder(LayoutInflater.from(activity).inflate(R.layout.item_nearby,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            NearbyHolder nearbyHolder=(NearbyHolder)holder;
            final FMUser user=fmusers.get(position);
            try{
                Glide.with(activity).load(BASE_URL+"upload/"+user.getUserName()+".png").asBitmap().error(R.drawable.em_default_avatar).into(nearbyHolder.itemFriendAvator);
            }catch (Exception e){
                Glide.with(activity).load(R.drawable.ease_default_avatar).asBitmap().into(nearbyHolder.itemFriendAvator);
            }

            nearbyHolder.itemFriendName.setText(user.getUserName());
            nearbyHolder.itemFriendAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriend(user);
                }
            });
            nearbyHolder.distance.setText("距我 "+user.getDistance()+" 米");
        }

        @Override
        public int getItemCount() {
            return fmusers==null?0:fmusers.size();
        }

        private class NearbyHolder extends RecyclerView.ViewHolder{
            CircleImagview itemFriendAvator;
            TextView itemFriendName;
            ImageView itemFriendAdd;
            TextView distance;

            public NearbyHolder(View itemView) {
                super(itemView);
                itemFriendAvator=(CircleImagview)itemView.findViewById(R.id.item_nearby_avator);
                itemFriendAdd=(ImageView)itemView.findViewById(R.id.item_nearby_add);
                itemFriendName=(TextView)itemView.findViewById(R.id.item_nearby_name);
                distance = (TextView) itemView.findViewById(R.id.item_nearby_distance);
            }
        }
    }

    private void addFriend(final FMUser fmuser) {

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                FMUser user= User.get(getApplicationContext()).getFmUser();
                boolean isAdd= UserUtil.addFriend(user.getId(),fmuser.getId(),user.getUserName(),fmuser.getUserName());
                if (isAdd) {
                    try {
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
                Toast.makeText(activity,"添加成功",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                Toast.makeText(activity,"添加失败",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
