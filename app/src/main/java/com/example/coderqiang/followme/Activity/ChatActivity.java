package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coderqiang.followme.R;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/27.
 */

public class ChatActivity extends Activity {
    public static final String EXTRA_PEEK="peekName";
    private static final String TAG="ChatActivity";

    @Bind(R.id.chat_nick_name)
    TextView nickName;
    @Bind(R.id.chat_edit_text)
    EditText editText;
    @Bind(R.id.chat_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.chat_send_button)
    Button sendButton;

    ArrayList<TIMMessage> messages;

    Context context;
    private String peek;
    private String userName;
    private TIMConversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        peek = getIntent().getStringExtra(EXTRA_PEEK);
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, peek);
        context=this;
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
    }

    private void initView(){
        messages = new ArrayList<TIMMessage>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new chatAdapter());
        conversation.getLocalMessage(10, null, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMMessage> timMessages) {
                for (int i=timMessages.size()-1;i>=0;i--){
                    messages.add(timMessages.get(i));
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"点击了");
                sendMessage(peek,editText.getText()+"");
            }
        });

        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                for (int i=0;i<list.size();i++){
                    TIMMessage message=list.get(i);
                    if(message.getSender()==peek||message.isSelf()){
                        messages.add(message);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        Log.i(TAG, "新消息" + message.getSender() + " " + message.isSelf());
                    }
                }
                return false;
            }
        });
    }
    TIMMessage sendedmsg;
        private void sendMessage(String peer,String str){
            TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C,peer);
            TIMMessage msg=new TIMMessage();
            TIMTextElem elem=new TIMTextElem();
            elem.setText(str);
            if (msg.addElement(elem) != 0) {
                Log.i(TAG,"添加失败");
                return;
            }
            sendedmsg=msg;
            conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "消息发送失败 code:" + i + " errmsg" + s);
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    messages.add(sendedmsg);
                    Log.i(TAG, "消息发送成功");
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size()-1);
                }
            });

        }

    private class chatAdapter extends RecyclerView.Adapter{
        private static final int TYPE_SELF=0;
        private static final int TYPE_PEEK=1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==TYPE_SELF){
                return new SelfHolder(getLayoutInflater().from(context).inflate(R.layout.chat_item_self, parent, false));
            }else if(viewType==TYPE_PEEK){
                return new PeekHolder(getLayoutInflater().from(context).inflate(R.layout.chat_item_peek, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof SelfHolder){
                SelfHolder selfHolder=(SelfHolder)holder;
                TIMTextElem elem =(TIMTextElem) messages.get(position).getElement(0);
                selfHolder.item_self.setText(elem.getText());
            }else if(holder instanceof PeekHolder){
                PeekHolder peekHolder=(PeekHolder)holder;
                TIMTextElem elem =(TIMTextElem) messages.get(position).getElement(0);
                peekHolder.item_peek.setText(elem.getText());
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(messages.get(position).isSelf()){
                return TYPE_SELF;
            }else {
                return TYPE_PEEK;
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        private class SelfHolder extends RecyclerView.ViewHolder{
            TextView item_self;
            public SelfHolder(View itemView) {
                super(itemView);
                item_self=(TextView)itemView.findViewById(R.id.chat_item_text_self);
            }
        }

        private class PeekHolder extends RecyclerView.ViewHolder{
            TextView item_peek;
            public PeekHolder(View itemView) {
                super(itemView);
                item_peek=(TextView)itemView.findViewById(R.id.chat_item_text_peek);
            }
        }
    }

}
