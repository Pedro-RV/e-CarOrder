package com.example.e_carorder.chargePointInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.e_carorder.R;
import com.example.e_carorder.chargePointInfo.reservationsRecyclerView.ReservationAdapter;
import com.example.e_carorder.chargePointInfo.reservationsRecyclerView.ReservationModel;
import com.example.e_carorder.chats.AllUsersChatsActivity;
import com.example.e_carorder.chats.usersRecyclerView.UserAdapter;
import com.example.e_carorder.chats.usersRecyclerView.UserModel;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.ReservationUserHelperClass;
import com.example.e_carorder.signInSignUp.SignInSignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ConnectorReservationActivity extends AppCompatActivity {

    private Button reservationBtn;
    private FloatingActionButton backReservationsBtn;
    private DatabaseReference databaseReference;

    private ArrayList<ConnectorHelperClass> connectors = new ArrayList<>();

    private ReservationAdapter reservationAdapter;
    private RecyclerView recyclerViewAllReservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connector_reservation);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

        final String chargePointId = getIntent().getStringExtra("chargePointId");
        final String connectorId = getIntent().getStringExtra("connectorId");

        reservationBtn = findViewById(R.id.reservationBtn);
        recyclerViewAllReservations = findViewById(R.id.rvAllReservations);
        backReservationsBtn = findViewById(R.id.backReservationsBtn);

        recyclerViewAllReservations.setLayoutManager(new LinearLayoutManager(this));

        Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        ConnectorHelperClass connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                        final ArrayList<ReservationUserHelperClass> reservations = connector.getReservations();

                        if(reservations != null){
                            ArrayList<ReservationModel> mReservations = new ArrayList<>();

                            for(int i = 0; i < reservations.size(); i++){

                                 String reservationUserId = reservations.get(i).getReservationUserId();
                                 long reservationDate = reservations.get(i).getReservationDate();

                                 ReservationModel addReservationModel = new ReservationModel(
                                         reservationUserId,
                                         reservationDate,
                                         chargePointId,
                                         connectorId);

                                 mReservations.add(addReservationModel);

                            }

                            reservationAdapter = new ReservationAdapter(ConnectorReservationActivity.this, mReservations);
                            recyclerViewAllReservations.setAdapter(reservationAdapter);

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Calendar calendar;
                DatePickerDialog datePickerDialog;

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(ConnectorReservationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        new TimePickerDialog(ConnectorReservationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            }
                        }, hour, minute, true);


                        Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

                        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        ConnectorHelperClass connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                                        ArrayList<ReservationUserHelperClass> reservations = connector.getReservations();

                                        if (reservations == null) {
                                            reservations = new ArrayList<>();
                                        }

                                        Calendar date = Calendar.getInstance();
                                        date.set(year, month, dayOfMonth, 0, 0);

                                        ReservationUserHelperClass newReservation = new ReservationUserHelperClass(
                                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                                date.getTimeInMillis());

                                        reservations.add(newReservation);

                                        Collections.sort(reservations, new Comparator<ReservationUserHelperClass>(){
                                            public int compare(ReservationUserHelperClass obj1, ReservationUserHelperClass obj2) {

                                                return Long.valueOf(obj1.getReservationDate()).compareTo(Long.valueOf(obj2.getReservationDate()));

                                            }
                                        });

                                        connector.setReservations(reservations);

                                        databaseReference.child(chargePointId).child("connectors").child(connectorId).setValue(connector);

                                        ArrayList<ReservationModel> mReservations = new ArrayList<>();

                                        for(int i = 0; i < reservations.size(); i++){

                                            String reservationUserId = reservations.get(i).getReservationUserId();
                                            long reservationDate = reservations.get(i).getReservationDate();

                                            ReservationModel addReservationModel = new ReservationModel(
                                                    reservationUserId,
                                                    reservationDate,
                                                    chargePointId,
                                                    connectorId);

                                            mReservations.add(addReservationModel);

                                        }

                                        reservationAdapter = new ReservationAdapter(ConnectorReservationActivity.this, mReservations);
                                        recyclerViewAllReservations.setAdapter(reservationAdapter);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }, year, month, day);

                datePickerDialog.show();

            }
        });

        backReservationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectorReservationActivity.super.onBackPressed();
            }
        });

    }

}