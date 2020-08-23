package com.example.e_carorder.helpers;

import java.io.Serializable;

public class AddressInfoHelperClass implements Serializable {

    private String title, address, stateOrProvince, town, postCode;
    private double latitude, longitude;

    public AddressInfoHelperClass(){
    }

    public AddressInfoHelperClass(String title, String address, String stateOrProvince, String town, String postCode, double latitude, double longitude) {
        this.title = title;
        this.address = address;
        this.stateOrProvince = stateOrProvince;
        this.town = town;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        address = address;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
