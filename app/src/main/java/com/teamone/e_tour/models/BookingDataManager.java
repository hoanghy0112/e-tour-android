package com.teamone.e_tour.models;

import androidx.lifecycle.MutableLiveData;

import com.teamone.e_tour.api.ticket.BookTicketApi;
import com.teamone.e_tour.entities.Ticket;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class BookingDataManager {
    private static BookingDataManager instance;
    private String routeName;
    private String tourName;
    private String description;
    private Date departureDate;
    private int numOfVisitor;
    private long price;

    public int getNumOfVisitor() {
        return numOfVisitor;
    }

    public void setNumOfVisitor(int numOfVisitor) {
        this.numOfVisitor = numOfVisitor;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    private MutableLiveData<BookTicketApi.RequestBody> ticketData = new MutableLiveData<>(new BookTicketApi.RequestBody());

    public BookingDataManager() {
        routeName = "";
        tourName = "";
        description = "";
        departureDate = new Date();
    }

    public static BookingDataManager getInstance() {
        if (instance == null) {
            instance = new BookingDataManager();
        }
        return instance;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public BookTicketApi.RequestBody getTicketData() {
        return ticketData.getValue();
    }

    public void addNewVisitor() {
        ArrayList<Ticket.Visitor> visitors = Objects.requireNonNull(ticketData.getValue()).ticketInfo.getVisitors();
        visitors.add(new Ticket.Visitor());

        BookTicketApi.RequestBody oldTicketData = ticketData.getValue();
        oldTicketData.ticketInfo.setVisitors(visitors);
        ticketData.postValue(oldTicketData);
    }

    public void removeVisitor(int index) {
        ArrayList<Ticket.Visitor> visitors = Objects.requireNonNull(ticketData.getValue()).ticketInfo.getVisitors();
        visitors.remove(index);

        BookTicketApi.RequestBody oldTicketData = ticketData.getValue();
        oldTicketData.ticketInfo.setVisitors(visitors);
        ticketData.postValue(oldTicketData);
    }

    public  void changeVisitorInfo(int index, Ticket.Visitor visitorInfo) {
        ArrayList<Ticket.Visitor> visitors = Objects.requireNonNull(ticketData.getValue()).ticketInfo.getVisitors();
        visitors.set(index, visitorInfo);

        BookTicketApi.RequestBody oldTicketData = ticketData.getValue();
        oldTicketData.ticketInfo.setVisitors(visitors);
        ticketData.postValue(oldTicketData);
    }

    public MutableLiveData<BookTicketApi.RequestBody> getTicketLiveData() {
        return  ticketData;
    }

    public void setTicketData(BookTicketApi.RequestBody ticketData) {
        this.ticketData.postValue(ticketData);
    }
}
