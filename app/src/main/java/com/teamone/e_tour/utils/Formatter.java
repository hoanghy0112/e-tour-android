package com.teamone.e_tour.utils;

import java.util.Locale;

public class Formatter {
    static public String toCurrency(long currency) {
        return String.format(Locale.ENGLISH, "%,d", currency) + " VND";
    }
}
