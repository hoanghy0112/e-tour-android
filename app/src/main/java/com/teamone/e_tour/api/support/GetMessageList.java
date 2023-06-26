package com.teamone.e_tour.api.support;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.entities.ApiResponse;
import com.teamone.e_tour.entities.ChatMessage;
import com.teamone.e_tour.entities.ChatRoom;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.socket.emitter.Emitter;

public class GetMessageList {

    public interface IGetMessageListCallback {
        void onGetMessageList(ArrayList<ChatMessage> messages);
        void onNewMessage(ChatMessage message);
    }


    public static class MessageListResponseData extends ApiResponse {
        public String chatRoomId;
        public ArrayList<ChatMessage> messages;
    }
    public static class MessageListResponse extends ApiResponse {
        public MessageListResponseData data;
    }
    public static class NewMessageResponse extends ApiResponse {
        public String chatRoomId;
        public String uid;
        public String content;
        public Date createdAt;
    }

    public static void getMessageList(Context context, String chatRoomId, IGetMessageListCallback callback) {
        SocketManager socket = SocketManager.getInstance(context);

        JSONObject object = new JSONObject();
        try {
            object.put("chatRoomId", chatRoomId);
        } catch (JSONException e) {

        }
        socket.emit("view-chat-message", object);

        socket.on("chat-message-list", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                MessageListResponse responseData = gson.fromJson(String.valueOf(args[0]), MessageListResponse.class);
                if (responseData.data.chatRoomId.equals(chatRoomId)) {
                    callback.onGetMessageList(responseData.data.messages);
                }
            }
        });
        socket.on("new-chat-message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                NewMessageResponse responseData = gson.fromJson(String.valueOf(args[0]), NewMessageResponse.class);
//                Log.e("new chat", String.valueOf(args[0]));
                if (responseData.chatRoomId.equals(chatRoomId)) {
                    callback.onNewMessage(new ChatMessage(responseData.uid, responseData.content, responseData.createdAt));
                }
            }
        });
    }
}
