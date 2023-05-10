package com.teamone.e_tour.api.savedList;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class AddToSaveListApi {
    public static AddToSaveListApi instance;
    public static final String emitEvent = "save-route";
    public static final String serverResponseEvent = "save-route-result";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;

        public ResponseData() {
        }
    }


    private Context context;
    private SocketManager socket;

    public AddToSaveListApi(Context context) {
        this.context = context;
        this.socket = new SocketManager(context);
    }

    public static AddToSaveListApi getInstance(Context context) {
        if (instance == null) {
            instance = new AddToSaveListApi(context);
        }
        return instance;
    }

    public void send(String routeId) {
        JSONObject object = new JSONObject();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

        try {
            object.put("routeId", routeId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        socket.emit(emitEvent, object);

        socket.on(serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ResponseData response = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
                if (response.status == 200) {
                }
            }
        });

    }

    public void finish() {
        if (socket != null && socket.getSocket() != null)
            socket.getSocket().disconnect();
    }
}
