package com.example.e_carorder.helpers;

import java.io.Serializable;

public class ReservationUserHelperClass implements Serializable {

    private String reservationUserId;
    private long reservationDate, reservationTimeFrom, reservationTimeTo;

    public ReservationUserHelperClass() {
    }

    public ReservationUserHelperClass(String reservationUserId, long reservationDate, long reservationTimeFrom, long reservationTimeTo) {
        this.reservationUserId = reservationUserId;
        this.reservationDate = reservationDate;
        this.reservationTimeFrom = reservationTimeFrom;
        this.reservationTimeTo = reservationTimeTo;
    }

    public String getReservationUserId() {
        return reservationUserId;
    }

    public void setReservationUserId(String reservationUserId) {
        this.reservationUserId = reservationUserId;
    }

    public long getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(long reservationDate) {
        this.reservationDate = reservationDate;
    }

    public long getReservationTimeFrom() {
        return reservationTimeFrom;
    }

    public void setReservationTimeFrom(long reservationTimeFrom) {
        this.reservationTimeFrom = reservationTimeFrom;
    }

    public long getReservationTimeTo() {
        return reservationTimeTo;
    }

    public void setReservationTimeTo(long reservationTimeTo) {
        this.reservationTimeTo = reservationTimeTo;
    }
}
