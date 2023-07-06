package com.teamone.e_tour.api.paymentCard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamone.e_tour.constants.ApiEndpoint;
import com.teamone.e_tour.entities.PaymentCard;

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

public interface PaymentCardApi {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    PaymentCardApi api = new Retrofit
            .Builder()
            .baseUrl(ApiEndpoint.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PaymentCardApi.class);

    public class ViewAllCardsResponse implements Serializable {
        public String statusCode;
        public String message;
        public ArrayList<PaymentCard> data;
    }

    public class UpdatedResponse implements Serializable {

    }

    public class MakeDefaultBody implements Serializable {
        public String cardId;

        public MakeDefaultBody(String cardId) {
            this.cardId = cardId;
        }
    }

    public class UpdateCardInfoBody implements Serializable {
        public String cardNumber;
        public String name;
        public Date expiredDate;
        public String cvv;
        public String type;

        public UpdateCardInfoBody(String cardNumber, String name, Date expiredDate, String cvv, String type) {
            this.cardNumber = cardNumber;
            this.name = name;
            this.expiredDate = expiredDate;
            this.cvv = cvv;
            this.type = type;
        }
    }


    @GET(ApiEndpoint.PaymentCardApiEndpoint.viewAllCards)
    Call<ViewAllCardsResponse> viewAllCards(@Header("Authorization") String accessToken);

    @DELETE(ApiEndpoint.PaymentCardApiEndpoint.updateCardInfo)
    Call<UpdatedResponse> deleteCard(@Header("Authorization") String accessToken, @Path("id") String id);

    @PUT(ApiEndpoint.PaymentCardApiEndpoint.defaultCard)
    Call<UpdatedResponse> makeDefault(@Header("Authorization") String accessToken, @Body MakeDefaultBody body);

    @POST(ApiEndpoint.PaymentCardApiEndpoint.addNewCard)
    Call<UpdatedResponse> addNewCard(@Header("Authorization") String accessToken, @Body UpdateCardInfoBody newCard);

    @PUT(ApiEndpoint.PaymentCardApiEndpoint.updateCardInfo)
    Call<UpdatedResponse> updateCardInfo(@Header("Authorization") String accessToken, @Path("id") String id, @Body UpdateCardInfoBody newCard);
}
