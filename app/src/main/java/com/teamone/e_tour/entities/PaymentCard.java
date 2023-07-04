package com.teamone.e_tour.entities;

import java.util.Date;

public class PaymentCard {
    public String cardNumber;
    public String name;
    public Date expiredDate;
    public String cvv;
    public String type;
    public String _id;
    public boolean isDefault;

    public PaymentCard(String cardNumber, String name, Date expiredDate, String cvv, String type) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.expiredDate = expiredDate;
        this.cvv = cvv;
        this.type = type;
    }
}
