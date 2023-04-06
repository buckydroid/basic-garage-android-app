package com.sahil.garage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VehicleService {
    @GET("vehicles/getallmakes?format=json")
    Call<VehicleMakeResponse> getVehicleMakes();

    @GET("vehicles/GetModelsForMakeId/{makeId}?format=json")
    Call<VehicleModelResponse> getVehicleModels(@Path("makeId") int makeId);
}
