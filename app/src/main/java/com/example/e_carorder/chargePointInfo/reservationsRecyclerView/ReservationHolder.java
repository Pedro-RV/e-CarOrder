package com.example.e_carorder.chargePointInfo.reservationsRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReservationHolder extends RecyclerView.ViewHolder {

    TextView usernameReservation, dateReservation;
    ImageView imageReservation;
    FloatingActionButton deleteReservationBtn;

    public ReservationHolder(@NonNull View itemView) {
        super(itemView);

        this.usernameReservation = itemView.findViewById(R.id.usernameReservation);
        this.dateReservation = itemView.findViewById(R.id.dateReservation);
        this.imageReservation = itemView.findViewById(R.id.imageReservation);
        this.deleteReservationBtn = itemView.findViewById(R.id.deleteReservationBtn);

    }
}
