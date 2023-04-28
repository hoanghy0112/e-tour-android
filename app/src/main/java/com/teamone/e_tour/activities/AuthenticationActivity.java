package com.teamone.e_tour.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.account.authentication.AuthenticationAPI;
import com.teamone.e_tour.api.account.authentication.SignInWithPasswordApiError;
import com.teamone.e_tour.api.account.authentication.SignInWithPasswordApiResult;
import com.teamone.e_tour.databinding.ActivityAuthenticationBinding;
import com.teamone.e_tour.dialogs.LoginDialog;
import com.teamone.e_tour.models.CredentialToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity {
    private Context context = this;
    ActivityAuthenticationBinding activityAuthenticationBinding;
    AuthenticationInfo viewmodel = new AuthenticationInfo();

    public class AuthenticationInfo extends BaseObservable {
        public AuthenticationInfo() {
        }

        public ObservableField<String> username = new ObservableField<>();
        public ObservableField<String> password = new ObservableField<>();

        public void onSignUp() {
            startActivity(new Intent(AuthenticationActivity.this, RegistrationActivity.class));
        }

        public void onSignIn() {
            LoginDialog dialog = new LoginDialog(AuthenticationActivity.this);
            dialog.showLoading();

            AuthenticationAPI.api.signInWithPassword(new AuthenticationAPI.Credential(username.get(), password.get())).enqueue(new Callback<SignInWithPasswordApiResult>() {
                @Override
                public void onResponse(@NonNull Call<SignInWithPasswordApiResult> call, @NonNull Response<SignInWithPasswordApiResult> response) {
                    if (response.code() == 200 && response.body() != null) {
                        SignInWithPasswordApiResult result = response.body();
                        CredentialToken.getInstance(context).setCredential(result.getUserId(), result.getAccessToken(), result.getRefreshToken());
                        startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
                        dialog.dismiss();
                    } else {
                        Gson gson = new GsonBuilder().create();
                        try {
                            assert response.errorBody() != null;
                            SignInWithPasswordApiError error = gson.fromJson(response.errorBody().string(), SignInWithPasswordApiError.class);
                            dialog.showError(error.getMessage());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SignInWithPasswordApiResult> call, @NonNull Throwable t) {
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
        Log.e("user id", CredentialToken.getInstance(this).getId());
    }
}