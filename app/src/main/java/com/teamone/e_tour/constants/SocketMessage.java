package com.teamone.e_tour.constants;

public class SocketMessage {
    public static class Client {
        public static final String VIEW_USER_PROFILE = "view-user-profile";
        public static final String REMOVE_LISTENER = "remove-listener";

        public static final String INCREASE_POINT = "increase-point";
    }

    public static class Server {
        public static final String USER_PROFILE = "user-profile";
        public static final String ERROR = "error";
    }
}
