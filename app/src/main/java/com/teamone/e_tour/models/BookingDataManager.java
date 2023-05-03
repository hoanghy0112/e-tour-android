package com.teamone.e_tour.models;

import com.teamone.e_tour.api.ticket.BookTicketApi;

import java.util.Date;

public class BookingDataManager {
    private static BookingDataManager instance;
    private String routeName;
    private String tourName;
    private String description;
    private Date departureDate;

    private BookTicketApi.RequestBody ticketData;

    public BookingDataManager() {
        routeName = "";
        tourName = "";
        description = "";
        departureDate = new Date();
        ticketData = new BookTicketApi.RequestBody();
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
        return ticketData;
    }

    public void setTicketData(BookTicketApi.RequestBody ticketData) {
        this.ticketData = ticketData;
    }
}
