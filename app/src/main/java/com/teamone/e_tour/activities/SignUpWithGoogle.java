package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.ActivitySignUpWithGoogleBinding;

public class SignUpWithGoogle extends AppCompatActivity {
    String accessToken;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySignUpWithGoogleBinding binding = ActivitySignUpWithGoogleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        accessToken = getIntent().getExtras().getString("accessToken");
        email = getIntent().getExtras().getString("email");
    }
}