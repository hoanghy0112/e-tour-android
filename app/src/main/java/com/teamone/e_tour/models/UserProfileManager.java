package com.teamone.e_tour.models;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.account.userProfile.ViewUserProfile;
import com.teamone.e_tour.constants.SocketMessage;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.interfaces.IUserProfileUpdateCallback;
import com.teamone.e_tour.utils.SocketManager;

import io.socket.emitter.Emitter;

public class UserProfileManager {
    private static UserProfileManager instance;
    private Context context;
    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>(new UserProfile());

    public UserProfileManager(Context context) {
        this.context = context;
    }

    public static UserProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserProfileManager(context);
        }

        return instance;
    }

    public UserProfile getUserProfile() {
        return userProfile.getValue();
    }
    public MutableLiveData<UserProfile> getLiveUserProfile() {
        return userProfile;
    }

    public void fetchUserProfile() {
        SocketManager socket = SocketManager.getInstance(context);
        socket.emit(SocketMessage.Client.VIEW_USER_PROFILE);

        socket.on(ViewUserProfile.serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String stringResponse = String.valueOf(args[0]);
                Gson gson = new GsonBuilder().create();

                ViewUserProfile.ResponseData response = gson.fromJson(stringResponse, ViewUserProfile.ResponseData.class);

                userProfile.postValue(response.data);
            }
        });

        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("error", String.valueOf(args[0]));
            }
        });
    }
}
