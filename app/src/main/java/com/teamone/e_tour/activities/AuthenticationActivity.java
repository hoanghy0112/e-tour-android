package com.teamone.e_tour.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.R;
import com.teamone.e_tour.api.account.authentication.AuthenticationAPI;
import com.teamone.e_tour.api.account.authentication.SignInWithGoogleAPI;
import com.teamone.e_tour.api.account.authentication.SignInWithPasswordApiError;
import com.teamone.e_tour.api.account.authentication.SignInWithPasswordApiResult;
import com.teamone.e_tour.api.account.registration.SignUpWithGoogleAPI;
import com.teamone.e_tour.databinding.ActivityAuthenticationBinding;
import com.teamone.e_tour.dialogs.LoadingDialog;
import com.teamone.e_tour.models.AppManagement;
import com.teamone.e_tour.models.CredentialToken;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity {
    private Context context = this;
    LoadingDialog dialog;
    ActivityAuthenticationBinding activityAuthenticationBinding;
    AuthenticationInfo viewmodel = new AuthenticationInfo();

    public class AuthenticationInfo extends BaseObservable {
        public AuthenticationInfo() {
        }

        public ObservableField<String> username = new ObservableField<>();
        public ObservableField<String> password = new ObservableField<>();

        public void signInWithGoogle() {
            goToSignIn();
        }

        public void onSignUp() {
            startActivity(new Intent(AuthenticationActivity.this, RegistrationActivity.class));
        }

        public void onSignIn() {
            dialog = new LoadingDialog(AuthenticationActivity.this);
            dialog.showLoading(context.getResources().getText(R.string.please_wait_we_are_signing_in_for_you).toString());

            AuthenticationAPI.api.signInWithPassword(new AuthenticationAPI.Credential(username.get(), password.get())).enqueue(new Callback<SignInWithPasswordApiResult>() {
                @Override
                public void onResponse(@NonNull Call<SignInWithPasswordApiResult> call, @NonNull Response<SignInWithPasswordApiResult> response) {
                    if (response.code() == 200 && response.body() != null) {
                        SignInWithPasswordApiResult result = response.body();
                        Log.e("body", result.getUserId() + " - " + result.getAccessToken());
                        CredentialToken.getInstance(context).setCredential(result.getUserId(), result.getAccessToken(), result.getRefreshToken());
                        SocketManager.reload(context);
                        startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
                        dialog.dismiss();
                        AuthenticationActivity.this.finish();
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

    private void goToSignIn() {
        Log.e("sign in with google", "signing in");
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert response != null;
            String email = response.getEmail();
            Toast.makeText(AuthenticationActivity.this, response.getEmail(), Toast.LENGTH_SHORT).show();
            assert user != null;
            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult getTokenResult) {
                    String token = getTokenResult.getToken();
                    Log.e("token", token);
//                    Toast.makeText(AuthenticationActivity.this, token, Toast.LENGTH_SHORT).show();
                    SignInWithGoogleAPI.api.signInWithGoogle(new SignInWithGoogleAPI.Credential(token)).enqueue(new Callback<SignInWithPasswordApiResult>() {
                        @Override
                        public void onResponse(Call<SignInWithPasswordApiResult> call, Response<SignInWithPasswordApiResult> response) {
                            if (response.code() == 200 && response.body() != null) {
                                SignInWithPasswordApiResult result = response.body();
                                CredentialToken.getInstance(context).setCredential(result.getUserId(), result.getAccessToken(), result.getRefreshToken());
                                SocketManager.reload(context);
                                startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
                                if (dialog != null) dialog.dismiss();
                                finish();
                            } else {
                                Gson gson = new GsonBuilder().create();

                                try {
                                    assert response.errorBody() != null;
                                    String errorBody = String.valueOf(response.errorBody().string());
                                    Log.e("errorbody", errorBody);
                                    SignInWithPasswordApiError result = gson.fromJson(errorBody.toString(), SignInWithPasswordApiError.class);
                                    Log.e("message", result.getMessage());
                                    Toast.makeText(AuthenticationActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (result.getMessage().equals("User not found")) {
                                        // Not registered
                                        Intent intent = new Intent(AuthenticationActivity.this, SignUpWithGoogle.class);
                                        intent.putExtra("accessToken", token);
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                    }
                                    if (dialog != null)
                                        dialog.showError(context.getResources().getText(R.string.fail_to_sign_in).toString() + "\nError message: " + result.getMessage());

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SignInWithPasswordApiResult> call, Throwable t) {
                            Toast.makeText(AuthenticationActivity.this, "Call API failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            Log.e("error", result.getIdpResponse().toString());
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