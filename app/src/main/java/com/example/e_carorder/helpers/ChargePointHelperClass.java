package com.example.e_carorder.helpers;

import java.util.ArrayList;

public class ChargePointHelperClass {

    private String id;
    private String statusType;
    private AddressInfoHelperClass addressInfo;
    private ArrayList<ConnectorHelperClass> connectors = new ArrayList<>();

    public ChargePointHelperClass() {
    }

    public ChargePointHelperClass(String id, String statusType, AddressInfoHelperClass addressInfo, ArrayList<ConnectorHelperClass> connectors) {
        this.id = id;
        this.statusType = statusType;
        this.addressInfo = addressInfo;
        this.connectors = connectors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public AddressInfoHelperClass getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfoHelperClass addressInfo) {
        this.addressInfo = addressInfo;
    }

    public ArrayList<ConnectorHelperClass> getConnectors() {
        return connectors;
    }

    public void setConnectors(ArrayList<ConnectorHelperClass> connectors) {
        this.connectors = connectors;
    }
}
