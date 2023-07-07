package com.teamone.e_tour.api.route;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.api.company.CompanyApi;
import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.entities.TouristRoute;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TouristRouteApi {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    TouristRouteApi api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TouristRouteApi.class);

    class FilterRouteDestinationResponse implements Serializable {
        public String statusCode;
        public String message;
        public ArrayList<TouristRoute> data;
    }

    class GetTouristRouteListResponse {
        public String message;
        public ArrayList<TouristRoute> data;
    }

    @GET(ApiEndpoint.RouteApiEndpoint.queryRoute)
    Call<FilterRouteDestinationResponse> filterRouteDestination(@Query("route") String route);

    @GET(ApiEndpoint.RouteApiEndpoint.recommendQuery)
    Call<GetTouristRouteListResponse> getRecommendRouteOfCompany(@Query("companyId") String companyId);

    @GET(ApiEndpoint.RouteApiEndpoint.similarRoute)
    Call<GetTouristRouteListResponse> getRecommendRouteOfSimilarity(@Query("routeId") String routeId, @Header("Authorization") String token);
}
