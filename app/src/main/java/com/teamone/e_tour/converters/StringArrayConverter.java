package com.teamone.e_tour.converters;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class StringArrayConverter {
    @TypeConverter
    public static ArrayList<String> fromSingleString(String value) {
        return value == null ? null : new ArrayList<String>(Arrays.asList(value.split(";")));
    }

    @TypeConverter
    public static String toSingleString(ArrayList<String> value) {
        return value == null ? null : String.join(";", value);
    }
}
