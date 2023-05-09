package com.teamone.e_tour.activities;

import static androidx.navigation.ui.NavigationUI.onNavDestinationSelected;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.adapters.RecommendedRouteListAdapter;
import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.models.DetailRouteManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.models.RecommendedRouteManager;
import com.teamone.e_tour.models.UserProfileManager;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onNavDestinationSelected(item, Navigation.findNavController(HomeActivity.this, R.id.home_wrapper));
                return true;
            }
        });

        binding.bottomNavigation.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homeFragment) {
                    Navigation.findNavController(HomeActivity.this, R.id.home_wrapper).navigate(R.id.homeFragment);
                }
                if (item.getItemId() == R.id.historyTab) {
                    Navigation.findNavController(HomeActivity.this, R.id.home_wrapper).navigate(R.id.historyTab);
                }
                if (item.getItemId() == R.id.savedTab) {
                    Navigation.findNavController(HomeActivity.this, R.id.home_wrapper).navigate(R.id.savedTab);
                }
                if (item.getItemId() == R.id.accountTab) {
                    Navigation.findNavController(HomeActivity.this, R.id.home_wrapper).navigate(R.id.accountTab);
                }
            }
        });

        AppManagement.getInstance(this).setFirstTime(false);

        UserProfileManager.getInstance(this).fetchUserProfile();

        // Initialize individual socket and connect it to server to reduce response time when fetching route
        DetailRouteManager.getInstance(this);
        BookedTicketManager.getInstance(this);
        RatingManager.getInstance(this);
    }
}