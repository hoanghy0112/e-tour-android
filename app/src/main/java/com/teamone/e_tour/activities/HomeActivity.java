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
import com.teamone.e_tour.utils.SocketManager;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppManagement.getInstance(this).setFirstTime(false);

        SocketManager.getInstance(this);
    }
}