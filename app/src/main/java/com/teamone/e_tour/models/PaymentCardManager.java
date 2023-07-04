package com.teamone.e_tour.models;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.teamone.e_tour.entities.PaymentCard;

import java.util.ArrayList;

public class PaymentCardManager {
    static PaymentCardManager instance;

    MutableLiveData<PaymentCard> defaultCard;

    MutableLiveData<ArrayList<PaymentCard>> cardList = new MutableLiveData<>();

    public PaymentCardManager() {
    }

    public static PaymentCardManager getInstance() {
        if (instance == null) {
            instance = new PaymentCardManager();
        }
        return instance;
    }

    public MutableLiveData<PaymentCard> getDefaultCard() {
        return defaultCard;
    }

    public void setDefaultCard(MutableLiveData<PaymentCard> defaultCard) {
        this.defaultCard = defaultCard;
    }

    public MutableLiveData<ArrayList<PaymentCard>> getCardList() {
        return cardList;
    }

    public void setCardList(ArrayList<PaymentCard> cardList) {
        this.cardList.postValue(cardList);
    }
}
