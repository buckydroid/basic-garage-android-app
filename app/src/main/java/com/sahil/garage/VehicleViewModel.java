package com.sahil.garage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VehicleViewModel extends AndroidViewModel {
    private VehicleRepository repository;
    private LiveData<List<Vehicle>> allVehicles;
    private String username;
    public VehicleViewModel(@NonNull Application application) {
        super(application);
        username = UserHolder.getInstance().getUsername();
        System.out.println("Username in Model : "+username);

        repository = new VehicleRepository(application, username);
        this.username = username;
        allVehicles = repository.getAllVehicles();
    }

    public void insert(Vehicle vehicle) {
        System.out.println("Added data to database");
        repository.insert(vehicle);
    }

    public void update(Vehicle vehicle) {
        repository.update(vehicle);
    }

    public void delete(Vehicle vehicle) {
        repository.delete(vehicle);
    }

    public LiveData<List<Vehicle>> getAllVehicles(String username) {
        return allVehicles;
    }
}
