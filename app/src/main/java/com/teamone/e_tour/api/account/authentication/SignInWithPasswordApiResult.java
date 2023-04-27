package com.teamone.e_tour.api.account.authentication;


import java.io.Serializable;

public class SignInWithPasswordApiResult implements Serializable {
    private String statusCode;
    private String message;
    private Data data;

    public static class Data implements Serializable {
        public UserInfo user;
        public Token tokens;

        public static class UserInfo implements Serializable {
            public String _id;
        }

        public static class Token implements Serializable {
            public String accessToken;
            public String refresshToken;
        }
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return data.user._id;
    }

    public String getAccessToken() {
        return data.tokens.accessToken;
    }

    public String getRefreshToken() {
        return data.tokens.refresshToken;
    }
}
