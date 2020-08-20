package com.example.e_carorder;

import java.util.ArrayList;

public class ChargePointHelperClass {

    private String generalComments, price;
    private AddressInfoHelperClass addressInfoHelperClass;
    private ArrayList<ConnectionHelperClass> connections = new ArrayList<>();

    public ChargePointHelperClass() {
    }

    public ChargePointHelperClass(String generalComments, String price, AddressInfoHelperClass addressInfoHelperClass, ArrayList<ConnectionHelperClass> connections) {
        this.generalComments = generalComments;
        this.price = price;
        this.addressInfoHelperClass = addressInfoHelperClass;
        this.connections = connections;
    }

    public String getGeneralComments() {
        return generalComments;
    }

    public void setGeneralComments(String generalComments) {
        this.generalComments = generalComments;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public AddressInfoHelperClass getAddressInfoHelperClass() {
        return addressInfoHelperClass;
    }

    public void setAddressInfoHelperClass(AddressInfoHelperClass addressInfoHelperClass) {
        this.addressInfoHelperClass = addressInfoHelperClass;
    }

    public ArrayList<ConnectionHelperClass> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<ConnectionHelperClass> connections) {
        this.connections = connections;
    }
}
