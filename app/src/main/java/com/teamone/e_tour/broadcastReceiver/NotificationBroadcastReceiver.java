package com.teamone.e_tour.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public NotificationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("notification_cancelled")) {
//            int id = intent.getIntExtra("index", 0);
            String id = intent.getStringExtra("id");
            if (id != null) {
                JSONObject object = new JSONObject();
                JSONArray idList = new JSONArray();
                idList.put(id);
                try {
                    object.put("notificationIDs", idList);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SocketManager.getInstance(context).emit("read-notification", object);
            }
        }
    }
}
