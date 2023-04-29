package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.RecommendedRouteManager;
import com.teamone.e_tour.models.UserProfileManager;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppManagement.getInstance(this).setFirstTime(false);

        UserProfileManager.getInstance(this).fetchUserProfile();

        RecommendedRouteManager.getInstance(this).fetchData(3);

        RecommendedRouteManager.getInstance(this).getRouteList().observe(this, new Observer<ArrayList<TouristRoute>>() {
            @Override
            public void onChanged(ArrayList<TouristRoute> touristRoutes) {
                Log.e("Tourist routes length: ", String.valueOf(touristRoutes.size()));
            }
        });
    }
}