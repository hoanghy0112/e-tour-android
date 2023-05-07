package com.teamone.e_tour.models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.teamone.e_tour.api.ticket.ViewBookedTicketApi;
import com.teamone.e_tour.utils.SocketManager;

import java.util.ArrayList;

public class BookedTicketManager {
    private static BookedTicketManager instance;
    private AppCompatActivity context;
    private ViewBookedTicketApi api;

    private MutableLiveData<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>> bookedTickets = new MutableLiveData<>();

    public BookedTicketManager(AppCompatActivity context) {
        this.context = context;
        this.api = new ViewBookedTicketApi(new SocketManager(context));

        api.getResponse().observe(context, new Observer<ViewBookedTicketApi.ResponseData>() {
            @Override
            public void onChanged(ViewBookedTicketApi.ResponseData responseData) {
                if (responseData != null)
                    bookedTickets.postValue(responseData.data);
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
        bookedTickets = new MutableLiveData<>(new ArrayList<>());
        api.fetch();
    }

    public MutableLiveData<ArrayList<ViewBookedTicketApi.ResponseData.Ticket>> getBookedTickets() {
        return bookedTickets;
    }
}
