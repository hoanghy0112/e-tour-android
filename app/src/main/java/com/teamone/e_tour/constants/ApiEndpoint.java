package com.teamone.e_tour.constants;

public class ApiEndpoint {
    public static String baseUrl = "https://etour-server.hoanghy.tech/";

    public static class AuthenticationApiEndpoint {
        public static final String signIn = "user/login/basic";
    }

    public static class RegistrationApiEndpoint {
        public static final String registerWithPassword = "user/signup/basic";
    }
}
