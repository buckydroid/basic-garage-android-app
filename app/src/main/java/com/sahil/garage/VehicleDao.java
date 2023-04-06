package com.sahil.garage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// VehicleDao.java
@Dao
public interface VehicleDao {
    @Insert
    void insert(Vehicle vehicle);

    @Update
    void update(Vehicle vehicle);

    @Delete
    void delete(Vehicle vehicle);

    @Query("SELECT * FROM Vehicle where username == :username")
    LiveData<List<Vehicle>> getAllVehicles(String username);
}
