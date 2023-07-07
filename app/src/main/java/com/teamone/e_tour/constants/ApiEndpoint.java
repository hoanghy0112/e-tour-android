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
        public static final String recommendQuery = "touristRoute/recommend";
        public static final String similarRoute = "touristRoute/recommend/similar";
    }

    public static class RegistrationApiEndpoint {
        public static final String registerWithPassword = "user/signup/basic";
        public static final String registerWithGoogle = "user/signup/google";
    }

    public static class ChatApiEndpoint {
        public static final String viewAllChats = "chat";
    }

    public static class PaymentCardApiEndpoint {
        public static final String viewAllCards = "/user/card";
        public static final String addNewCard = "/user/card";
        public static final String updateCardInfo = "/user/card/{id}";
        public static final String defaultCard = "/user/card/default";
    }

    public static class CompanyApiEndpoint {
        public static final String getCompanyInfo = "/company/{id}";
    }
}
