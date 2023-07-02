package com.teamone.e_tour.models;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.chat.ChatRoomApi;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.entities.ChatMessage;
import com.teamone.e_tour.entities.ChatRoom;
import com.teamone.e_tour.entities.Staff;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomManager {
    @SuppressLint("StaticFieldLeak")
    static ChatRoomManager instance;
    Context context;

    MutableLiveData<ArrayList<ResponseData.ChatRoom>> chatRooms = new MutableLiveData<>();

    public ChatRoomManager() {
    }

    public static ChatRoomManager getInstance() {
        if (instance == null) {
            instance = new ChatRoomManager();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<ResponseData.ChatRoom>> getChatRooms() {
        return chatRooms;
    }

    public void setContext(Context context) {
        this.context = context;

//        SocketManager.getInstance(context).on("chat-room-list", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
//
//                ResponseData responseData = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
//                if (responseData.status == 200) {
//                    chatRooms.postValue(responseData.data);
//                }
//            }
//        });
    }

    public void fetchData() {
//        SocketManager.getInstance(context).emit("view-chat-room-list", new JSONObject());
        ChatRoomApi.api.viewAllChats(CredentialToken.getInstance(context).getBearerAccessToken()).enqueue(new Callback<ChatRoomApi.ResponseData>() {
            @Override
            public void onResponse(Call<ChatRoomApi.ResponseData> call, Response<ChatRoomApi.ResponseData> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    chatRooms.postValue(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<ChatRoomApi.ResponseData> call, Throwable t) {

            }
        });
    }

    public static class ResponseData {
        public int status;
        public String message;
        public ArrayList<ChatRoom> data;

        public static class ChatRoom {
            public String _id;
            public String routeId;
            public Date updatedAt;
            public Staff staff;
            public TouristRoute route;
            public ChatMessage lastChat;
        }
    }
}
