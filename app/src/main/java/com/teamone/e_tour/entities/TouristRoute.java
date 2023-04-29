package com.teamone.e_tour.entities;

import java.util.ArrayList;

public class TouristRoute {
    private String _id;
    private String companyId;
    private String name;
    private String description;
    private String type;
    private Long reservationFee;
    private ArrayList<String> route;
    private ArrayList<String> images;

    public TouristRoute(String _id, String companyId, String name, String description, String type, Long reservationFee, ArrayList<String> route, ArrayList<String> images) {
        this._id = _id;
        this.companyId = companyId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.reservationFee = reservationFee;
        this.route = route;
        this.images = images;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getReservationFee() {
        return reservationFee;
    }

    public void setReservationFee(Long reservationFee) {
        this.reservationFee = reservationFee;
    }

    public ArrayList<String> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<String> route) {
        this.route = route;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
