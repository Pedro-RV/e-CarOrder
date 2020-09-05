package com.example.e_carorder.helpers;

import java.io.Serializable;

public class ConnectorHelperClass implements Serializable {

    private String checkInUserId, connectorType, powerKW;

    public ConnectorHelperClass(){
    }

    public ConnectorHelperClass(String checkInUserId, String connectorType, String powerKW) {
        this.checkInUserId = checkInUserId;
        this.connectorType = connectorType;
        this.powerKW = powerKW;
    }

    public String getCheckInUserId() {
        return checkInUserId;
    }

    public void setCheckInUserId(String checkInUserId) {
        this.checkInUserId = checkInUserId;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public String getPowerKW() {
        return powerKW;
    }

    public void setPowerKW(String powerKW) {
        this.powerKW = powerKW;
    }
}
