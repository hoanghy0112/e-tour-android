package com.teamone.e_tour.api.ticket;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.SocketMessage;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import io.socket.emitter.Emitter;

public class ViewBookedTicketApi {
    public static ViewBookedTicketApi instance;
    public static final String emitEvent = "view-booked-ticket";
    public static final String serverResponseEvent = "booked-ticket-list";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public String listenerId;
        public ArrayList<Ticket> data;

        public class Ticket implements Serializable {
            public String _id;
            public String userId;
            public Tour tourId;
            public String status;
            public String fullName;
            public String phoneNumber;
            public String email;
            public String pickupLocation;
            public String specialRequirement;
            public long price;
            public Date createdAt;
            public ArrayList<com.teamone.e_tour.entities.Ticket.Visitor> visitors;
        }

        public class Tour implements Serializable {
            public String _id;
            public String name;
            public String description;
            public Date from;
            public String type;
            public String image;
            public TouristRoute touristRoute;
        }

        public class TouristRoute implements Serializable {
            public String _id;
            public String name;
            public String description;
            public String type;
            public ArrayList<String> route;
            public ArrayList<String> images;
        }
    }

    private SocketManager socket;
    private MutableLiveData<ResponseData> response = new MutableLiveData<>(null);

    public MutableLiveData<ResponseData> getResponse() {
        return response;
    }

    public ViewBookedTicketApi(Context context) {
        socket = SocketManager.getInstance(context);
    }

    public ViewBookedTicketApi(SocketManager socket) {
        this.socket = socket;
    }


    public static ViewBookedTicketApi getInstance(Context context) {
        if (instance == null) {
            instance = new ViewBookedTicketApi(context);
        }
        return instance;
    }

    public void fetch() {
        JSONObject object;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

        socket.emit(emitEvent, new JSONObject());

//        socket.on(serverResponseEvent, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e("booked-ticket", String.valueOf(args[0]));
//                ResponseData responseData = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
//                if (responseData.status == 200) {
//                    response.postValue(responseData);
//                }
//            }
//        });

        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("debug", String.valueOf(args[0]));
            }
        });
    }

    public SocketManager getSocket() {
        return socket;
    }

    public void finish() {
        if (response.getValue() == null) return;

        final String listenerId = response.getValue().listenerId;

        JSONObject object = new JSONObject();
        try {
            object.put("listenerId", listenerId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        socket.emit(SocketMessage.Client.REMOVE_LISTENER);
    }
}
