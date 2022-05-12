package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.TravelPlan;
import com.example.coderqiang.followme.Model.TravlePlanLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.DateUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/3/10.
 */

public class ShowTravlePlanAcvitity extends Activity {

    @Bind(R.id.show_travelplan_recyclery)
    RecyclerView showTravelplanRecyclery;

    List<TravelPlan> plans;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_travelplan);
        ButterKnife.bind(this);
        plans= TravlePlanLab.get(getApplicationContext()).getTravelPlans();
        activity=this;
        initView();
    }

    private void initView() {
        showTravelplanRecyclery.setLayoutManager(new LinearLayoutManager(this));
        showTravelplanRecyclery.setAdapter(new PlanAdapter());
    }

    private class PlanAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PlanHodler(LayoutInflater.from(activity).inflate(R.layout.item_show_travelplan, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PlanHodler planHolder=(PlanHodler)holder;
            final TravelPlan travelPlan =plans.get(position);
            City city=CityLab.get(getApplicationContext()).isContain(travelPlan.getCityName());
            Glide.with(activity).load(city.getIamgeUrls().get(0)).into(planHolder.cityImage);
            planHolder.name.setText(travelPlan.getTravleName()+" "+ DateUtil.getDateToString(travelPlan.getTime()));
            planHolder.setCurButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TravlePlanLab.get(getApplicationContext()).setCurrentPlan(travelPlan);
                    EventBus.getDefault().post("plan");
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return plans==null?0:plans.size();
        }

        private class PlanHodler extends RecyclerView.ViewHolder{
            ImageView cityImage;
            TextView name;
            Button setCurButton;

            public PlanHodler(View itemView) {
                super(itemView);
                cityImage = (ImageView) itemView.findViewById(R.id.item_show_travelplan_image);
                name=(TextView)itemView.findViewById(R.id.item_show_travelplan_name);
                setCurButton=(Button)itemView.findViewById(R.id.item_show_travelplan_button);
            }
        }
    }
}
