package com.teamone.e_tour;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.teamone.e_tour.activities.AuthenticationActivity;
import com.teamone.e_tour.activities.HomeActivity;
import com.teamone.e_tour.activities.OnBoardingActivity;
import com.teamone.e_tour.databinding.ActivityMainBinding;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.CredentialToken;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppManagement.getInstance(this).getFirstTime()) {
            Intent intent = new Intent(this,  OnBoardingActivity.class);
            startActivity(intent);
            finish();
        } else if (CredentialToken.getInstance(this).getId().length() != 0) {
            Intent intent = new Intent(this,  HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this,  AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }

    }
}