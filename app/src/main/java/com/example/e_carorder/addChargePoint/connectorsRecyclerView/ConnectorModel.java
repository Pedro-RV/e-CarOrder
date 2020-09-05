package com.example.e_carorder.addChargePoint.connectorsRecyclerView;

public class ConnectorModel {

    private String connectorType, connectorPowerKW;
    private int imageConnector;
    private Boolean activateBtn;

    public ConnectorModel(String connectorType, String connectorPowerKW, int imageConnector, Boolean activateBtn) {
        this.connectorType = connectorType;
        this.connectorPowerKW = connectorPowerKW;
        this.imageConnector = imageConnector;
        this.activateBtn = activateBtn;
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

    public Boolean getActivateBtn() {
        return activateBtn;
    }

    public void setActivateBtn(Boolean activateBtn) {
        this.activateBtn = activateBtn;
    }
}
