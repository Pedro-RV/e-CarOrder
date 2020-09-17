package com.example.e_carorder.helpers;

import java.io.Serializable;

public class ReservationUserHelperClass implements Serializable {

    private String reservationUserId;
    private long reservationDate;

    public ReservationUserHelperClass() {
    }

    public ReservationUserHelperClass(String reservationUserId, long reservationDate) {
        this.reservationUserId = reservationUserId;
        this.reservationDate = reservationDate;
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
}
