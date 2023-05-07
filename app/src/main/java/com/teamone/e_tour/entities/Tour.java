package com.teamone.e_tour.entities;

import java.io.Serializable;
import java.util.Date;

public class Tour implements Serializable {
    private String _id;
    private String name;
    private String description;
    private long price;
    private Date from;
    private Date to;
    private String type;
    private String touristRoute;

    public Tour() {
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTouristRoute() {
        return touristRoute;
    }

    public void setTouristRoute(String touristRoute) {
        this.touristRoute = touristRoute;
    }

}
