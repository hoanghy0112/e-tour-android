package com.teamone.e_tour.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.account.authentication.AuthenticationAPI;
import com.teamone.e_tour.api.account.authentication.SignInWithPasswordApiError;
import com.teamone.e_tour.api.account.authentication.SignInWithPasswordApiResult;
import com.teamone.e_tour.databinding.ActivityAuthenticationBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.utils.SocketManager;

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
            LoadingDialog dialog = new LoadingDialog(AuthenticationActivity.this);
            dialog.showLoading(context.getResources().getText(R.string.please_wait_we_are_signing_in_for_you).toString());

            AuthenticationAPI.api.signInWithPassword(new AuthenticationAPI.Credential(username.get(), password.get())).enqueue(new Callback<SignInWithPasswordApiResult>() {
                @Override
                public void onResponse(@NonNull Call<SignInWithPasswordApiResult> call, @NonNull Response<SignInWithPasswordApiResult> response) {
                    if (response.code() == 200 && response.body() != null) {
                        SignInWithPasswordApiResult result = response.body();
                        CredentialToken.getInstance(context).setCredential(result.getUserId(), result.getAccessToken(), result.getRefreshToken());
                        SocketManager.reload(context);
                        startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
                        dialog.dismiss();
                        finish();
                    } else {
                        Gson gson = new GsonBuilder().create();
                        try {
                            assert response.errorBody() != null;
                            SignInWithPasswordApiError error = gson.fromJson(response.errorBody().string(), SignInWithPasswordApiError.class);
                            dialog.showError(context.getResources().getText(R.string.fail_to_sign_in).toString() + "\nError message: " + error.getMessage());
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
        AppManagement.getInstance(this).setFirstTime(false);
        activityAuthenticationBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        activityAuthenticationBinding.setViewModel(viewmodel);
        setContentView(activityAuthenticationBinding.getRoot());
    }
}