package com.teamone.e_tour.api;

import android.content.Context;

import com.teamone.e_tour.models.CredentialToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {
    static ApiClient instance;
    OkHttpClient client;

    public static ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public ApiClient(Context context) {
        client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String accessToken = CredentialToken.getInstance(context).getAccessToken();
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
    }

    public OkHttpClient getClient() {
        return client;
    }
}
