package com.example.coderqiang.followme.IMabout;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coderqiang.followme.Activity.ChatActivity;
import com.example.coderqiang.followme.Model.MyConversation;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.View.RecyclerViewDivider;
import com.tencent.TIMConversation;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMProfileSystemElem;
import com.tencent.TIMSNSChangeInfo;
import com.tencent.TIMSNSSystemElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/27.
 */

public class ConversationFragment extends Fragment {
    private static final String TAG="ConversationFragment";
    @Bind(R.id.conversation_recycler)
    RecyclerView recyclerView;
    List<TIMConversation> conversations;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    private void initView(){

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        conversations = TIMManager.getInstance().getConversionList();
        for (int i=0;i<conversations.size();i++) {
            TIMConversation conversation=conversations.get(i);
            conversation.getLocalMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Log.i(TAG,"获取本地对话失败");
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    Log.i(TAG,"获取本地对话成功"+timMessages.size());
                }
            });
        }
        recyclerView.setAdapter(new ConversationAdapter());
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                for (int i = 0; i < list.size(); i++) {
                    for(int j=0;j<list.get(i).getElementCount();j++){
                        TIMElem elem = list.get(i).getElement(j);
                        TIMElemType elemType=elem.getType();
                        Log.i(TAG, "elem type:" + elemType.name());
                        if (elemType == TIMElemType.Text) {
                            TIMTextElem timTextElem=(TIMTextElem) elem;
                            Log.i(TAG,"新消息-->发送者:"+list.get(i).getSenderProfile()+" 内容"+timTextElem.getText());
                        }else if(elemType==TIMElemType.Custom){
                            Log.e(TAG," "+elemType);
                        }
                    }
                    Log.i(TAG, "消息总数" + list.size());
                }
                conversations = TIMManager.getInstance().getConversionList();
                recyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private class ConversationAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ConverHolder(getActivity().getLayoutInflater().from(getActivity()).inflate(R.layout.conversation_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if(holder instanceof ConverHolder){
                ConverHolder converHolder=(ConverHolder)holder;
                MyConversation myConversation=getLastMessage(conversations.get(position));
                Log.i(TAG, myConversation.getContent() + " " + myConversation.getName() + " " + myConversation.getDate());
                converHolder.textView.setText(myConversation.getContent());
                converHolder.item_date.setText(myConversation.getDate());
                converHolder.item_name.setText(myConversation.getName());
                converHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String peer=conversations.get(position).getPeer();
                        Log.i(TAG, "对方ID"+peer);
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(ChatActivity.EXTRA_PEEK, peer);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            Log.e(TAG, "converationsize" + conversations.size());
            return conversations.size();
        }

        private MyConversation getLastMessage(TIMConversation conversation){
            MyConversation myConversation=new MyConversation();


            TIMMessage message=conversation.getLastMsgs(1).get(0);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date(message.timestamp()*1000L));
            date=date.substring(5);
            Log.i(TAG, "date:" + date+" sender"+message.getSender());
            myConversation.setDate(date);
            String sender=message.getSender();
            if(sender.length()>=20) sender = sender.substring(0, 20);
            myConversation.setName(sender);
            String content="null";
            for(int i=0;i<message.getElementCount();i++) {
                TIMElem elem=message.getElement(i);
                content=getMessageContent(elem);
                myConversation.setContent(content);
                Log.i(TAG,"第"+i+"条消息");
            }
            if (myConversation.getContent().length()>=15) myConversation.setContent(myConversation.getContent().substring(0,15)+"...");
            return myConversation;
        }


        private String getMessageContent(TIMElem elem){
            MyConversation myConversation=new MyConversation();
            TIMElemType elemType=elem.getType();
            if(elemType==TIMElemType.Text){
                Log.i(TAG,"文本消息");
                return ((TIMTextElem)elem).getText();
            }else if(elemType==TIMElemType.Custom){
                Log.i(TAG, "自定义消息");
                return "自定义消息："+((TIMCustomElem)elem).getDesc();
            }else if(elemType==TIMElemType.Face){
                Log.i(TAG,"表情信息");
                return "[表情]";
            }else if(elemType==TIMElemType.SNSTips){
                Log.i(TAG, "SNS消息");
                TIMSNSSystemElem systemElem=(TIMSNSSystemElem)elem;
                TIMSNSChangeInfo changeInfo=systemElem.getChangeInfoList().get(0);
                myConversation.setContent("SNS:" + systemElem.getChangeInfoList().size() + " " + systemElem.getDecideReportTimestamp() + " changeInfo:" + changeInfo.getIdentifier() + " " + changeInfo.getNickName() + " " + changeInfo.getRemark());
                return "SNS信息";
            }else if(elemType==TIMElemType.ProfileTips){
                TIMProfileSystemElem profileSystemElem=(TIMProfileSystemElem)elem;//好友改变之类消息
                myConversation.setContent(profileSystemElem.getFromUser() + " " + profileSystemElem.getNickName() + " " + profileSystemElem.getSubType());
                return "ProfileTips信息";
            }
            return elemType.name();
        }

        private class ConverHolder extends RecyclerView.ViewHolder{
            LinearLayout layout;
            ImageView imageView;
            TextView textView;
            TextView item_date;
            TextView item_name;
            public ConverHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.converation_item_tv);
                imageView = (ImageView) itemView.findViewById(R.id.converation_img);
                item_date = (TextView) itemView.findViewById(R.id.conversation_item_date);
                item_name = (TextView) itemView.findViewById(R.id.conversation_item_name);
                layout = (LinearLayout) itemView.findViewById(R.id.conversation_layout);
            }
        }
    }
}
