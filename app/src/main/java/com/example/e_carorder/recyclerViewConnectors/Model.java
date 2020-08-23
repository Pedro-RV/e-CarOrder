package com.example.e_carorder.recyclerViewConnectors;

public class Model {

    private String connectorType, connectorPowerKW;
    private int imageConnector;

    public Model(String connectorType, String connectorPowerKW, int imageConnector) {
        this.connectorType = connectorType;
        this.connectorPowerKW = connectorPowerKW;
        this.imageConnector = imageConnector;
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
}
