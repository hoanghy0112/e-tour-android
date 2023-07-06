package com.teamone.e_tour.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Formatter {
    static public String toCurrency(long currency) {
        return String.format(Locale.ENGLISH, "%,d", currency) + " VND";
    }

    static public String dateToDateWithHourString(Date date) {
        String pattern = "K:m a, EE, dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return simpleDateFormat.format(date);
    }

    static public String dateToDateWithoutHourString(Date date) {
        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return simpleDateFormat.format(date);
    }

    static public String dateToShortDateWithoutHourString(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return simpleDateFormat.format(date);
    }

    static public String dateToDateOnlyHourString(Date date) {
        String pattern = "K:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return simpleDateFormat.format(date);
    }

    static public String dateToDayString(Date date) {
        String pattern = "EE, dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return simpleDateFormat.format(date);
    }

    static public String dateToCardEXP(Date date) {
        String pattern = "MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return simpleDateFormat.format(date);
    }

    static public String dateToHourString(Date date) {
        String pattern = "K:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return simpleDateFormat.format(date);
    }

    static public Date fromCardExp(String dateStr) throws ParseException {
        String pattern = "MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("vi", "VN"));
        return Objects.requireNonNull(simpleDateFormat.parse(dateStr));
    }
}
