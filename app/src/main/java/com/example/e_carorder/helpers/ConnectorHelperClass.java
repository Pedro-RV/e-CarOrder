package com.example.e_carorder.helpers;

import java.io.Serializable;

public class ConnectorHelperClass implements Serializable {

    private String checkInUserId, connectorType, powerKW;
    private Boolean alert;
    private long alertDate;

    public ConnectorHelperClass(){
    }

    public ConnectorHelperClass(String connectorType, String powerKW) {
        this.connectorType = connectorType;
        this.powerKW = powerKW;

        this.checkInUserId = "";
        this.alert = false;
        this.alertDate = -1;
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

    public Boolean getAlert() {
        return alert;
    }

    public void setAlert(Boolean alert) {
        this.alert = alert;
    }

    public long getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(long alertDate) {
        this.alertDate = alertDate;
    }
}
