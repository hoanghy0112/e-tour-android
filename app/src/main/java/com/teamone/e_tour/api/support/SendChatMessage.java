package com.teamone.e_tour.api.support;

import android.content.Context;

import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SendChatMessage {
    public static void send(Context context, String chatRoomId, String content) {
        SocketManager socket = SocketManager.getInstance(context);

        JSONObject object = new JSONObject();
        try {
            object.put("chatRoomId", chatRoomId);
            object.put("content", content);
        } catch (JSONException e) {

        }
        socket.emit("create-chat-message", object);
    }
}
