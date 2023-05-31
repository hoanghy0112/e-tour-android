package com.teamone.e_tour.dataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.teamone.e_tour.entities.TouristRoute;

import java.util.ArrayList;

@Dao
public interface TouristRouteDAO {
    @Query("SELECT * FROM touristRoutes WHERE _id = :id")
    LiveData<TouristRoute> findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(TouristRoute... routes);
}
