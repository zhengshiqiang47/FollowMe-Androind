package com.example.coderqiang.followme.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.coderqiang.followme.R;
import com.nightonke.boommenu.BoomMenuButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/1.
 */

public class UserinfoFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_uerinfo_detail_2, container, false);
        ButterKnife.bind(this, v);
        loadImg();
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
