package com.example.coderqiang.followme.IMabout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coderqiang.followme.R;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMUser;
import com.tencent.TIMValueCallBack;
import com.tencent.imcore.TextElem;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/27.
 */

public class ConversationActivity extends Activity {
    private static final String tag = "ConversationActivity";
    @Bind(R.id.converation_edit)
    EditText editText;
    @Bind(R.id.converation_sendButton)
    Button sendButton;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converation);
        context=this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                for (int i = 0; i < list.size(); i++) {
                    for(int j=0;j<list.get(i).getElementCount();j++){
                        TIMElem elem = list.get(i).getElement(j);
                        TIMElemType elemType=elem.getType();
                        Log.i(tag, "elem type:" + elemType.name());
                        if (elemType == TIMElemType.Text) {
                            TIMTextElem timTextElem=(TIMTextElem) elem;
                            Log.i(tag,"新消息-->发送者:"+list.get(i).getSenderProfile()+" 内容"+timTextElem.getText());
                        }else if(elemType==TIMElemType.Custom){
                            Log.e(tag," "+elemType);
                        }
                    }

;
                    Log.i(tag, "消息总数" + list.size());
                }
                return false;
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "" + editText.getText());
                sendMessage("86-15659772595",editText.getText()+"");
            }
        });
    }

    private void sendMessage(String peer,String str){
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C,peer);
        TIMMessage msg=new TIMMessage();
        TIMTextElem elem=new TIMTextElem();
        elem.setText(str);

        if (msg.addElement(elem) != 0) {
            Log.i(tag,"添加失败");
            return;
        }
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Log.i(tag, "消息发送失败 code:" + i + " errmsg" + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                Log.i(tag, "消息发送成功");
            }
        });

    }




}
