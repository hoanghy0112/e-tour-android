package com.teamone.e_tour.api.savedList;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class ViewSavedRouteListApi {

    public static ViewSavedRouteListApi instance;
    public static final String emitEvent = "view-saved-route";
    public static final String serverResponseEvent = "saved-route";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public ArrayList<TouristRoute> data;

        public ResponseData() {
        }
    }


    private SocketManager socket;

    public MutableLiveData<ArrayList<TouristRoute>> getData() {
        return data;
    }

    private MutableLiveData<ArrayList<TouristRoute>> data = new MutableLiveData<>(null);

    public ViewSavedRouteListApi(SocketManager socket) {
        this.socket = socket;
    }

    public static ViewSavedRouteListApi getInstance(Context context) {
        if (instance == null) {
            instance = new ViewSavedRouteListApi(new SocketManager(context));
        }
        return instance;
    }

    public void send() {
        JSONObject object = new JSONObject();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

        socket.emit(emitEvent, object);

        socket.on(serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ResponseData response = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
                if (response.status == 200) {
                    data.postValue(response.data);
                }
            }
        });
    }

    public void finish() {
        if (socket != null && socket.getSocket() != null)
            socket.getSocket().disconnect();
    }
}
