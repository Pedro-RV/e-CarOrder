package com.example.e_carorder.helpers;

public class ReservationUserHelperClass {

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
