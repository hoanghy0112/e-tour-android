package com.teamone.e_tour.api.route;

import com.teamone.e_tour.entities.TouristRoute;
import com.teamone.e_tour.entities.UserProfile;

import java.util.ArrayList;

public class ViewRecommendedRoute {
    public static final String emitEvent = "view-recommend-route";
    public static final String serverResponseEvent = "list-route";

    public class ResponseData {
        public String statusCode;
        public String message;
        public int status;
        public ArrayList<TouristRoute> data;
    }
}
