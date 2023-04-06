package com.sahil.garage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Vehicle.class}, version = 1)
public abstract class VehiclesDatabase extends RoomDatabase {
    public abstract VehicleDao vehicleDao();

    private static volatile VehiclesDatabase INSTANCE;

    public static VehiclesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (VehiclesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VehiclesDatabase.class, "vehicles_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
