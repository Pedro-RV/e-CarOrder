package com.example.e_carorder.chargePointInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.e_carorder.R;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorModel;
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
import java.util.HashMap;
import java.util.Map;

public class ConnectorReservationActivity extends AppCompatActivity {

    private Button reservationBtn;
    private DatabaseReference databaseReference;

    private ArrayList<ConnectorHelperClass> connectors = new ArrayList<>();

    private ReservationAdapter reservationAdapter;
    private RecyclerView recyclerViewAllReservations;
    private ArrayList<ReservationModel> mReservations = new ArrayList<>();

    private ConnectorHelperClass connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connector_reservation);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

        final String chargePointId = getIntent().getStringExtra("chargePointId");
        final String connectorId = getIntent().getStringExtra("connectorId");

        reservationBtn = findViewById(R.id.reservationBtn);
        recyclerViewAllReservations = findViewById(R.id.rvAllReservations);

        recyclerViewAllReservations.setLayoutManager(new LinearLayoutManager(this));

        Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                        final ArrayList<ReservationUserHelperClass> reservations = connector.getReservations();

                        if(reservations != null){
                            for(int i = 0; i < reservations.size(); i++){

                                 String reservationUserId = reservations.get(i).getReservationUserId();
                                 String reservationDate = reservations.get(i).getReservationDate();

                                 ReservationModel addReservationModel = new ReservationModel(
                                        reservationUserId,
                                        reservationDate);

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

                final EditText introducedDate = new EditText(v.getContext());
                introducedDate.setInputType(InputType.TYPE_CLASS_DATETIME);
                introducedDate.setHint("Date");
                introducedDate.setBackgroundColor(Color.rgb(247,242,255));
                introducedDate.setTextSize(18);
                FrameLayout container = new FrameLayout(ConnectorReservationActivity.this);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.height = 112;
                introducedDate.setLayoutParams(params);
                container.addView(introducedDate);

                final AlertDialog addReservationDialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Reservation date.")
                        .setMessage("Enter the date you want to reserve.")
                        .setView(container)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Accept", null)
                        .create();

                addReservationDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(final DialogInterface dialog) {

                        Button positive = addReservationDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negative = addReservationDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                        positive.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                final String date = introducedDate.getText().toString();

                                if(date.isEmpty()){
                                    introducedDate.setError("You have to write a date.");

                                } else {

                                    ArrayList<ReservationUserHelperClass> reservations = connector.getReservations();

                                    if(reservations == null){
                                        reservations = new ArrayList<>();
                                    }

                                    ReservationUserHelperClass newReservation = new ReservationUserHelperClass(
                                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                            date);

                                    reservations.add(newReservation);

                                    connector.setReservations(reservations);

                                    databaseReference.child(chargePointId).child("connectors").child(connectorId).setValue(connector);

                                    ReservationModel addReservationModel = new ReservationModel(
                                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                            date
                                    );

                                    mReservations.add(addReservationModel);

                                    reservationAdapter = new ReservationAdapter(ConnectorReservationActivity.this, mReservations);
                                    recyclerViewAllReservations.setAdapter(reservationAdapter);

                                    dialog.dismiss();

                                }

                            }
                        });

                        negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                });

                addReservationDialog.show();

            }
        });



    }
}