package com.example.coderqiang.followme.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.SettingLab;
import com.example.coderqiang.followme.Model.TravlePlanLab;
import com.example.coderqiang.followme.Model.TravelDay;
import com.example.coderqiang.followme.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderQiang on 2017/1/5.
 */

public class AddScenicDialog extends Dialog {
    Window window;
    Context context;
    int x;
    int y;
    int type;
    private CitySeletctDialog.DialogCallback dialogCallback;
    private RecyclerView recyclerView;
    private View customView;
    private TextView title;

    Scenicspot scenicspot;
    List<TravelDay> travelDays;

    public AddScenicDialog(Context context, int x, int y, Scenicspot scenicspot) {
        super(context);
        this.x=x;
        this.y=y;
        this.context=context;
        LayoutInflater inflater=LayoutInflater.from(context);
        customView=inflater.inflate(R.layout.city_select_dialog,null);
        this.scenicspot=scenicspot;
        travelDays = TravlePlanLab.get(context.getApplicationContext()).getCurrentPlan().getTravelDays();
    }

    public AddScenicDialog(Context context, int themeResId,Scenicspot scenicspot) {
        super(context, themeResId);
        LayoutInflater inflater=LayoutInflater.from(context);
        customView=inflater.inflate(R.layout.city_select_dialog,null);
        this.scenicspot=scenicspot;
        travelDays = TravlePlanLab.get(context.getApplicationContext()).getCurrentPlan().getTravelDays();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(customView);
        windowDeploy(x,y);
        title=(TextView)findViewById(R.id.city_select_dilog_title);
        title.setText("添加"+scenicspot.getScenicName()+":");
        recyclerView=(RecyclerView)findViewById(R.id.city_select_dilog_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CityAdapter());
    }

    public void windowDeploy(int x, int y){
        window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.white); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.width= WindowManager.LayoutParams.MATCH_PARENT;
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移
//            wl.alpha = 0.6f; //设置透明度
//            wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);
    }

    protected AddScenicDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private class CityAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CityHolder(LayoutInflater.from(context).inflate(R.layout.city_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           CityHolder cityHolder=(CityHolder)holder;
            final int tempPositon=position;
            cityHolder.textView.setText("添加到Day "+ travelDays.get(position).getDayNum());
            cityHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Dialog","点击了:"+ travelDays.get(tempPositon).getDayNum());
                    String result= travelDays.get(tempPositon).addScenicSpots(scenicspot);
                    Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
                    if(result.equals("添加成功")){
                        SettingLab.getSettingLab(context.getApplicationContext()).setTravelDayUpdate(true);
                        EventBus.getDefault().post("addscenicspot");
                    }
//                    if(dialogCallback!=null){
//                        dialogCallback.refresh(cities.get(tempPositon),type);
//                    }
                    onBackPressed();
                }
            });
        }

        @Override
        public int getItemCount() {
            return travelDays.size();
        }
    }

    private class CityHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public CityHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.city_item_name);
        }
    }

}
