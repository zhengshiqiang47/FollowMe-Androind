package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.coderqiang.followme.IMabout.ConversationActivity;
import com.example.coderqiang.followme.R;
import com.tencent.TIMCallBack;
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

/**
 * Created by CoderQiang on 2016/11/26.
 */

public class LoginActivity extends Activity {
    private static final String TAG="LoginActivity";
    @Bind(R.id.login_name_edit)
    EditText name_edit;
    @Bind(R.id.login_passwd_edit)
    EditText passwd_edit;
    @Bind(R.id.login_sign_in)
    Button signInButton;
    @Bind(R.id.login_sign_up)
    Button signUpButton;
    @Bind(R.id.login_identifycode)
    EditText identifyCode;
    @Bind(R.id.login_loginButton)
    Button loginButton;
    @Bind(R.id.login_reset)
    Button resetButton;
    @Bind(R.id.login_reset2)
    Button resetButton2;

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
        setContentView(R.layout.activity_login);
        context=this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        signInButton.setText("获取验证码");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(name_edit.getText() + "", passwd_edit.getText() + "");
                Log.i(TAG, name_edit.getText() + " " + passwd_edit.getText() + "");
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountHelper.TLSPwdRegVerifyCode(pwd, pwdRegListener);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswd();
            }
        });
        resetButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountHelper.TLSPwdResetAskCode(num, resetListener);
            }
        });
    }

    private void signUp(String number, final String pwd){
        pwdRegListener=new TLSPwdRegListener() {
            @Override
            public void OnPwdRegAskCodeSuccess(int i, int i1) {
                Log.i(TAG, "短信调用成功");
            }

            @Override
            public void OnPwdRegReaskCodeSuccess(int i, int i1) {

            }

            @Override
            public void OnPwdRegVerifyCodeSuccess() {
                Log.i(TAG,"验证成功");
                accountHelper.TLSPwdRegCommit(pwd, pwdRegListener);
            }

            @Override
            public void OnPwdRegCommitSuccess(TLSUserInfo tlsUserInfo) {
                Log.i(TAG, "密码提交成功");
            }

            @Override
            public void OnPwdRegFail(TLSErrInfo tlsErrInfo) {
                Log.i(TAG, "短信调用失败");
            }

            @Override
            public void OnPwdRegTimeout(TLSErrInfo tlsErrInfo) {
                Log.i(TAG, "短信调用超时"+tlsErrInfo.ErrCode);
            }
        };
        accountHelper = TLSAccountHelper.getInstance().init(getApplicationContext(),sdkAppid,accType,appVer);
        accountHelper.TLSPwdRegAskCode(num, pwdRegListener);
    }


    private void resetPasswd(){
        accountHelper = TLSAccountHelper.getInstance().init(getApplicationContext(),sdkAppid,accType,appVer);
        resetListener=new TLSPwdResetListener() {
            @Override
            public void OnPwdResetAskCodeSuccess(int i, int i1) {
                Log.i(TAG, "密码重置请求成功");
            }

            @Override
            public void OnPwdResetReaskCodeSuccess(int i, int i1) {

            }

            @Override
            public void OnPwdResetVerifyCodeSuccess() {

            }

            @Override
            public void OnPwdResetCommitSuccess(TLSUserInfo tlsUserInfo) {
                Log.i(TAG, "重置成功");
            }

            @Override
            public void OnPwdResetFail(TLSErrInfo tlsErrInfo) {

            }

            @Override
            public void OnPwdResetTimeout(TLSErrInfo tlsErrInfo) {

            }
        };
        accountHelper.TLSPwdResetAskCode(num, resetListener);

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
                        Intent intent = new Intent(context, ConversationActivity.class);
                        startActivity(intent);
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

}
