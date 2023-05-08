package com.teamone.e_tour.api.rate;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.route.ViewDetailRouteApi;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;


public class CreateNewRateApi {
    private static CreateNewRateApi instance;

    public static final String emitEvent = "create-rate";
    public static final String serverResponseEvent = "create-rate-result";

    private SocketManager socket;

    public CreateNewRateApi(Context context) {
        socket = SocketManager.getInstance(context);
    }

    public CreateNewRateApi(SocketManager socket) {
        this.socket = socket;
    }

    public void send(float ratingPoint, String comment, String routeId) {
        JSONObject object = new JSONObject();

        try {
            object.put("star", ratingPoint);
            object.put("description", comment);
            object.put("touristsRouteId", routeId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        socket.emit(emitEvent, object);


        socket.on(serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                Log.e("aa", String.valueOf(args[0]));
            }
        });
        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                Log.e("aa", String.valueOf(args[0]));
            }
        });
    }

    public void finish() {
    }
}
