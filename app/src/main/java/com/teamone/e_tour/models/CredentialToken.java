package com.teamone.e_tour.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.teamone.e_tour.utils.MySharedPreferences;

public class CredentialToken {
    private Context context;
    private static CredentialToken instance;
    private String id;
    private String accessToken;
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public CredentialToken() {
    }

    public CredentialToken(Context context) {
        this.context = context;
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        this.id = mySharedPreferences.getSharedPreferences().getString("id", "");
        this.accessToken = mySharedPreferences.getSharedPreferences().getString("accessToken", "");
        this.refreshToken = mySharedPreferences.getSharedPreferences().getString("refreshToken", "");
    }

    public static CredentialToken getInstance(Context context) {
        if (instance == null) {
            instance = new CredentialToken(context);
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setCredential(String id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        SharedPreferences.Editor editor = mySharedPreferences.getEditor();
        editor.putString("id", id);
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }

    public void refresh() {

    }
}
