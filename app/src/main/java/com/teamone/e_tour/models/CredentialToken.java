package com.teamone.e_tour.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.route.ViewDetailRouteApi;
import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.utils.MySharedPreferences;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class CredentialToken {
    private Context context;
    private static CredentialToken instance;
    private String id;
    private String accessToken;
    private String refreshToken;
    private String listenerId;

    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();


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
        updateUserProfile();
    }

    private void updateUserProfile() {
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);

        UserProfile userProfile = new UserProfile();
        userProfile.setFullName(mySharedPreferences.getSharedPreferences().getString("fullname", ""));
        userProfile.setEmail(mySharedPreferences.getSharedPreferences().getString("email", ""));
        userProfile.setImage(mySharedPreferences.getSharedPreferences().getString("image", ""));

        this.userProfile.postValue(userProfile);
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

    public String getBearerAccessToken() {
        return "Bearer " + accessToken;
    }

    public MutableLiveData<UserProfile> getUserProfile() {
        return userProfile;
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

        SocketManager.getInstance(context).getSocket().emit("view-user-profile");

        editor.apply();
    }

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public String listenerId;
        public UserProfile data;
    }
}
