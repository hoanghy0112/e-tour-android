package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.teamone.e_tour.databinding.ActivityOnBoardingBinding;
import com.teamone.e_tour.models.CredentialToken;

public class OnBoardingActivity extends AppCompatActivity {
    ActivityOnBoardingBinding activityOnBoardingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOnBoardingBinding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(activityOnBoardingBinding.getRoot());


        activityOnBoardingBinding.getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetStartedClicked();
            }
        });
    }
    
    public void onGetStartedClicked() {
        startActivity(new Intent(this, AuthenticationActivity.class));
        finish();
    }
}