package com.teamone.e_tour.models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.teamone.e_tour.api.savedList.ViewSavedRouteListApi;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import java.util.ArrayList;

public class SavedRouteManager {

    private static SavedRouteManager instance;
    private AppCompatActivity context;
    private ViewSavedRouteListApi api;

    private MutableLiveData<ArrayList<TouristRoute>> routes = new MutableLiveData<>();

    private String ratingTicketId;

    public SavedRouteManager(AppCompatActivity context) {
        this.context = context;

        ViewSavedRouteListApi.getInstance(context).send();

        ViewSavedRouteListApi.getInstance(context).getData().observe(context, new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                routes.postValue(touristRoutes);
            }
        });
    }

    public static SavedRouteManager getInstance(AppCompatActivity context) {
        if (instance == null) {
            instance = new SavedRouteManager(context);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<TouristRoute>> getRoutes() {
        return routes;
    }

    public void remove(String routeId) {
        ArrayList<TouristRoute> newRoutes = routes.getValue();
        newRoutes.removeIf(touristRoute -> touristRoute.get_id() == routeId);
        routes.postValue(newRoutes);
    }
}
