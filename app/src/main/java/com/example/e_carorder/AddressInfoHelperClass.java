package com.example.e_carorder;

public class AddressInfoHelperClass {

    private String address, postCode, stateOrProvince, town;

    public AddressInfoHelperClass(){
    }

    public AddressInfoHelperClass(String address, String postCode, String stateOrProvince, String town) {
        this.address = address;
        this.postCode = postCode;
        this.stateOrProvince = stateOrProvince;
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
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
}
