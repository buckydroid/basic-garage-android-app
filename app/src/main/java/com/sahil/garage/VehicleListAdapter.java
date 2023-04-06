package com.sahil.garage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.VehicleHolder> {
    private List<Vehicle> vehicles = new ArrayList<>();
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onAddImageClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public VehicleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_item, parent, false);
        return new VehicleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleHolder holder, int position) {
        Vehicle currentVehicle = vehicles.get(position);
        holder.textViewMake.setText(currentVehicle.getMake());
        holder.textViewModel.setText(currentVehicle.getModel());
        // load image if necessary

        if (currentVehicle.getImagePath() != null) {
            File imageFile = new File(currentVehicle.getImagePath());
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                holder.imageViewCar.setImageBitmap(bitmap);
            } else {
                // Set a default or placeholder image if the file doesn't exist.
                holder.imageViewCar.setImageResource(R.drawable.car);
            }
        } else {
            // Set a default or placeholder image if the imagePath is null.
            holder.imageViewCar.setImageResource(R.drawable.car);
        }
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }
    public Vehicle getVehicleAtPosition(int position) {
        return vehicles.get(position);
    }
    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

    class VehicleHolder extends RecyclerView.ViewHolder {
        private TextView textViewMake;
        private TextView textViewModel;
        private ImageView imageViewCar;
        private Button buttonDeleteCar;
        private Button buttonAddImage;

        public VehicleHolder(View itemView) {
            super(itemView);
            textViewMake = itemView.findViewById(R.id.text_view_make);
            textViewModel = itemView.findViewById(R.id.text_view_model);
            imageViewCar = itemView.findViewById(R.id.image_view_car);
            buttonDeleteCar = itemView.findViewById(R.id.button_delete_car);
            buttonAddImage = itemView.findViewById(R.id.button_add_image);

            buttonDeleteCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });

            buttonAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onAddImageClick(position);
                        }
                    }
                }
            });
        }

    }
}