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

public interface RegistrationAPI {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    RegistrationAPI api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RegistrationAPI.class);

    public class RegistrationForm {
        public String fullName;
        public String identity;
        public Boolean isForeigner;
        public String email;
        public String username;
        public String password;
        public String phoneNumber;
        public String address;

        public RegistrationForm(String fullName, String identity, Boolean isForeigner, String email, String username, String password, String phoneNumber, String address) {
            this.fullName = fullName;
            this.identity = identity;
            this.isForeigner = isForeigner;
            this.email = email;
            this.username = username;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.address = address;
        }
    }

    public class RegistrationWithPasswordSuccess implements Serializable {
        private String statusCode;
        private String message;
        private Data data;

        public class Data {

            private UserInfo user;
            private Token tokens;

            public class UserInfo {
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

            public class Token {
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

    @POST(ApiEndpoint.RegistrationApiEndpoint.registerWithPassword)
    Call<RegistrationWithPasswordSuccess> registerWithPassword(@Body RegistrationForm registrationForm);
}
