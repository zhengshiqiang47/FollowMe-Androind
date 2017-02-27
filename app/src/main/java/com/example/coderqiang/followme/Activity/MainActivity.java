package com.example.coderqiang.followme.Activity;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
//
//import com.alibaba.mobileim.IYWLoginService;
//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.mobileim.YWAccountType;
//import com.alibaba.mobileim.YWIMKit;
//import com.alibaba.mobileim.YWLoginParam;
//import com.alibaba.mobileim.aop.AdviceBinder;
//import com.alibaba.mobileim.aop.PointCutEnum;
//import com.alibaba.mobileim.channel.event.IWxCallback;
//import com.alibaba.mobileim.contact.IYWContactService;
//import com.alibaba.mobileim.ui.contact.ContactsFragment;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.example.coderqiang.followme.Fragment.EaseContactListFragment;
import com.example.coderqiang.followme.Fragment.JourneyFragment;
import com.example.coderqiang.followme.Fragment.ScenicCommentsFragment;
import com.example.coderqiang.followme.Fragment.ScenicMainFragment;
import com.example.coderqiang.followme.Fragment.SquarFragment;
import com.example.coderqiang.followme.Fragment.UserinfoFragment;
import com.example.coderqiang.followme.IMAbout.Model.ConversationListFragment;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpAnalyze;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.Util.SetStatusColor;
import com.example.coderqiang.followme.Util.TravelPlanUtil;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private static final String TAG="MainActivity";
    FragmentManager fm;
    public JourneyFragment journeyFragment;
    UserinfoFragment userinfoFragment;
    SquarFragment squarFragment;
    ScenicMainFragment scenicMainFragment;
    EaseContactListFragment easeContactListFragment;
    ConversationListFragment conversationListFragment;
    private int currentTabIndex;
    private TextView unreadLabel;
    Fragment currentFragment;
    @Bind(R.id.journey_nav)
    BottomNavigationBar bottomNavigationBar;
    @Bind(R.id.journey_day_fab)
    FloatingActionButton journeyFab;
    @Bind(R.id.journey_menu_hide_text)
    FrameLayout menuHideTv;
    @Bind(R.id.menu_new_day_layout)
    LinearLayout menuDayLayout;
    @Bind(R.id.journey_day_menu_layout)
    LinearLayout menuLayout;
    @Bind(R.id.menu_new_journey_layout)
    LinearLayout menuJourneyLayout;
    @Bind(R.id.menu_new_journey_note_layout)
    LinearLayout menuNoteLayou;
    @Bind(R.id.menu_new_journey)
    TextView menuNewJourney;
    @Bind(R.id.menu_add_scenic_layout)
    LinearLayout menuAddScenicLayout;

    FragmentManager fmV4;
    Context context;
    Boolean isV4=false;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fmV4=getSupportFragmentManager();
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        context=this;
        fm =this.getSupportFragmentManager();
        setDefaultFragment();
        initNavigation();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
                Log.i(TAG, "连接上了");
            }

            @Override
            public void onDisconnected(int i) {
                Log.i(TAG, "连接失败" + i);
            }
        });
        new GetScenicSpot().execute();
        SetStatusColor.MIUISetStatusBarLightMode(getWindow(), true);
    }


    @Override
    public void onBackPressed() {
        if(count<2){
            count++;
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    private void initNavigation(){
        menuNoteLayou.setOnClickListener(this);
        registerBroadcastReceiver();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_CLASSIC);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.position1, "日程"))
                .addItem(new BottomNavigationItem(R.mipmap.fire_white,"攻略"))
                .addItem(new BottomNavigationItem(R.mipmap.xingji1,"动态"))
                .addItem(new BottomNavigationItem(R.mipmap.chat, "通讯"))
                .addItem(new BottomNavigationItem(R.drawable.mine_selected,"我的"))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Log.i(TAG, "position" + position);
                switch (position) {
                    case 0:
                        switchFragment(currentFragment,journeyFragment);
                        break;
                    case 1:
                        if(scenicMainFragment==null){
                            scenicMainFragment=new ScenicMainFragment();
                        }
                        switchFragment(currentFragment, scenicMainFragment);
                        break;
                    case 2:
                        if(squarFragment==null){
                            squarFragment=new SquarFragment();
                        }
                        switchFragment(currentFragment,squarFragment);
                        break;
//                    case 3:
//                        if (conversationListFragment == null) {
//                            conversationListFragment=new ConversationListFragment();
//                            conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
//
//                                @Override
//                                public void onListItemClicked(EMConversation conversation) {
//                                    Log.i(TAG, "点击联系人");
//                                    startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
//                                }
//                            });
//                        }
//                        Log.i(TAG,"conversationList");
//                        switchFragment(currentFragment,conversationListFragment);
//
//                        break;
                    case 3:
                        if (conversationListFragment == null) {
                            conversationListFragment=new ConversationListFragment();
                            conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

                                @Override
                                public void onListItemClicked(EMConversation conversation) {
                                    Log.i(TAG, "点击联系人");
                                    startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
                                }
                            });
                        }
                        Log.i(TAG,"conversationList");
                        switchFragment(currentFragment,conversationListFragment);
                        break;
                    case 4:
                        if (userinfoFragment == null) {
                            userinfoFragment=new UserinfoFragment();
                        }
                        switchFragment(currentFragment,userinfoFragment);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        Glide.with(this).load("http://123.206.195.52:8080/day_30/upload/"+ User.get(getApplicationContext()).getName()+".png").asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(new Target<Bitmap>() {
            @Override
            public void onLoadStarted(Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                User.get(getApplicationContext()).setTouxiang(resource);
                Log.i(TAG,"加载头像成功");
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {

            }

            @Override
            public void getSize(SizeReadyCallback cb) {

            }

            @Override
            public void setRequest(Request request) {

            }

            @Override
            public Request getRequest() {
                return null;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {

            }
        });
    }

    private void switchFragment(Fragment from, Fragment to){
        if(currentFragment!=to){
            if(!to.isAdded()){
                fm.beginTransaction().add(R.id.fragment_container,to).hide(from).show(to).commitAllowingStateLoss();
            }else {
                fm.beginTransaction().hide(from).show(to).commitAllowingStateLoss();
//                fm.beginTransaction().replace(R.id.fragment_container, to).commit();
            }

            currentFragment=to;
            Log.i(TAG, "current:" + to.toString());
        }

    }

    private void setDefaultFragment(){
        journeyFragment = JourneyFragment.newInstance();
        android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_container, journeyFragment);
        ft.commit();
        currentFragment=journeyFragment;
        conversationListFragment=new ConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                Log.i(TAG, "点击联系人");
                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
        squarFragment=new SquarFragment();
        userinfoFragment=new UserinfoFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_new_journey_note_layout:
                Log.i(TAG,"写动态");
                Intent intent = new Intent(this, NewDynamicActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class GetScenicSpot extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpParse httpParse=new HttpParse();
            String cityName= MyLocation.getMyLocation(getApplicationContext()).getCityName();
//            cityName="杭州市";
            City city=CityLab.get(getApplicationContext()).isContain(cityName);
            ArrayList<Scenicspot> tempSces= httpParse.getScenicspot(getApplicationContext(), city.getCityName(),city.getScenicPage()+"");
            Log.i(TAG,"hasLocation"+MyLocation.getMyLocation(getApplicationContext()).isHasLocation());
            if(!MyLocation.getMyLocation(getApplicationContext()).isHasLocation()){
                Log.i(TAG,"进到这了");
                city.setScenicspots(tempSces);
            }
            city.addscenicPage();
            Log.e(TAG, ScenicspotLab.get(getApplication()).getScenicspots().size() + "");
            TravelPlanUtil.getTravelPlan(context);
            httpParse.getAllScenicDetails(getApplicationContext(), cityName);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EventBus.getDefault().post("景点数据获取完成");
        }
    }

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(com.example.coderqiang.followme.Model.EaseConstant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(com.example.coderqiang.followme.Model.EaseConstant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG,"收到消息了");
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
                //red packet code : 处理红包回执透传消息
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        EMChatManager emChatManager=EMClient.getInstance().chatManager();
        emChatManager.addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                Log.i(TAG,"消息来了");
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void overrideTransitionEventBus(String msg) {
        Log.i(TAG,"msg:"+msg);
        if(msg.equals("定位改变")){
            Log.i(TAG,msg);
            new GetScenicSpot().execute();
        }else if(msg.equals("dynamicFragment")){
            Log.i(TAG,msg);
            switchFragment(currentFragment,squarFragment);
        }else if(msg.equals("联系人列表")){
            if(easeContactListFragment==null){
                easeContactListFragment=new EaseContactListFragment();
                easeContactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
                    @Override
                    public void onListItemClicked(EaseUser user) {
                        startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername()));
                    }
                });
            }
            switchFragment(currentFragment,easeContactListFragment);
        }else if(msg.equals("会话列表")){
            if (conversationListFragment == null) {
                conversationListFragment=new ConversationListFragment();
                conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

                    @Override
                    public void onListItemClicked(EMConversation conversation) {
                        Log.i(TAG, "点击联系人");
                        startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
                    }
                });
            }
            Log.i(TAG,"conversationList");
            switchFragment(currentFragment,conversationListFragment);
        }

    }

}
