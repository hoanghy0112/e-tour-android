package com.teamone.e_tour.api.rate;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.SocketMessage;
import com.teamone.e_tour.entities.Rating;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class ViewRatingOfRouteApi {

    public static final String emitEvent = "view-rate-of-route";
    public static final String serverResponseEvent = "rate-of-route";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public String listenerId;
        public ArrayList<Rating> data;
        public String routeId;
    }


    private SocketManager socket;
    private MutableLiveData<ResponseData> response = new MutableLiveData<>();

    public ViewRatingOfRouteApi(Context context) {
        socket = SocketManager.getInstance(context);
    }

    public ViewRatingOfRouteApi(SocketManager socket) {
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

        socket.emit(emitEvent, object);

        socket.on(serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                ResponseData responseData = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
                responseData.routeId = id;
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
