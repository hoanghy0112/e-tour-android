package com.teamone.e_tour.models;

import android.content.Context;

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
    private UserProfile userProfile = new UserProfile();

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
        return userProfile;
    }

    public void onUserProfileUpdated(IUserProfileUpdateCallback callback) {
        SocketManager socket = SocketManager.getInstance(context);

        socket.on(SocketMessage.Server.USER_PROFILE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String stringResponse = String.valueOf(args[0]);
                Gson gson = new GsonBuilder().create();

                ViewUserProfile.ResponseData response = gson.fromJson(stringResponse, ViewUserProfile.ResponseData.class);

                userProfile = response.data;

                callback.onChange(userProfile);
            }
        });
    }

    public void watch() {
        SocketManager socket = SocketManager.getInstance(context);
        socket.emit(SocketMessage.Client.VIEW_USER_PROFILE);
    }
}
