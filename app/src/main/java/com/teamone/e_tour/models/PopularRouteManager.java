package com.teamone.e_tour.models;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.route.ViewPopularRoute;
import com.teamone.e_tour.api.route.ViewRecommendedRoute;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class PopularRouteManager {

    private static PopularRouteManager instance;
    private Context context;
    private SocketManager socket;

    private MutableLiveData<ArrayList<TouristRoute>> routeList = new MutableLiveData<ArrayList<TouristRoute>>(new ArrayList<>());

    public MutableLiveData<ArrayList<TouristRoute>> getRouteList() {
        return routeList;
    }

    public PopularRouteManager(Context context) {
        this.context = context;
        this.socket = new SocketManager(context);
    }

    public static PopularRouteManager getInstance(Context context) {
        if (instance == null) {
            instance = new PopularRouteManager(context);
        }
        return instance;
    }

    public void fetchData(int num) {
        JSONObject object = new JSONObject();
        try {
            object.put("num", num);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        SocketManager.getInstance(context).emit(ViewPopularRoute.emitEvent, object);

        SocketManager.getInstance(context).on(ViewPopularRoute.serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                ViewPopularRoute.ResponseData response = gson.fromJson(String.valueOf(args[0]), ViewPopularRoute.ResponseData.class);
                if (response.status == 200) {
                    routeList.postValue(response.data);
                }
            }
        });
    }
}
