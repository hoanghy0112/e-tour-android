package com.teamone.e_tour.api.account.authentication;

import java.io.Serializable;

public class SignInWithPasswordApiError implements Serializable {
    public String statusCode;
    public String message;

    public String getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
