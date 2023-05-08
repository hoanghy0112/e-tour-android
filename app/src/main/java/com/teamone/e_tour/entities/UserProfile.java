package com.teamone.e_tour.entities;

import java.io.Serializable;

public class UserProfile implements Serializable {

    private String _id;
    private String fullName = "";
    private String email;
    private String image;
    private String phoneNumber;
    private String address;
    private String identity;
    private Boolean isForeigner;
    private Boolean isPhoneVerified;
    private Boolean isEmailVerified;

    public UserProfile() {
    }

    public UserProfile(String fullName, String email, String image, String phoneNumber, String address, String identity, Boolean isForeigner, Boolean isPhoneVerified, Boolean isEmailVerified) {
        this.fullName = fullName;
        this.email = email;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.identity = identity;
        this.isForeigner = isForeigner;
        this.isPhoneVerified = isPhoneVerified;
        this.isEmailVerified = isEmailVerified;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Boolean getForeigner() {
        return isForeigner;
    }

    public void setForeigner(Boolean foreigner) {
        isForeigner = foreigner;
    }

    public Boolean getPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }
}
