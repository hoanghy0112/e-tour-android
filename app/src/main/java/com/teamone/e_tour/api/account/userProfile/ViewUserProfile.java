package com.teamone.e_tour.api.account.userProfile;


import com.teamone.e_tour.entities.UserProfile;

public class ViewUserProfile{
    public static final String emitEvent = "view-user-profile";
    public static final String serverResponseEvent = "user-profile";

    public class ResponseData {
        public String statusCode;
        public String message;
        public String status;
        public UserProfile data;
    }
}
