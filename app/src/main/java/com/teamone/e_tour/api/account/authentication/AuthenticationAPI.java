package com.teamone.e_tour.api.account.authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.ApiEndpoint;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthenticationAPI {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    AuthenticationAPI api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuthenticationAPI.class);

    public class Credential {
        public String username;
        public String password;

        public Credential(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @POST(ApiEndpoint.AuthenticationApiEndpoint.signIn)
    Call<SignInWithPasswordApiResult> signInWithPassword(@Body Credential credential);
}
