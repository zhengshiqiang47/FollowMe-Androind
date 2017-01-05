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

import com.example.coderqiang.followme.Activity.ChinaActivity;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.R;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/12/30.
 */

public class CitySeletctDialog extends Dialog {
    public static final int TYPE_CITY=1;
    public static final int TYPE_PROVINCE=2;
    Window window;
    Context context;
    CitySeletctDialog dialog;
    ArrayList<String> cities;
    int x;
    int y;
    int type;
    private DialogCallback dialogCallback;
    private RecyclerView recyclerView;
    private View customView;
    private TextView title;

    public void setDialogCallback(DialogCallback dialogCallback) {
        this.dialogCallback = dialogCallback;
    }

    public interface DialogCallback{
        public void refresh(String select, int t);
    }

    public CitySeletctDialog(Context context,int x,int y,ArrayList<String> cities) {
        super(context);
        Log.i("Dialog", "X:" + x + "Y:" + y);
        this.context=context;
        LayoutInflater inflater=LayoutInflater.from(context);
        customView=inflater.inflate(R.layout.city_select_dialog,null);
        if (cities.contains("福建")){
            type=TYPE_PROVINCE;
            Log.i("Dialog","省份类型");
        }else type=TYPE_CITY;
        this.cities=cities;
    }

    public CitySeletctDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
        LayoutInflater inflater=LayoutInflater.from(context);
        customView=inflater.inflate(R.layout.city_select_dialog,null);
    }

    protected CitySeletctDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(customView);
        windowDeploy(x,y);
        dialog=this;
        title=(TextView)findViewById(R.id.city_select_dilog_title);
        title.setText("选择城市");
        recyclerView=(RecyclerView)findViewById(R.id.city_select_dilog_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CityAdapter());
    }

    public View getCustomView() {
        return customView;
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

    private class CityAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CityHolder(LayoutInflater.from(context).inflate(R.layout.city_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
            CityHolder cityHolder=(CityHolder)holder;
            final int tempPositon=position;
            cityHolder.textView.setText(cities.get(position));
            cityHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Dialog","点击了:"+cities.get(tempPositon));
                    if(dialogCallback!=null){
                        dialogCallback.refresh(cities.get(tempPositon),type);
                    }
                    onBackPressed();
                }
            });
        }

        @Override
        public int getItemCount() {
            return cities.size();
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
