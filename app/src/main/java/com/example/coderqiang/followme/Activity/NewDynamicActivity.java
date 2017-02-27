package com.example.coderqiang.followme.Activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.example.coderqiang.followme.Adapter.ImageAddAdapter;
import com.example.coderqiang.followme.Fragment.DynamicFragment;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.DynamicLab;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.DateUtil;
import com.example.coderqiang.followme.Util.UserUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CoderQiang on 2017/2/16.
 */

public class NewDynamicActivity extends FragmentActivity {

    private static final int PHOTO_REQUEST=0;

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

    private ArrayList<Uri> uris=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dynamic);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        newDynamicRecyclerView.setLayoutManager(gridLayoutManager);
        newDynamicRecyclerView.setAdapter(new ImageAddAdapter(ImageAddAdapter.VIEW_NEW_DYNAMIC,uris,this));
        newDynamicTime.setText(DateUtil.getDateToStringMD(System.currentTimeMillis()));
        if(MyLocation.getMyLocation(getApplicationContext()).getBdLocation()!=null){
            BDLocation bdLocation=MyLocation.getMyLocation(getApplicationContext()).getBdLocation();
            newDynamicAddress.setText(bdLocation.getCity()+" "+bdLocation.getLocationDescribe().replace("附近","").replace("在",""));
        }
        newDynamicPublicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=User.get(getApplicationContext());
                ArrayList<Uri> addUri=new ArrayList<Uri>();
                addUri.addAll(uris);
                Dynamic dynamic=new Dynamic(user.getName(),user.getId(),newDynamicEditText.getText().toString(),newDynamicAddress.getText().toString(),System.currentTimeMillis(),addUri);
                DynamicLab.get(getApplicationContext()).getDynamics().add(dynamic);
//                EventBus.getDefault().post("dynamicFragment");

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
