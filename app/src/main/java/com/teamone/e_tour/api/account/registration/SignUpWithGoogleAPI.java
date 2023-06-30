package com.teamone.e_tour.api.account.registration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.ApiEndpoint;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUpWithGoogleAPI {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    SignUpWithGoogleAPI api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SignUpWithGoogleAPI.class);

    public class RegistrationForm {
        public String fullName;
        public String identity;
        public Boolean isForeigner;
        public String email;
        public String phoneNumber;
        public String address;
        public String accessToken;

        public RegistrationForm(String fullName, String identity, Boolean isForeigner, String email, String phoneNumber, String address, String accessToken) {
            this.fullName = fullName;
            this.identity = identity;
            this.isForeigner = isForeigner;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.accessToken = accessToken;
        }
    }

    public class RegistrationWithPasswordSuccess implements Serializable {
        private String statusCode;
        private String message;
        private Data data;

        public static class Data {

            private UserInfo user;
            private Token tokens;

            public static class UserInfo {
                private String fullName;
                private String email;
                private String phoneNumber;
                private String address;
                private String image;
                private String identity;
                private Boolean isForeigner;
                private Boolean isPhoneVerified;
                private Boolean isEmailVerified;
                private String credential;
                private String _id;
            }

            public static class Token {
                private String accessToken;
                private String refreshToken;
            }
        }

        public String getUserId() {
            return data.user._id;
        }

        public String getAccessToken() {
            return data.tokens.accessToken;
        }

        public String getRefreshToken() {
            return data.tokens.refreshToken;
        }
    }

    @POST(ApiEndpoint.RegistrationApiEndpoint.registerWithGoogle)
    Call<RegistrationWithPasswordSuccess> registerWithGoogle(@Body RegistrationForm registrationForm);
}
