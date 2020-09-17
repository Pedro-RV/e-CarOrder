package com.example.e_carorder.chargePointInfo.reservationsRecyclerView;

public class ReservationModel {

    private String reservationUserId;
    private long date, timeFrom, timeTo;
    private String chargePointId, connectorId;

    public ReservationModel(String reservationUserId, long date, long timeFrom, long timeTo, String chargePointId, String connectorId) {
        this.reservationUserId = reservationUserId;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
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

    public long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(long timeTo) {
        this.timeTo = timeTo;
    }
}
