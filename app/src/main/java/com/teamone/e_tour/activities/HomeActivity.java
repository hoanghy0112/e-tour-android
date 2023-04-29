package com.teamone.e_tour.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.CredentialToken;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppManagement.getInstance(this).setFirstTime(false);

//        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.item_1:
//                        item.setIcon(getResources().getDrawable(R.drawable.home_focus, getTheme()));
//                        break;
//                }
//
//                binding.bottomNavigation.
//
//                return true;
//            }
//        });
    }
}