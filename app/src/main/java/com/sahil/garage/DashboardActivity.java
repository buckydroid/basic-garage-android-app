package com.sahil.garage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardActivity extends AppCompatActivity {

    private Spinner spinnerVehicleMake, spinnerVehicleModel;
    private Button buttonAddCar;
    private RecyclerView recyclerViewVehicles;

    private VehicleListAdapter vehicleListAdapter;
    private VehicleViewModel vehicleViewModel;

    private ImageView logoutBtn;

    private String username;

    // Variables to store selected vehicle make and model
    private String selectedMake;
    private String selectedModel;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private int selectedPosition;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://vpic.nhtsa.dot.gov/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        spinnerVehicleMake = findViewById(R.id.spinner_vehicle_make);
        spinnerVehicleModel = findViewById(R.id.spinner_vehicle_model);
        buttonAddCar = findViewById(R.id.button_add_car);
        recyclerViewVehicles = findViewById(R.id.recycler_view_vehicles);
        SharedPreferences prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        username = prefs.getString("current_username",null);

        UserHolder.getInstance().setUsername(username);
        logoutBtn = findViewById(R.id.logout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("current_username", null);
                editor.apply();
                finish();

            }
        });
        // Set up Vehicle List RecyclerView
        vehicleListAdapter = new VehicleListAdapter();
        recyclerViewVehicles.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVehicles.setHasFixedSize(true);
        recyclerViewVehicles.setAdapter(vehicleListAdapter);

        vehicleListAdapter.setOnItemClickListener(new VehicleListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                Vehicle vehicle = vehicleListAdapter.getVehicleAtPosition(position);
                vehicleViewModel.delete(vehicle);
            }

            @Override
            public void onAddImageClick(int position) {
                // Implement functionality for adding an image to the car
                selectedPosition = position;
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent chooserIntent = Intent.createChooser(pickIntent, "Choose an action:");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});
                startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        // Set up VehicleViewModel
        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);
        vehicleViewModel.getAllVehicles(username).observe(this, new Observer<List<Vehicle>>() {
            @Override
            public void onChanged(List<Vehicle> vehicles) {
                if(vehicles.size()>0)
                System.out.println("Vehicle username : " +vehicles.get(0).getUsername());

                vehicleListAdapter.setVehicles(vehicles);
            }
        });

        // Load vehicle makes and set up onChange listeners
        loadVehicleMakes();

        buttonAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMake != null && selectedModel != null) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setMake(selectedMake);
                    vehicle.setUsername(username);
                    vehicle.setModel(selectedModel);
                    vehicleViewModel.insert(vehicle);
                }
            }
        });
    }

    // Implement the loadVehicleMakes and loadVehicleModels methods for API requests
    // Load vehicle makes and set up onChange listeners
    private void loadVehicleMakes() {
        VehicleService vehicleService = retrofit.create(VehicleService.class);

        Call<VehicleMakeResponse> vehicleMakeCall = vehicleService.getVehicleMakes();
        vehicleMakeCall.enqueue(new Callback<VehicleMakeResponse>() {
            @Override
            public void onResponse(Call<VehicleMakeResponse> call, Response<VehicleMakeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<VehicleMake> vehicleMakes = response.body().getResults();
                    VehicleMakeAdapter vehicleMakeAdapter = new VehicleMakeAdapter(DashboardActivity.this, vehicleMakes);
                    spinnerVehicleMake.setAdapter(vehicleMakeAdapter);

                    spinnerVehicleMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position,
                                                   long id) {

                            VehicleMake selectedVehicleMake = vehicleMakes.get(position);
                            selectedMake = selectedVehicleMake.getName();
                            loadVehicleModels(selectedVehicleMake.getId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<VehicleMakeResponse> call, Throwable t) {
                // Handle failure scenario
            }
        });
    }

    // For the selected make, load vehicle models
    private void loadVehicleModels(int makeId) {
        VehicleService vehicleService = retrofit.create(VehicleService.class);

        Call<VehicleModelResponse> vehicleModelCall = vehicleService.getVehicleModels(makeId);
        vehicleModelCall.enqueue(new Callback<VehicleModelResponse>() {
            @Override
            public void onResponse(Call<VehicleModelResponse> call,
                                   Response<VehicleModelResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<VehicleModel> vehicleModels = response.body().getResults();
                    ArrayAdapter<VehicleModel> vehicleModelAdapter = new ArrayAdapter<>(
                            DashboardActivity.this,
                            android.R.layout.simple_spinner_item,
                            vehicleModels
                    );
                    spinnerVehicleModel.setAdapter(vehicleModelAdapter);

                    spinnerVehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position,
                                                   long id) {
                            VehicleModel selectedVehicleModel = vehicleModels.get(position);
                            selectedModel = selectedVehicleModel.getModel();

                            selectedModel = selectedVehicleModel.getModel();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<VehicleModelResponse> call, Throwable t) {
                // Handle failure scenario
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String imagePath = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    imagePath = getPathFromUri(imageUri);
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imagePath = saveBitmapToGallery(imageBitmap);
                }
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    imagePath = getPathFromUri(imageUri);
                }
            }

            if (imagePath != null) {
                Vehicle vehicle = vehicleListAdapter.getVehicleAtPosition(selectedPosition);
                vehicle.setImagePath(imagePath);
                vehicleViewModel.update(vehicle);
                vehicleListAdapter.notifyItemChanged(selectedPosition);
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
    }

    private String saveBitmapToGallery(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Car Image");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Car Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Captured car image");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        OutputStream outputStream;
        try {
            outputStream = getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getPathFromUri(uri);
    }
}
