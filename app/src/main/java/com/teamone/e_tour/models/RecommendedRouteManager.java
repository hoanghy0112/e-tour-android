package com.teamone.e_tour.models;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.route.ViewRecommendedRoute;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.function.Consumer;

import io.socket.emitter.Emitter;

public class RecommendedRouteManager {
    private static RecommendedRouteManager instance;
    private Context context;

    private MutableLiveData<ArrayList<TouristRoute>> routeList = new MutableLiveData<ArrayList<TouristRoute>>(new ArrayList<>());

    public MutableLiveData<ArrayList<TouristRoute>> getRouteList() {
        return routeList;
    }

    public RecommendedRouteManager(Context context) {
        this.context = context;
    }

    public static RecommendedRouteManager getInstance(Context context) {
        if (instance == null) {
            instance = new RecommendedRouteManager(context);
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
        SocketManager.getInstance(context).emit(ViewRecommendedRoute.emitEvent, object);

        SocketManager.getInstance(context).on(ViewRecommendedRoute.serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                ViewRecommendedRoute.ResponseData response = gson.fromJson(String.valueOf(args[0]), ViewRecommendedRoute.ResponseData.class);
                if (response.status == 200) {
                    routeList.postValue(response.data);
                }
            }
        });
    }
}
