package com.teamone.e_tour.api.ticket;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.ApiClient;
import com.teamone.e_tour.api.account.authentication.AuthenticationAPI;
import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.entities.ApiResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public class TicketAPI {
    static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

    public interface API {
        @DELETE(ApiEndpoint.TicketApiEndPoint.remove)
        Call<ApiResponse> discardTicket(@Path("id") String id);
    }

    public static API getApi(Context context) {
        API api = new Retrofit
                .Builder()
                .client(ApiClient.getInstance(context).getClient())
                .baseUrl(ApiEndpoint.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(API.class);

        return api;
    }

}
