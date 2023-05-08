package com.teamone.e_tour.entities;

import java.io.Serializable;

public class Rating implements Serializable {
    private String _id;
    private float star;
    private String description;
    private UserProfile userId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserProfile getUserId() {
        return userId;
    }

    public void setUserId(UserProfile userId) {
        this.userId = userId;
    }
}
