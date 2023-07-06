package com.teamone.e_tour.api.support;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.route.ViewDetailRouteApi;
import com.teamone.e_tour.entities.ApiResponse;
import com.teamone.e_tour.entities.ChatRoom;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class GetChatRoomOfRoute {
    public interface IGetChatRoomOfRouteCallback {
        void onSuccess(ChatRoom chatRoom);
    }

    public static class ResponseData extends ApiResponse {
        public ChatRoom data;
    }

    public static void getChatRoom(Context context, String routeId, IGetChatRoomOfRouteCallback callback) {
        SocketManager socket = SocketManager.getInstance(context);

        JSONObject object = new JSONObject();
        try {
            Log.e("routeId", routeId);
            object.put("routeId", routeId);
        } catch (JSONException e) {

        }
        socket.emit("get-chat-room-of-route", object);

        socket.on("get-chat-room-of-route-result", args -> {
            Gson gson = new GsonBuilder().create();
            Log.e("chat-data", String.valueOf(args[0]));
            ResponseData responseData = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
            Log.e("get-chat-room-of-route-result", String.valueOf(args[0]));
            if (responseData.data.routeId.equals(routeId)) {
                callback.onSuccess(responseData.data);
            }
        });
    }
}
