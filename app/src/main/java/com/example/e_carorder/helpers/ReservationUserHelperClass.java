package com.example.e_carorder.helpers;

public class ReservationUserHelperClass {

    private String reservationUserId;
    private String reservationDate;

    public ReservationUserHelperClass() {
    }

    public ReservationUserHelperClass(String reservationUserId, String reservationDate) {
        this.reservationUserId = reservationUserId;
        this.reservationDate = reservationDate;
    }

    public String getReservationUserId() {
        return reservationUserId;
    }

    public void setReservationUserId(String reservationUserId) {
        this.reservationUserId = reservationUserId;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }
}
