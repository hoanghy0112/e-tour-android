package com.teamone.e_tour.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Ticket implements Serializable {
    private String _id;
    private String userId;
    private String tourId;
    private String status;
    private String fullName;
    private String phoneNumber;
    private String email;
    private long price;
    private ArrayList<Visitor> visitors;

    public Date getCreatedAt() {
        return createdAt;
    }

    private Date createdAt;

    public Ticket() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public ArrayList<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(ArrayList<Visitor> visitors) {
        this.visitors = visitors;
    }

    public static class Visitor implements Serializable {
        private String name;
        private int age;
        private String address;
        private String phoneNumber;
//        private String request;

        public Visitor() {
            name = "";
        }

        public Visitor(String name, int age, String address, String phoneNumber) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
//
//        public String getRequest() {
//            return request;
//        }
//
//        public void setRequest(String request) {
//            this.request = request;
//        }
    }
}
