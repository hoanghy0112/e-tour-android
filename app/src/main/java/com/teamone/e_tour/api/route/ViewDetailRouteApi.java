package com.teamone.e_tour.api.route;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class ViewDetailRouteApi {
    public static final String emitEvent = "view-route";
    public static final String serverResponseEvent = "route";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public TouristRoute data;
    }


    private Context context;
    private SocketManager socket;

    private MutableLiveData<TouristRoute> routeData = new MutableLiveData<TouristRoute>(new TouristRoute());


    public ViewDetailRouteApi(Context context) {
        this.context = context;
    }

    public MutableLiveData<TouristRoute> getRouteData() {
        return routeData;
    }

    public void fetchData(String id) {
        JSONObject object = new JSONObject();

        try {
            object.put("id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        socket = SocketManager.getInstance(context);
        socket.emit(ViewDetailRouteApi.emitEvent, object);

        socket.on(ViewDetailRouteApi.serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                ViewDetailRouteApi.ResponseData response = gson.fromJson(String.valueOf(args[0]), ViewDetailRouteApi.ResponseData.class);
                if (response.status == 200) {
                    routeData.postValue(response.data);
                }
            }
        });
    }

    public void finish() {
//        if (socket != null && socket.getSocket() != null)
//            socket.getSocket().disconnect();
    }
}
