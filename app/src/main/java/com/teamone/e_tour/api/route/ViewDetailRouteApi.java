package com.teamone.e_tour.api.route;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.SocketMessage;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.socket.emitter.Emitter;

public class ViewDetailRouteApi {
    public static final String emitEvent = "view-route";
    public static final String serverResponseEvent = "route";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public String listenerId;
        public TouristRoute data;
    }


    private SocketManager socket;
    private MutableLiveData<ResponseData> response = new MutableLiveData<>();


    public ViewDetailRouteApi(Context context) {
        socket = SocketManager.getInstance(context);
    }

    public ViewDetailRouteApi(SocketManager socket) {
        this.socket = socket;
    }

    public MutableLiveData<ResponseData> getResponse() {
        return response;
    }

    public void fetchData(String id) {
        JSONObject object = new JSONObject();

        try {
            object.put("id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        socket.emit(ViewDetailRouteApi.emitEvent, object);

        socket.on(ViewDetailRouteApi.serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                ViewDetailRouteApi.ResponseData responseData = gson.fromJson(String.valueOf(args[0]), ViewDetailRouteApi.ResponseData.class);
                response.postValue(responseData);
            }
        });
    }

    public void finish() {
        if (response.getValue() == null) return;

        final String listenerId = response.getValue().listenerId;

        JSONObject object = new JSONObject();
        try {
            object.put("listenerId", listenerId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        socket.emit(SocketMessage.Client.REMOVE_LISTENER);
    }
}
