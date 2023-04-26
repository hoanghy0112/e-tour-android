package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import android.database.Observable;
import android.os.Bundle;

import com.teamone.e_tour.R;
import com.teamone.e_tour.databinding.ActivityAuthenticationBinding;

public class Authentication extends AppCompatActivity {
    ActivityAuthenticationBinding activityAuthenticationBinding;

    public static class AuthenticationInfo extends BaseObservable {
        public ObservableField<String> username;
        public ObservableField<String> password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAuthenticationBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(activityAuthenticationBinding.getRoot());
    }
}