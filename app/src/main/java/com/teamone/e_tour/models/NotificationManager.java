package com.teamone.e_tour.models;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.entities.NotificationItem;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import io.socket.emitter.Emitter;

public class NotificationManager {
    Context context;
    private static NotificationManager instance;
    private final MutableLiveData<ArrayList<NotificationItem>> unSeenNotificationItems = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<NotificationItem>> notificationList = new MutableLiveData<>();

    public NotificationManager(Context context) {
        this.context = context;
    }

    public MutableLiveData<ArrayList<NotificationItem>> getUnSeenNotificationItems() {
        return unSeenNotificationItems;
    }

    public MutableLiveData<ArrayList<NotificationItem>> getNotificationList() {
        return notificationList;
    }

    public static NotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationManager(context);
        }
        return instance;
    }

    public void fetchData() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

        SocketManager.getInstance(context).emit("view-all-notification", new JSONObject());
        SocketManager.getInstance(context).on("all-notification-list", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("response-data", String.valueOf(args[0]));
                ResponseData responseData = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
                if (responseData.status == 200) {
                    unSeenNotificationItems.postValue(responseData.data.stream().filter(notificationItem -> !notificationItem.isRead).collect(Collectors.toCollection(ArrayList::new)));
                    notificationList.postValue(responseData.data);
                }
            }
        });
    }

    private static class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public String listenerId;
        public Date createdAt;
        public ArrayList<NotificationItem> data;
    }
}
