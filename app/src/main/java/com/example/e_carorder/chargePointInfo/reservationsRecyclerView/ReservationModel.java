package com.example.e_carorder.chargePointInfo.reservationsRecyclerView;

public class ReservationModel {

    private String reservationUserId;
    private long date;
    private String chargePointId, connectorId;

    public ReservationModel(String reservationUserId, long date, String chargePointId, String connectorId) {
        this.reservationUserId = reservationUserId;
        this.date = date;
        this.chargePointId = chargePointId;
        this.connectorId = connectorId;
    }

    public String getReservationUserId() {
        return reservationUserId;
    }

    public void setReservationUserId(String reservationUserId) {
        this.reservationUserId = reservationUserId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
}
