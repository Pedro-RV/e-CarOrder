package com.example.e_carorder.helpers;

import java.io.Serializable;

public class ConnectorHelperClass implements Serializable {

    private String connectorType, powerKW;

    public ConnectorHelperClass(){
    }

    public ConnectorHelperClass(String connectorType, String powerKW) {
        this.connectorType = connectorType;
        this.powerKW = powerKW;
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
