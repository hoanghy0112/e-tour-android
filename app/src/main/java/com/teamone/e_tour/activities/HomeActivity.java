package com.teamone.e_tour.activities;

import static androidx.navigation.ui.NavigationUI.onNavDestinationSelected;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.BookedTicketManager;
import com.teamone.e_tour.models.DetailRouteManager;
import com.teamone.e_tour.models.HotVoucherManager;
import com.teamone.e_tour.models.PopularRouteManager;
import com.teamone.e_tour.models.RatingManager;
import com.teamone.e_tour.models.SavedRouteManager;
import com.teamone.e_tour.models.UserProfileManager;

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
        SavedRouteManager.getInstance(this);
        PopularRouteManager.getInstance(this).fetchData(10);
        HotVoucherManager.getInstance(this).fetchData(10);
    }
}