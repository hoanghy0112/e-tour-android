package com.teamone.e_tour.models;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.utils.SocketManager;

import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class BookedTicketManager {
    private static BookedTicketManager instance;
    private AppCompatActivity context;
    private ViewBookedTicketApi api;

    private MutableLiveData<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>> bookedTickets = new MutableLiveData<>();

    private String ratingTicketId;

    public BookedTicketManager(AppCompatActivity context) {
        this.context = context;
        this.api = new ViewBookedTicketApi(SocketManager.getInstance(context));

        viewBookedTickets();

        api.getSocket().on("booked-ticket-list", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

                ViewBookedTicketApi.ResponseData responseData = gson.fromJson(String.valueOf(args[0]), ViewBookedTicketApi.ResponseData.class);
                if (responseData.status == 200) {
                    bookedTickets.postValue(responseData.data);
                }
            }
        });
    }

    public static BookedTicketManager getInstance(AppCompatActivity context) {
        if (instance == null) {
            instance = new BookedTicketManager(context);
        }
        return instance;
    }

    public void viewBookedTickets() {
        api.finish();
//        bookedTickets = new MutableLiveData<>();
        api.fetch();
    }

    public MutableLiveData<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>> getBookedTickets() {
        return bookedTickets;
    }

    public String getRatingTicketId() {
        return ratingTicketId;
    }

    public void setRatingTicketId(String ratingTicketId) {
        this.ratingTicketId = ratingTicketId;
    }
}
