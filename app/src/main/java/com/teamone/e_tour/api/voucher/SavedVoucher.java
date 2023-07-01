package com.teamone.e_tour.api.voucher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.entities.Company;
import com.teamone.e_tour.entities.Voucher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SavedVoucher {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    SavedVoucher api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SavedVoucher.class);

    public class ViewSavedVoucherResponse implements Serializable {
        public String statusCode;
        public String message;
        public ArrayList<Voucher> data;
    }

    class AddToSavedListBody implements Serializable {
        public String voucherId;

        public AddToSavedListBody(String voucherId) {
            this.voucherId = voucherId;
        }
    }

    class AddToSavedListResponse implements Serializable {

    }


    @GET(ApiEndpoint.VoucherApiEndpoint.viewAllSaved)
    Call<ViewSavedVoucherResponse> viewSavedVoucher(@Header("Authorization") String authorization);

    @PUT(ApiEndpoint.VoucherApiEndpoint.viewAllSaved)
    Call<AddToSavedListResponse> addToSavedList(@Body AddToSavedListBody body, @Header("Authorization") String authorization);


}
