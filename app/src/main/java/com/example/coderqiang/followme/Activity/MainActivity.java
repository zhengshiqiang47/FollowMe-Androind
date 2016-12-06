package com.example.coderqiang.followme.Activity;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.example.coderqiang.followme.Fragment.JourneyFragment;
import com.example.coderqiang.followme.Fragment.ScenicCommentsFragment;
import com.example.coderqiang.followme.Fragment.ScenicMainFragment;
import com.example.coderqiang.followme.Fragment.SquarFragment;
import com.example.coderqiang.followme.Fragment.UserinfoFragment;
import com.example.coderqiang.followme.IMAbout.Model.ConversationListFragment;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpAnalyze;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.Util.SetStatusColor;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {
    private static final String TAG="MainActivity";
    FragmentManager fm;
    public JourneyFragment journeyFragment;
    UserinfoFragment userinfoFragment;
    SquarFragment squarFragment;
    ScenicMainFragment scenicMainFragment;
    private int currentTabIndex;
    private TextView unreadLabel;
    Fragment currentFragment;
    @Bind(R.id.journey_nav)
    BottomNavigationBar bottomNavigationBar;
    ConversationListFragment conversationListFragment;
    FragmentManager fmV4;
    //    YWIMKit imKit;
//    TLSAccountHelper accountHelper;
//    TLSLoginHelper loginHelper;
//    TLSPwdLoginListener loginListener;
//
//    TLSPwdResetListener resetListener;
//    TLSPwdRegListener pwdRegListener;
    Context context;
    Boolean isV4=false;
    int count=0;
    String num="zhengshiqiang";
    String pwd="zsqqq1996424";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fmV4=getSupportFragmentManager();
        ButterKnife.bind(this);
        context=this;
        fm =this.getSupportFragmentManager();
        setDefaultFragment();
        initNavigation();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

    private void initNavigation(){
        registerBroadcastReceiver();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.position1, "日程"))
                .addItem(new BottomNavigationItem(R.mipmap.xingji1,"动态"))
                .addItem(new BottomNavigationItem(R.drawable.fire_white,"攻略"))
                .addItem(new BottomNavigationItem(R.mipmap.chat, "联系人"))
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
                        switchFragment(currentFragment, scenicMainFragment);
                        break;
                    case 2:
                        if(squarFragment==null){
                            squarFragment=new SquarFragment();
                        }
                        switchFragment(currentFragment,squarFragment);
                        break;
                    case 3:
                        if (conversationListFragment == null) {
                            conversationListFragment=new ConversationListFragment();
                            conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

                                @Override
                                public void onListItemClicked(EMConversation conversation) {
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
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void switchFragment(Fragment from, Fragment to){
        if(currentFragment!=to){
            if(!to.isAdded()){
                fm.beginTransaction().add(R.id.fragment_container,to).show(to).commit();
            }else {
                fm.beginTransaction().hide(from).show(to).commit();
            }
            currentFragment=to;
        }

    }

    private void setDefaultFragment(){
        journeyFragment = JourneyFragment.newInstance();
        android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_container, journeyFragment);
        ft.commit();
        currentFragment=journeyFragment;
        conversationListFragment=new ConversationListFragment();
        squarFragment=new SquarFragment();
        userinfoFragment=new UserinfoFragment();
        scenicMainFragment=new ScenicMainFragment();
    }

    private class GetScenicSpot extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpParse httpParse=new HttpParse();
            String cityName= MyLocation.getMyLocation(getApplicationContext()).getCityName();
            httpParse.getScenicspot(getApplicationContext(), MyLocation.getMyLocation(getApplicationContext()).getCityName(),"1");
            httpParse.getAllScenicDetails(getApplicationContext(), cityName);
            return null;
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


}
