package com.example.e_carorder.helpers;

import java.io.Serializable;
import java.util.ArrayList;

public class ConnectorHelperClass implements Serializable {

    private String checkInUserId, connectorType, powerKW;
    private Boolean alert;
    private long alertDate, checkOutTime;

    private ArrayList<ReservationUserHelperClass> reservations;

    private ArrayList<QueueItemHelperClass> queue;

    public ConnectorHelperClass(){
    }

    public ConnectorHelperClass(String checkInUserId, String connectorType, String powerKW, Boolean alert, long alertDate, long checkOutTime,
                                ArrayList<ReservationUserHelperClass> reservations, ArrayList<QueueItemHelperClass> queue) {
        this.checkInUserId = checkInUserId;
        this.connectorType = connectorType;
        this.powerKW = powerKW;
        this.alert = alert;
        this.alertDate = alertDate;
        this.checkOutTime = checkOutTime;
        this.reservations = reservations;
        this.queue = queue;
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

    public ArrayList<ReservationUserHelperClass> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<ReservationUserHelperClass> reservations) {
        this.reservations = reservations;
    }

    public ArrayList<QueueItemHelperClass> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<QueueItemHelperClass> queue) {
        this.queue = queue;
    }

    public long getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(long checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
}
