package com.teamone.e_tour.services;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.teamone.e_tour.converters.DateConverter;
import com.teamone.e_tour.converters.StringArrayConverter;
import com.teamone.e_tour.dataAccessObject.TouristRouteDAO;
import com.teamone.e_tour.entities.TouristRoute;

//@Database(entities = {TouristRoute.class}, version = 1)
//@TypeConverters({StringArrayConverter.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
//    public abstract TouristRouteDAO touristRouteDAO();
}
