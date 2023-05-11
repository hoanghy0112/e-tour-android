package com.teamone.e_tour.api.route;

import com.teamone.e_tour.entities.TouristRoute;

import java.util.ArrayList;

public class ViewPopularRoute {
    public static final String emitEvent = "view-popular-route";
    public static final String serverResponseEvent = "view-popular-route-result";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public ArrayList<TouristRoute> data;
    }
}
