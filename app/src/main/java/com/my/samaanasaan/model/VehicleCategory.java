package com.my.samaanasaan.model;

public class VehicleCategory {

   private  String categoryId;
    private float Capacity;
    private  double PerKMCharge;
    private double BaseFair;
    private String CategoryName;
    private double PerMinutCharge;
    private double Volume;

    public VehicleCategory() {
    }

    public VehicleCategory(float capacity, double perKMCharge, double baseFair, String categoryName, double perMinutCharge, double volume) {
        Capacity = capacity;
        PerKMCharge = perKMCharge;
        BaseFair = baseFair;
        CategoryName = categoryName;
        PerMinutCharge = perMinutCharge;
        Volume = volume;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public float getCapacity() {
        return Capacity;
    }

    public void setCapacity(float capacity) {
        Capacity = capacity;
    }

    public double getPerKMCharge() {
        return PerKMCharge;
    }

    public void setPerKMCharge(double perKMCharge) {
        PerKMCharge = perKMCharge;
    }

    public double getBaseFair() {
        return BaseFair;
    }

    public void setBaseFair(double baseFair) {
        BaseFair = baseFair;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public double getPerMinutCharge() {
        return PerMinutCharge;
    }

    public void setPerMinutCharge(double perMinutCharge) {
        PerMinutCharge = perMinutCharge;
    }

    public double getVolume() {
        return Volume;
    }

    public void setVolume(double volume) {
        Volume = volume;
    }
}
