package com.example.e_carorder.chargePointInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.example.e_carorder.chargePointInfo.reservationsRecyclerView.ReservationAdapter;
import com.example.e_carorder.chargePointInfo.reservationsRecyclerView.ReservationModel;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.QueueHelperClass;
import com.example.e_carorder.helpers.ReservationUserHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConnectorQueueActivity extends AppCompatActivity {

    private Button joinQueueBtn;
    private DatabaseReference databaseReference;

    private ReservationAdapter reservationAdapter;
    private RecyclerView recyclerViewQueue;
    private ArrayList<ReservationModel> mReservations = new ArrayList<>();

    private ConnectorHelperClass connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connector_queue);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

        final String chargePointId = getIntent().getStringExtra("chargePointId");
        final String connectorId = getIntent().getStringExtra("connectorId");

        joinQueueBtn = findViewById(R.id.joinQueueBtn);
        recyclerViewQueue = findViewById(R.id.rvQueue);

        recyclerViewQueue.setLayoutManager(new LinearLayoutManager(this));

        Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                        final ArrayList<QueueHelperClass> queue = connector.getQueue();

                        if(queue != null){
                            for(int i = 0; i < queue.size(); i++){

                                String queueUserId = queue.get(i).getQueueUserId();
                                String queueUserPosition = Integer.toString(queue.size());

                                ReservationModel addReservationModel = new ReservationModel(
                                        queueUserId,
                                        queueUserPosition);

                                mReservations.add(addReservationModel);

                            }

                            reservationAdapter = new ReservationAdapter(ConnectorQueueActivity.this, mReservations);
                            recyclerViewQueue.setAdapter(reservationAdapter);

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        joinQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                ArrayList<QueueHelperClass> queue = connector.getQueue();

                if(queue == null){
                    queue = new ArrayList<>();

                } else{
                    for(int i = 0; i < queue.size(); i++){
                        if(queue.get(i).getQueueUserId().equals(currentUserId)){
                            Toast.makeText(ConnectorQueueActivity.this, "Error! You are already in queue.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                QueueHelperClass newQueue = new QueueHelperClass(FirebaseAuth.getInstance().getCurrentUser().getUid());

                queue.add(newQueue);

                connector.setQueue(queue);

                databaseReference.child(chargePointId).child("connectors").child(connectorId).setValue(connector);

                String queueUserPosition = Integer.toString(queue.size());

                ReservationModel addReservationModel = new ReservationModel(
                        currentUserId,
                        queueUserPosition
                );

                mReservations.add(addReservationModel);

                reservationAdapter = new ReservationAdapter(ConnectorQueueActivity.this, mReservations);
                recyclerViewQueue.setAdapter(reservationAdapter);

            }
        });

    }
}