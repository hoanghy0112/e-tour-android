package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.ActivityHomeBinding;
import com.teamone.e_tour.models.CredentialToken;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.homeText.setText(CredentialToken.getInstance(this).getId());
    }
}