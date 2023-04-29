package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.UserProfileManager;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppManagement.getInstance(this).setFirstTime(false);

        UserProfileManager.getInstance(this).watch();
    }
}