package com.teamone.e_tour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.teamone.e_tour.api.account.authentication.AuthenticationAPI;
import com.teamone.e_tour.api.account.authentication.SignInWithPasswordApiResult;
import com.teamone.e_tour.databinding.ActivityAuthenticationBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity {
    ActivityAuthenticationBinding activityAuthenticationBinding;
    AuthenticationInfo viewmodel = new AuthenticationInfo();

    public class AuthenticationInfo extends BaseObservable {
        public AuthenticationInfo() {
        }

        public ObservableField<String> username = new ObservableField<String>();
        public ObservableField<String> password = new ObservableField<String>();

        public void onSignIn() {
            AuthenticationAPI.api.signInWithPassword(new AuthenticationAPI.Credential(username.get(), password.get())).enqueue(new Callback<SignInWithPasswordApiResult>() {
                @Override
                public void onResponse(Call<SignInWithPasswordApiResult> call, Response<SignInWithPasswordApiResult> response) {
                    Toast.makeText(AuthenticationActivity.this, "Call API successfully", Toast.LENGTH_SHORT).show();
                    SignInWithPasswordApiResult result = response.body();
                    if (result != null && result.getStatusCode().equals("10000")) {
                        // set user information
                    }
                }

                @Override
                public void onFailure(Call<SignInWithPasswordApiResult> call, Throwable t) {
                    Toast.makeText(AuthenticationActivity.this, "Call API failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAuthenticationBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        activityAuthenticationBinding.setViewModel(viewmodel);
        setContentView(activityAuthenticationBinding.getRoot());
    }
}