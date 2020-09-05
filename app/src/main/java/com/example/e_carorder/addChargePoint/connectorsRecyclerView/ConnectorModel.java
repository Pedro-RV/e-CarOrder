package com.example.e_carorder.addChargePoint.connectorsRecyclerView;

public class ConnectorModel {

    private String chargePointId, connectorId, checkInUserId, connectorType, connectorPowerKW;
    private int imageConnector;
    private double chargePointLatitude, chargePointLongitude;

    public ConnectorModel(String chargePointId, String connectorId, String checkInUserId, String connectorType, String connectorPowerKW, int imageConnector, double chargePointLatitude, double chargePointLongitude) {
        this.chargePointId = chargePointId;
        this.connectorId = connectorId;
        this.checkInUserId = checkInUserId;
        this.connectorType = connectorType;
        this.connectorPowerKW = connectorPowerKW;
        this.imageConnector = imageConnector;
        this.chargePointLatitude = chargePointLatitude;
        this.chargePointLongitude = chargePointLongitude;
    }

    public String getChargePointId() {
        return chargePointId;
    }

    public void setChargePointId(String chargePointId) {
        this.chargePointId = chargePointId;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
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

    public String getConnectorPowerKW() {
        return connectorPowerKW;
    }

    public void setConnectorPowerKW(String connectorPowerKW) {
        this.connectorPowerKW = connectorPowerKW;
    }

    public int getImageConnector() {
        return imageConnector;
    }

    public void setImageConnector(int imageConnector) {
        this.imageConnector = imageConnector;
    }

    public double getChargePointLatitude() {
        return chargePointLatitude;
    }

    public void setChargePointLatitude(double chargePointLatitude) {
        this.chargePointLatitude = chargePointLatitude;
    }

    public double getChargePointLongitude() {
        return chargePointLongitude;
    }

    public void setChargePointLongitude(double chargePointLongitude) {
        this.chargePointLongitude = chargePointLongitude;
    }
}
