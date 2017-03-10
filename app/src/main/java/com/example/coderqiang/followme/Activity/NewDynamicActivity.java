package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.example.coderqiang.followme.Adapter.ImageAddAdapter;
import com.example.coderqiang.followme.Fragment.DynamicFragment;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.DynamicImage;
import com.example.coderqiang.followme.Model.DynamicLab;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.DateUtil;
import com.example.coderqiang.followme.Util.ServerUtil;
import com.example.coderqiang.followme.Util.UploadImage;
import com.example.coderqiang.followme.Util.UserUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/2/16.
 */

public class NewDynamicActivity extends FragmentActivity {

    private static final String TAG="NewDynamicActivity";
    private static final int PHOTO_REQUEST=0;
    public static final int FILTER_REQUEST=1;
    public static final String EXTRA_POSITION="position";

    @Bind(R.id.new_dynamic_backicon)
    ImageView newDynamicBackicon;
    @Bind(R.id.new_dynamic_editText)
    EditText newDynamicEditText;
    @Bind(R.id.new_dynamic_recyclerView)
    RecyclerView newDynamicRecyclerView;
    @Bind(R.id.new_dynamic_address)
    TextView newDynamicAddress;
    @Bind(R.id.new_dynamic_time)
    TextView newDynamicTime;
    @Bind(R.id.new_dynamic_publicButton)
    Button newDynamicPublicButton;
    @Bind(R.id.new_dynamic_progress_layout)
    RelativeLayout progressLayout;

    Dynamic dynamic;
    BDLocation bdLocation;
    ImageAddAdapter addAdapter;
    Activity context;

    private ArrayList<Uri> uris=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dynamic);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.journey_green));
        ButterKnife.bind(this);
        context=this;
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        newDynamicRecyclerView.setLayoutManager(gridLayoutManager);
        addAdapter=new ImageAddAdapter(ImageAddAdapter.VIEW_NEW_DYNAMIC,uris,this,null);
        newDynamicRecyclerView.setAdapter(addAdapter);
        newDynamicTime.setText(DateUtil.getDateToStringMD(System.currentTimeMillis()));
        if(MyLocation.getMyLocation(getApplicationContext()).getBdLocation()!=null){
            bdLocation=MyLocation.getMyLocation(getApplicationContext()).getBdLocation();
            newDynamicAddress.setText(bdLocation.getCity()+" "+bdLocation.getLocationDescribe().replace("附近","").replace("在",""));
        }
        newDynamicPublicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
                progressLayout.setVisibility(View.VISIBLE);
                progressLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
//                EventBus.getDefault().post("dynamicFragment");

            }
        });

    }

    private void upload() {
        User user=User.get(getApplicationContext());
        ArrayList<Uri> addUri=new ArrayList<Uri>();
        addUri.addAll(uris);
        dynamic=new Dynamic(user.getName(),user.getId(),newDynamicEditText.getText().toString(),newDynamicAddress.getText().toString(),System.currentTimeMillis(),addUri,0,0);
        if(bdLocation!=null){
            dynamic.setLatitude(bdLocation.getLatitude());
            dynamic.setLongtitude(bdLocation.getLongitude());
        }
        Observable.create(new Observable.OnSubscribe<Dynamic>() {
            @Override
            public void call(Subscriber<? super Dynamic> subscriber) {
                int id= ServerUtil.uploadDynamic(getApplicationContext(),dynamic);
                if(id!=0){
                    int flag=1;
                    ArrayList<DynamicImage> dynamicImages = new ArrayList<DynamicImage>();
                    for(int i=0;i<dynamic.getImagURL().size();i++){
                        String res= UploadImage.DynamicImgUpload(dynamic.getImagURL().get(i),id+""+i,context);
                        DynamicImage dynamicImage=new DynamicImage();
                        dynamicImage.setDynamicId(id);
                        dynamicImage.setName(res);
                        dynamicImages.add(dynamicImage);
                    }
                    String imageName=new Gson().toJson(dynamicImages);
                    dynamic.setDynamicImages(dynamicImages);
                    ServerUtil.uploadDynamicImageName(context,imageName,id);
                    subscriber.onNext(dynamic);
                }else {
                    subscriber.onCompleted();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Dynamic>() {
            @Override
            public void onCompleted() {
                Toast.makeText(getApplicationContext(),"动态发布失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Dynamic dynamic) {
                DynamicLab.get(getApplicationContext()).getDynamics().add(dynamic);
                Toast.makeText(getApplicationContext(),"动态发布成功",Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post("dynamicFragment");
                progressLayout.setVisibility(View.GONE);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PHOTO_REQUEST){
            try {
                if (data != null) {
                    Uri originalUri = data.getData();        //获得图片的uri
//                    Log.i("Select",originalUri.getPath());
//                    path=originalUri.getPath()
                    uris.add(originalUri);
                    newDynamicRecyclerView.getAdapter().notifyItemInserted(uris.size()-1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(resultCode==RESULT_OK){
            if(requestCode==FILTER_REQUEST){
                int position=data.getIntExtra(EXTRA_POSITION,-1);
                if(position!=-1){
                    Uri uri=Uri.parse(data.getStringExtra("uri"));
                    addAdapter.getUris().remove(position);
                    addAdapter.getUris().add(position,uri);
                    newDynamicRecyclerView.getAdapter().notifyItemChanged(position);
                }
            }
        }
    }

    @OnClick({R.id.new_dynamic_backicon, R.id.new_dynamic_publicButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_dynamic_backicon:
                onBackPressed();
                break;
            case R.id.new_dynamic_publicButton:
                break;
        }
    }


}
