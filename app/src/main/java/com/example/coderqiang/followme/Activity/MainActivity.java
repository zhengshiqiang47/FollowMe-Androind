package com.example.coderqiang.followme.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
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
import com.example.coderqiang.followme.Fragment.JourneyFragment;
import com.example.coderqiang.followme.Fragment.ScenicCommentsFragment;
import com.example.coderqiang.followme.Fragment.SquarFragment;
import com.example.coderqiang.followme.Fragment.UserinfoFragment;
import com.example.coderqiang.followme.IMabout.ConversationActivity;
import com.example.coderqiang.followme.IMabout.ConversationFragment;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.HttpParse;
import com.example.coderqiang.followme.Util.SetStatusColor;
import com.tencent.TIMCallBack;
import com.tencent.TIMConnListener;
import com.tencent.TIMManager;
import com.tencent.TIMUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSPwdRegListener;
import tencent.tls.platform.TLSPwdResetListener;
import tencent.tls.platform.TLSUserInfo;

public class MainActivity extends FragmentActivity {
    private static final String TAG="MainActivity";
    android.app.FragmentManager fm;
    JourneyFragment journeyFragment;
    UserinfoFragment userinfoFragment;
    SquarFragment squarFragment;
    ScenicCommentsFragment scenicCommentsFragment;
    Fragment currentFragment;
    @Bind(R.id.journey_nav)
    BottomNavigationBar bottomNavigationBar;
    ConversationFragment conversationFragment;
//    YWIMKit imKit;

    public final static int accType = 8902;
    public final static int sdkAppid=1400019371;
    public final static String appVer="1.0";
    TLSAccountHelper accountHelper;
    TLSLoginHelper loginHelper;
    TLSPwdLoginListener loginListener;

    TLSPwdRegListener pwdRegListener;
    TLSPwdResetListener resetListener;
    Context context;

    String num="86-15659772595";
    String pwd="zsqqq1996424";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context=this;
        fm = this.getFragmentManager();
        setDefaultFragment();
        initNavigation();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        new GetScenicSpot().execute();
        SetStatusColor.MIUISetStatusBarLightMode(getWindow(), true);
        Log.i(TAG, "rom厂商:" + android.os.Build.BRAND);
        TIMManager timManager=TIMManager.getInstance();
        timManager.init(getApplicationContext());
        timManager.setConnectionListener(new TIMConnListener() {
            @Override
            public void onConnected() {
                Log.i(TAG, "连接建立");
            }
            @Override
            public void onDisconnected(int i, String s) {
                Log.i(TAG, "连接断开errorCode:"+i+" "+s);
            }

            @Override
            public void onWifiNeedAuth(String s) {
                Log.i(TAG, "连接WifiNeedAuth");
            }
        });
        loginAccount();
//        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationFragment.class);
//        loginAndStartActivity();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    int count=0;
    @Override
    public void onBackPressed() {
        if(count<2){
            count++;
        }else {
            super.onBackPressed();
        }
    }

    private void initNavigation(){
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.position1, "日程"))
                .addItem(new BottomNavigationItem(R.drawable.tab_conversation, "广场"))
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
                        if(squarFragment==null){
                            squarFragment=new SquarFragment();
                        }
                        switchFragment(currentFragment,squarFragment);
                        break;
                    case 2:
                        if (conversationFragment == null) {
                            conversationFragment=new ConversationFragment();
                        }
                        switchFragment(currentFragment, conversationFragment);
//                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        startActivity(intent);
//                        addFriend();
                        break;
                    case 3:
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

    private void switchFragment(Fragment from,Fragment to){
        if(currentFragment!=to){
            if(!to.isAdded()){
                fm.beginTransaction().add(R.id.fragment_container,to).commit();
            }else {
                fm.beginTransaction().hide(from).show(to).commit();
            }
            currentFragment=to;
        }

    }

    private void setDefaultFragment(){
        journeyFragment = JourneyFragment.newInstance();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_container, journeyFragment);
        ft.commit();
        currentFragment=journeyFragment;
    }

    private class GetScenicSpot extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpParse httpParse=new HttpParse();
            httpParse.getScenicspot(getApplicationContext(),"杭州","2");
            httpParse.getScenicDetail(getApplicationContext(),ScenicspotLab.get(getApplicationContext()).getScenicspots().get(0));
            return null;
        }
    }

    private void loginAccount() {
        loginHelper = TLSLoginHelper.getInstance().init(getApplicationContext(), sdkAppid, accType, appVer);
//        if(!loginHelper.needLogin(num)) {
//            Log.i(TAG, "不需要登录");
//            Intent intent = new Intent(context, ConversationActivity.class);
//            startActivity(intent);
//            return;
//        }
        loginListener=new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {

                String usersig = loginHelper.getUserSig(tlsUserInfo.identifier);
                Log.i(TAG, "登录请求成功"+usersig);
                TIMUser user = new TIMUser();
                user.setIdentifier(num);

                TIMManager.getInstance().login(sdkAppid,user,usersig,new TIMCallBack(){

                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "SDK登录失败"+i+" "+s);
                    }

                    @Override
                    public void onSuccess() {
                        Log.e(TAG,"SDK登录成功");
                    }
                });
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
                Log.i(TAG, "登录请求图片成功");
            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
                Log.i(TAG, "登录有问题");
            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                Log.i(TAG, "登录请求失败"+tlsErrInfo.ErrCode+" "+tlsErrInfo.Msg+" "+tlsErrInfo.Title);
            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                Log.i(TAG, "登录请求超时");
            }
        };
        loginHelper.TLSPwdLogin(num, pwd.getBytes(), loginListener);

    }
//    private void loginAndStartActivity(){
//        String userid = "1234";
//        String passwd="123456";
//        imKit=YWAPI.getIMKitInstance(userid, LocationApplication.API_KEY);
//        IYWLoginService loginService=imKit.getLoginService();
//        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, passwd);
//        loginService.login(loginParam, new IWxCallback() {
//            @Override
//            public void onSuccess(Object... objects) {
//                Log.i(TAG,"登录成功");
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                Log.i(TAG,"登录失败");
//            }
//
//            @Override
//            public void onProgress(int i) {
//                Log.i(TAG,"登录中"+i);
//            }
//        });
//    }
//
//    private void addFriend(){
//        IYWContactService contactService = imKit.getContactService();
//
//        IWxCallback callback = new IWxCallback() {
//
//            @Override
//            public void onSuccess(Object... result) {
//                // onSuccess参数为空
//                Log.i(TAG,"添加成功");
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onProgress(int progress) {
//
//            }
//
//        };
//        contactService.addContact("1234", LocationApplication.API_KEY, "remarkName", "message", callback);
//    }
}
