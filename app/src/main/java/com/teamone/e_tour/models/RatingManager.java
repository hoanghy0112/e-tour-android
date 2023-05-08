package com.teamone.e_tour.models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.teamone.e_tour.api.rate.ViewRatingOfRouteApi;
import com.teamone.e_tour.entities.Rating;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.utils.SocketManager;

import java.util.ArrayList;
import java.util.HashMap;

public class RatingManager {
    private static RatingManager instance;
    private AppCompatActivity context;
    private ViewRatingOfRouteApi api;

    private MutableLiveData<HashMap<String, ArrayList<Rating>>> cacheData = new MutableLiveData<>(new HashMap<String, ArrayList<Rating>>());

    private MutableLiveData<ArrayList<Rating>> rating = new MutableLiveData<>();

    public RatingManager(AppCompatActivity context) {
        this.context = context;
        this.api = new ViewRatingOfRouteApi(new SocketManager(context));

        api.getResponse().observe(context, new Observer<ViewRatingOfRouteApi.ResponseData>() {
            @Override
            public void onChanged(ViewRatingOfRouteApi.ResponseData responseData) {
                if (responseData == null) return;

                rating.postValue(responseData.data);

                ArrayList<Rating> ratings = responseData.data;
                HashMap<String, ArrayList<Rating>> map = cacheData.getValue();
                assert map != null;
                map.put(responseData.routeId, ratings);
                cacheData.postValue(map);
            }
        });
    }

    public static RatingManager getInstance(AppCompatActivity context) {
        if (instance == null) {
            instance = new RatingManager(context);
        }
        return instance;
    }

    public void viewRating(String routeId) {
        api.finish();
        rating = new MutableLiveData<>(cacheData.getValue().get(routeId));
        api.fetchData(routeId);
    }

    public MutableLiveData<ArrayList<Rating>> getRating() {
        return rating;
    }
}
