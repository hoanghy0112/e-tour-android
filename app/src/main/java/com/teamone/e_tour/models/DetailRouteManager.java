package com.teamone.e_tour.models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.teamone.e_tour.api.route.ViewDetailRouteApi;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailRouteManager {
    private static DetailRouteManager instance;
    private AppCompatActivity context;
    private ViewDetailRouteApi api;

    private MutableLiveData<HashMap<String, TouristRoute>> cacheData = new MutableLiveData<>(new HashMap<String, TouristRoute>());

    private MutableLiveData<TouristRoute> routeInfo = new MutableLiveData<>();

    public DetailRouteManager(AppCompatActivity context) {
        this.context = context;
        this.api = new ViewDetailRouteApi(SocketManager.getInstance(context));

        api.getResponse().observe(context, new Observer<ViewDetailRouteApi.ResponseData>() {
            @Override
            public void onChanged(ViewDetailRouteApi.ResponseData responseData) {
                routeInfo.postValue(responseData.data);

                TouristRoute touristRoute = responseData.data;
                HashMap<String, TouristRoute> map = cacheData.getValue();
                assert map != null;
                map.put(touristRoute.get_id(), touristRoute);
                cacheData.postValue(map);
            }
        });
    }

    public static DetailRouteManager getInstance(AppCompatActivity context) {
        if (instance == null) {
            instance = new DetailRouteManager(context);
        }
        return instance;
    }

    public void viewRoute(String id) {
        api.finish();
        routeInfo = new MutableLiveData<>(cacheData.getValue().get(id));
        api.fetchData(id);
    }

    public MutableLiveData<TouristRoute> getRouteInfo() {
        return routeInfo;
    }
}
