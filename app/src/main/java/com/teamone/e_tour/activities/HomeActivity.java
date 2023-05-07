package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.HorizontalScrollView;

import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.RecommendedRouteListAdapter;
import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.DetailRouteManager;
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

        // Initialize individual socket and connect it to server to reduce response time when fetching route
        DetailRouteManager.getInstance(this);
    }
}