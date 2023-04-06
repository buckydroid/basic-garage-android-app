package com.sahil.garage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class VehicleModelAdapter extends ArrayAdapter<VehicleModel> {

    public VehicleModelAdapter(@NonNull Context context, @NonNull List<VehicleModel> VehicleModels) {
        super(context, 0, VehicleModels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    android.R.layout.simple_spinner_item, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(android.R.id.text1);

        VehicleModel currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getModel());
        }

        return convertView;
    }
}
