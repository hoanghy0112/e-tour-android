package com.teamone.e_tour.constants;

public class ApiEndpoint {
    //    public static String baseUrl = "https://localhost:3000/";
    public static String baseUrl = "https://etour-server.hoanghy.tech/";

    public static class AuthenticationApiEndpoint {
        public static final String signIn = "user/login/basic";
        public static final String signInWithGoogle = "user/login/google";
    }

    public static class TicketApiEndPoint {
        public static final String remove = "ticket/{id}";
    }

    public static class VoucherApiEndpoint {
        public static final String viewAllSaved = "voucher/save?populate=true";
    }

    public static class RouteApiEndpoint {
        public static final String queryRoute = "touristRoute/find";
    }

    public static class RegistrationApiEndpoint {
        public static final String registerWithPassword = "user/signup/basic";
        public static final String registerWithGoogle = "user/signup/google";
    }
}
