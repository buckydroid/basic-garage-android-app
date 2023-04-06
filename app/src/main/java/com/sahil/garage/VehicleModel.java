package com.sahil.garage;

public class VehicleModel {
    private int Model_ID;
    private String Model_Name;

    public int getId() {
        return Model_ID;
    }

    public void setId(int id) {
        Model_ID = id;
    }

    public String getModel() {
        return Model_Name;
    }

    public void setModel(String model) {
        Model_Name = model;
    }

    public String toString() {
        return Model_Name;
    }
}
