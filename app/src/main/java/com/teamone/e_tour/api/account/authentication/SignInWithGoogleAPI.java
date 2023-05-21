package com.teamone.e_tour.api.account.authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.ApiEndpoint;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInWithGoogleAPI {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    SignInWithGoogleAPI api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SignInWithGoogleAPI.class);

    public class Credential {
        public String accessToken;

        public Credential(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @POST(ApiEndpoint.AuthenticationApiEndpoint.signInWithGoogle)
    Call<SignInWithPasswordApiResult> signInWithGoogle(@Body Credential credential);
}
