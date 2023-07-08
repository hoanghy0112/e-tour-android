package com.teamone.e_tour.models;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.teamone.e_tour.api.ticket.BookTicketApi;
import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.entities.Ticket;
import com.teamone.e_tour.entities.UserProfile;
import com.teamone.e_tour.entities.Voucher;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class BookingDataManager {
    private static BookingDataManager instance;
    private String routeName;
    private String imageUri = "";
    private String tourName;
    private String description;
    private Date departureDate;
    private int numOfVisitor;
    private long price;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.postValue(errorMessage);
    }

    public void setBookedTicket(MutableLiveData<Ticket> bookedTicket) {
        this.bookedTicket = bookedTicket;
    }

    public String getImageUri() {
        return ApiEndpoint.baseUrl + "images/" + imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    private MutableLiveData<Ticket> bookedTicket = new MutableLiveData<>(null);

    public MutableLiveData<Ticket> getBookedTicket() {
        return bookedTicket;
    }

    public void setBookedTicket(Ticket bookedTicket) {
        this.bookedTicket.postValue(bookedTicket);
    }

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

    public void addUserAsVisitor(Context context) {
        UserProfile userProfile = UserProfileManager.getInstance(context).getUserProfile();
        Ticket.Visitor visitor = new Ticket.Visitor();
        visitor.setName(userProfile.getFullName());
        visitor.setPhoneNumber(userProfile.getPhoneNumber());
        visitor.setAddress(userProfile.getAddress());
        visitor.setAge(18);

        ArrayList<Ticket.Visitor> visitors = Objects.requireNonNull(ticketData.getValue()).ticketInfo.getVisitors();
        visitors.add(visitor);

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

    public void changeVisitorInfo(int index, Ticket.Visitor visitorInfo) {
        ArrayList<Ticket.Visitor> visitors = Objects.requireNonNull(ticketData.getValue()).ticketInfo.getVisitors();
        visitors.set(index, visitorInfo);

        BookTicketApi.RequestBody oldTicketData = ticketData.getValue();
        oldTicketData.ticketInfo.setVisitors(visitors);
        ticketData.postValue(oldTicketData);
    }

    public MutableLiveData<BookTicketApi.RequestBody> getTicketLiveData() {
        return ticketData;
    }

    public void setTicketData(BookTicketApi.RequestBody ticketData) {
        this.ticketData.postValue(ticketData);
    }

    public long getTotalPrice() {
        long total = numOfVisitor * price;

        Voucher voucher = VoucherManager.getInstance().getSelectVoucher().getValue();

        if (voucher == null) {
            return total;
        } else {
            if (voucher.getType().equals("money")) {
                total -= voucher.getValue();
            } else {
                total -= total * voucher.getValue();
            }
        }

        return total;
    }
}
