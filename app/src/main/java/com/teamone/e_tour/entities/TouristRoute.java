package com.teamone.e_tour.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "touristRoutes")
public class TouristRoute {
    @PrimaryKey
    @NonNull
    private String _id;
    private String companyId;
    private String name;
    private String description;
    private String type;
    private Long reservationFee;
    private float rate;
    private int num;
    private ArrayList<String> route;
    private ArrayList<String> images;
    private int point;
    private Date createdAt;
    private boolean isFollowing;
    private boolean isSaved;
    private Company company;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String get_id() {
        return _id;
    }

    public TouristRoute() {
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
