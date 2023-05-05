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

public class BookTicketApi {
    public static BookTicketApi instance;
    public static final String emitEvent = "book-ticket";
    public static final String serverResponseEvent = "booked-ticket";

    public static class RequestBody implements Serializable {
        public TicketInfo ticketInfo;
        public ArrayList<String> voucherIds;

        public RequestBody() {
            ticketInfo = new TicketInfo();
            voucherIds = new ArrayList<>();
        }

        public class TicketInfo implements Serializable {
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

            public TicketInfo(String tourId, String fullName, String email, String phoneNumber, ArrayList<Ticket.Visitor> visitors) {
                this.tourId = tourId;
                this.fullName = fullName;
                this.email = email;
                this.phoneNumber = phoneNumber;
                this.visitors = visitors;
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


    public BookTicketApi(Context context) {
        this.context = context;
    }

    public static BookTicketApi getInstance(Context context) {
        if (instance == null) {
            instance = new BookTicketApi(context);
        }
        return instance;
    }

    public void send(RequestBody body) {
        JSONObject object;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

        try {
            String jsonString = gson.toJson(body);
            object = new JSONObject(jsonString);
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
            }
        });

        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ResponseData response = gson.fromJson(String.valueOf(args[0]), ResponseData.class);
                BookingDataManager.getInstance().setErrorMessage(response.message);
            }
        });
    }

    public void finish() {
        if (socket != null && socket.getSocket() != null)
            socket.getSocket().disconnect();
    }
}
