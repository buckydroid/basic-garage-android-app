package com.sahil.garage;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class VehicleRepository {
    private VehicleDao vehicleDao;
    private LiveData<List<Vehicle>> allVehicles;
    private String username;

    public VehicleRepository(Application application, String username) {
        VehiclesDatabase database = VehiclesDatabase.getInstance(application);
        vehicleDao = database.vehicleDao();
        allVehicles = vehicleDao.getAllVehicles(username);
        this.username = username;
    }

    public void insert(Vehicle vehicle) {
        new InsertVehicleAsyncTask(vehicleDao).execute(vehicle);
    }

    public void update(Vehicle vehicle) {
        new UpdateVehicleAsyncTask(vehicleDao).execute(vehicle);
    }

    public void delete(Vehicle vehicle) {
        new DeleteVehicleAsyncTask(vehicleDao).execute(vehicle);
    }

    public LiveData<List<Vehicle>> getAllVehicles() {
        return allVehicles;
    }

    private static class InsertVehicleAsyncTask extends AsyncTask<Vehicle, Void, Void> {
        private VehicleDao vehicleDao;
        private InsertVehicleAsyncTask(VehicleDao vehicleDao) {
            this.vehicleDao = vehicleDao;
        }
        @Override
        protected Void doInBackground(Vehicle... vehicles) {
            vehicleDao.insert(vehicles[0]);
            return null;
        }
    }

    private static class UpdateVehicleAsyncTask extends AsyncTask<Vehicle, Void, Void> {
        private VehicleDao vehicleDao;
        private UpdateVehicleAsyncTask(VehicleDao vehicleDao) {
            this.vehicleDao = vehicleDao;
        }
        @Override
        protected Void doInBackground(Vehicle... vehicles) {
            vehicleDao.update(vehicles[0]);
            return null;
        }
    }

    private static class DeleteVehicleAsyncTask extends AsyncTask<Vehicle, Void, Void> {
        private VehicleDao vehicleDao;
        private DeleteVehicleAsyncTask(VehicleDao vehicleDao) {
            this.vehicleDao = vehicleDao;
        }
        @Override
        protected Void doInBackground(Vehicle... vehicles) {
            vehicleDao.delete(vehicles[0]);
            return null;
        }
    }
}
