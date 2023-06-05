package com.teamone.e_tour.api.ticket;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.tour.ViewTourListOfRouteApi;
import com.teamone.e_tour.entities.Ticket;
import com.teamone.e_tour.entities.Tour;
import com.teamone.e_tour.models.BookingDataManager;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import io.socket.emitter.Emitter;
import okhttp3.ResponseBody;

public class UpdateTicketApi {
    public static UpdateTicketApi instance;
    public static final String emitEvent = "update-ticket";
    public static final String serverResponseEvent = "update-ticket-result";

    public static class RequestBody implements Serializable {
        public TicketInfo ticketInfo;
        public String ticketId;

        public RequestBody(TicketInfo ticketInfo, String ticketId) {
            this.ticketInfo = ticketInfo;
            this.ticketId = ticketId;
        }

        public static class TicketInfo implements Serializable {
            private String tourId;
            private String fullName;
            private String email;
            private String phoneNumber;
            private String specialRequirement;
            private String pickupLocation;

            public String getSpecialRequirement() {
                return specialRequirement;
            }

            public void setSpecialRequirement(String specialRequirement) {
                this.specialRequirement = specialRequirement;
            }

            public String getPickupLocation() {
                return pickupLocation;
            }

            public void setPickupLocation(String pickupLocation) {
                this.pickupLocation = pickupLocation;
            }

            private ArrayList<Ticket.Visitor> visitors = new ArrayList<>();

            public String getTourId() {
                return tourId;
            }

            public String getFullName() {
                return fullName;
            }

            public String getEmail() {
                return email;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public ArrayList<Ticket.Visitor> getVisitors() {
                return visitors;
            }

            public void setTourId(String tourId) {
                this.tourId = tourId;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public void setVisitors(ArrayList<Ticket.Visitor> visitors) {
                this.visitors = visitors;
            }

            public TicketInfo() {
            }

            public TicketInfo(String tourId, String fullName, String email, String phoneNumber, ArrayList<Ticket.Visitor> visitors, String pickupLocation, String specialRequirement) {
                this.tourId = tourId;
                this.fullName = fullName;
                this.email = email;
                this.phoneNumber = phoneNumber;
                this.visitors = visitors;
                this.pickupLocation = pickupLocation;
                this.specialRequirement = specialRequirement;
            }
        }
    }

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public Ticket data;

        public ResponseData() {
        }
    }


    private Context context;
    private SocketManager socket;

    public MutableLiveData<Ticket> getData() {
        return data;
    }

    private MutableLiveData<Ticket> data = new MutableLiveData<>(null);
    private MutableLiveData<ResponseData> responseData = new MutableLiveData<>(null);


    public UpdateTicketApi(Context context) {
        this.context = context;
    }

    public static UpdateTicketApi getInstance(Context context) {
        if (instance == null) {
            instance = new UpdateTicketApi(context);
        }
        return instance;
    }

    public void send(RequestBody body) {
        JSONObject object;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

        responseData.postValue(null);

        try {
            String jsonString = gson.toJson(body);
            object = new JSONObject(jsonString);
            Log.e("object shape", jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        socket = new SocketManager(context);
        socket.emit(emitEvent, object);

        socket.on(serverResponseEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ResponseData response = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
                if (response.status == 200) {
                    data.postValue(response.data);
                }
                responseData.postValue(response);
            }
        });

        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ResponseData response = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
            }
        });
    }

    public MutableLiveData<ResponseData> getResponseData() {
        return responseData;
    }

    public void finish() {
        if (socket != null && socket.getSocket() != null)
            socket.getSocket().disconnect();
    }
}
