package com.teamone.e_tour.entities;

import com.teamone.e_tour.constants.ApiEndpoint;

public class Image {
    private String imageId;

    public Image() {
        imageId = "";
    }

    public Image(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUri() {
        return ApiEndpoint.baseUrl + "images/" + imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
