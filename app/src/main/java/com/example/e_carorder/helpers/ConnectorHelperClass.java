package com.example.e_carorder.helpers;

import java.io.Serializable;
import java.util.ArrayList;

public class ConnectorHelperClass implements Serializable {

    private String checkInUserId, connectorType, powerKW;
    private Boolean alert;
    private long alertDate;

    private ArrayList<ReservationUserHelperClass> reservations;

    private ArrayList<QueueHelperClass> queue;

    public ConnectorHelperClass(){
    }

    public ConnectorHelperClass(String checkInUserId, String connectorType, String powerKW, Boolean alert, long alertDate, ArrayList<ReservationUserHelperClass> reservations, ArrayList<QueueHelperClass> queue) {
        this.checkInUserId = checkInUserId;
        this.connectorType = connectorType;
        this.powerKW = powerKW;
        this.alert = alert;
        this.alertDate = alertDate;
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

    public ArrayList<QueueHelperClass> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<QueueHelperClass> queue) {
        this.queue = queue;
    }
}
