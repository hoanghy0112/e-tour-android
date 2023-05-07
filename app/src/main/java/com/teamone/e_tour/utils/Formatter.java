package com.teamone.e_tour.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {
    static public String toCurrency(long currency) {
        return String.format(Locale.ENGLISH, "%,d", currency) + " VND";
    }

    static public String dateToString(Date date) {
        String pattern = "K:m a, EE, dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return simpleDateFormat.format(date);
    }
}
