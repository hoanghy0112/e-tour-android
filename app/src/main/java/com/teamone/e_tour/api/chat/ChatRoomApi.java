package com.teamone.e_tour.api.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.entities.ChatMessage;
import com.teamone.e_tour.entities.Staff;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.entities.Voucher;
import com.teamone.e_tour.models.ChatRoomManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface ChatRoomApi {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ChatRoomApi api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ChatRoomApi.class);


    public static class ResponseData {
        public int status;
        public String message;
        public ArrayList<ChatRoomManager.ResponseData.ChatRoom> data;

        public static class ChatRoom {
            public String _id;
            public String routeId;
            public Date updatedAt;
            public Staff staff;
            public TouristRoute route;
            public ChatMessage lastChat;
        }
    }


    @GET(ApiEndpoint.ChatApiEndpoint.viewAllChats)
    Call<ResponseData> viewAllChats(@Header("Authorization") String authorization);


}
