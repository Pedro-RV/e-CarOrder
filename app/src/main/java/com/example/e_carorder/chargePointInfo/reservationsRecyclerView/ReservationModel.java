package com.example.e_carorder.chargePointInfo.reservationsRecyclerView;

public class ReservationModel {

    private String reservationUserId, date;

    public ReservationModel(String reservationUserId, String date) {
        this.reservationUserId = reservationUserId;
        this.date = date;
    }

    public String getReservationUserId() {
        return reservationUserId;
    }

    public void setReservationUserId(String reservationUserId) {
        this.reservationUserId = reservationUserId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
