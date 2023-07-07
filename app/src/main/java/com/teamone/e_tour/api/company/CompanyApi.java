package com.teamone.e_tour.api.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.entities.Company;
import com.teamone.e_tour.entities.PaymentCard;
import com.teamone.e_tour.entities.TouristRoute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CompanyApi {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    CompanyApi api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CompanyApi.class);

    public class GetCompanyInfoResponse {
        public String message;
        public Company data;
    }

    public class GetTouristRouteOfCompany {
        public String message;
        public ArrayList<TouristRoute> data;
    }

    @GET(ApiEndpoint.CompanyApiEndpoint.getCompanyInfo)
    Call<GetCompanyInfoResponse> getCompanyInfo(@Path("id") String companyId, @Header("Authorization") String token);

    @GET(ApiEndpoint.RouteApiEndpoint.queryRoute)
    Call<GetTouristRouteOfCompany> getTouristRouteOfCompany(@Query("companyId") String companyId);
}
