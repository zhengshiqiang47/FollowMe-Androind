package com.example.coderqiang.followme.Activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.WindowManager;

import com.example.coderqiang.followme.Fragment.ChatFragment;
import com.example.coderqiang.followme.Fragment.JourneyFragment;
import com.example.coderqiang.followme.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatInputMenu;

/**
 * Created by CoderQiang on 2016/11/30.
 */


public class ChatActivity extends FragmentActivity {
    String name;
    EaseChatFragment easeChatFragment;
    FragmentManager fm;
    EaseChatInputMenu inputMenu;
    ChatFragment chatFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        name = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        chatFragment=new ChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_chat,chatFragment).commit();
    }
}
