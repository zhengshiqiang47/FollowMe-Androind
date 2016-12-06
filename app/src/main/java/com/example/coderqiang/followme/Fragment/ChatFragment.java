package com.example.coderqiang.followme.Fragment;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.coderqiang.followme.Activity.VoiceCallActivity;
import com.example.coderqiang.followme.IMAbout.Model.Activity.ChatRowVoiceCall;
import com.example.coderqiang.followme.IMAbout.Model.Activity.EaseBaiduMapActivity;
import com.example.coderqiang.followme.IMAbout.Model.Activity.ImageGridActivity;
import com.example.coderqiang.followme.IMAbout.Model.Activity.PickAtUserActivity;
import com.example.coderqiang.followme.IMAbout.Model.Activity.VideoCallActivity;
import com.example.coderqiang.followme.IMAbout.Model.EmojiconGroupData;
import com.example.coderqiang.followme.IMAbout.Model.RobotUser;
import com.example.coderqiang.followme.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowLocation;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by CoderQiang on 2016/12/1.
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper{
    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 5;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 6;
    static final int ITEM_LOCATION = 3;
    //red packet code : 红包功能使用的常量
    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
    private static final int MESSAGE_TYPE_RECV_TRANSFER_PACKET = 9;
    private static final int MESSAGE_TYPE_SEND_TRANSFER_PACKET = 10;
    private static final int REQUEST_CODE_SEND_RED_PACKET = 16;
    private static final int ITEM_RED_PACKET = 16;
    private static final int REQUEST_CODE_SEND_TRANSFER_PACKET = 17;
    private static final int ITEM_TRANSFER_PACKET = 17;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        super.setUpView();
        ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconGroupData.getData());
        EaseUI.getInstance().setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {
            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data=EmojiconGroupData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if(emojicon.getIdentityCode().equals(emojiconIdentityCode)){
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });
        if(chatType==EaseConstant.CHATTYPE_GROUP){
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
                                putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();

        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
        if(chatType == EaseConstant.CHATTYPE_SINGLE){
            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
        }
        //聊天室暂时不支持红包功能
//        //red packet code : 注册红包菜单选项
//        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
//            inputMenu.registerExtendMenuItem("Red Packet", R.drawable.em_chat_red_packet_selector, ITEM_RED_PACKET, extendMenuItemClickListener);
//        }
//        //red packet code : 注册转账菜单选项
//        if (chatType ==EaseConstant.CHATTYPE_SINGLE) {
//            inputMenu.registerExtendMenuItem("Transfer", R.drawable.em_chat_red_packet_selector, ITEM_TRANSFER_PACKET, extendMenuItemClickListener);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case RESULT_CODE_FORWARD: // forward
//                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//                    startActivity(intent);
                    Log.i(TAG,"转发按钮");
                    break;
                default:
                    break;
            }
        }
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if(data != null){
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
//                //red packet code : 发送红包消息到聊天界面
//                case REQUEST_CODE_SEND_RED_PACKET:
//                    if (data != null){
////                        sendMessage(RedPacketUtil.createRPMessage(getActivity(), data, toChatUsername));
//                        Log.i(TAG, "发红包功能(未做)");
//                    }
//                    break;
//                case REQUEST_CODE_SEND_TRANSFER_PACKET://发送转账消息
//                    if (data != null) {
////                        sendMessage(RedPacketUtil.createTRMessage(getActivity(), data, toChatUsername));
//                        Log.i(TAG, "转账功能(未做)");
//                    }
//                    break;
                //end of red packet code
                default:
                    break;
            }
        }
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }



    @Override
    public void onEnterToChatDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i(TAG,"详情页(未做)");
// /            startActivityForResult(
//                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
//                    REQUEST_CODE_GROUP_DETAIL);
        }else if(chatType == EaseConstant.CHATTYPE_CHATROOM){
//            startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
            Log.i(TAG,"详情页(未做)");
        }
    }

    @Override
    public void onAvatarClick(String username) {
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
        Log.i(TAG,"用户详情页(未做)");
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        if(message.getType()== EMMessage.Type.LOCATION){
            startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
        }
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO:
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            case ITEM_LOCATION:
                startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                break;
//            //red packet code : 进入发红包页面
//            case ITEM_RED_PACKET:
//                RedPacketUtil.startRedPacketActivityForResult(this, chatType, toChatUsername, REQUEST_CODE_SEND_RED_PACKET);
//                break;
//            case ITEM_TRANSFER_PACKET://进入转账页面
//                RedPacketUtil.startTransferActivityForResult(this, toChatUsername, REQUEST_CODE_SEND_TRANSFER_PACKET);
//                break;
            //end of red packet code
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 10;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if(message.getType() == EMMessage.Type.TXT){
                //voice call
                if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                }else if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }if (message.getType() == EMMessage.Type.LOCATION) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
                }
                //red packet code : 红包消息、红包回执消息以及转账消息的chatrow type
//                else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {
//                    //发送红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET : MESSAGE_TYPE_SEND_RED_PACKET;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    //领取红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET_ACK : MESSAGE_TYPE_SEND_RED_PACKET_ACK;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_TRANSFER_PACKET_MESSAGE, false)) {
//                    //转账消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TRANSFER_PACKET : MESSAGE_TYPE_SEND_TRANSFER_PACKET;
//                }
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if(message.getType() == EMMessage.Type.TXT){
                // voice call or video call
                if (message.getBooleanAttribute(com.example.coderqiang.followme.Model.EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(com.example.coderqiang.followme.Model.EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
                //red packet code : 红包消息、红包回执消息以及转账消息的chat row
//                else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {//红包消息
//                    return new ChatRowRedPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {//红包回执消息
//                    return new ChatRowRedPacketAck(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_TRANSFER_PACKET_MESSAGE, false)) {//转账消息
//                    return new ChatRowTransfer(getActivity(), message, position, adapter);
//                }
                //end of red packet code
            }else if(message.getType()== EMMessage.Type.LOCATION){
                Log.i(TAG,"Type==Location");
                return new EaseChatRowLocation(context, message, position,adapter);
            }
            return null;
        }


    }
}
