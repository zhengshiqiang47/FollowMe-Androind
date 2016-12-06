package com.example.coderqiang.followme.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.coderqiang.followme.Activity.PictureActivity;
import com.example.coderqiang.followme.Activity.WebViewActivity;
import com.example.coderqiang.followme.R;
import com.nightonke.boommenu.BoomMenuButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/1.
 */

public class UserinfoFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    @Bind(R.id.userinfo_my_message)
    LinearLayout myMessageLayout;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_uerinfo_detail_2, container, false);
        ButterKnife.bind(this, v);
        loadImg();
        myMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.TYPE,WebViewActivity.TYPE_URL);
                intent.putExtra(WebViewActivity.WEB_URL,"http://you.ctrip.com/travels/fuzhou164/2639504.html");
                startActivity(intent);
            }
        });
        return v;
    }

    private void loadImg() {
//        Glide.with(this)
//                .load("http://dl.bizhi.sogou.com/images/2012/02/21/92347.jpg")
//                .centerCrop()
//                .crossFade()
//                .placeholder(R.drawable.geometry)
//                .into(userImg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
