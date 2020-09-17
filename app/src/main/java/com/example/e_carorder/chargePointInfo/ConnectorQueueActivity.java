package com.example.e_carorder.chargePointInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_carorder.R;
import com.example.e_carorder.chargePointInfo.queueItemsRecyclerView.QueueItemAdapter;
import com.example.e_carorder.chargePointInfo.queueItemsRecyclerView.QueueItemModel;
import com.example.e_carorder.chargePointInfo.reservationsRecyclerView.ReservationAdapter;
import com.example.e_carorder.chargePointInfo.reservationsRecyclerView.ReservationModel;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.QueueItemHelperClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConnectorQueueActivity extends AppCompatActivity {

    private Button joinQueueBtn, leaveQueueBtn;
    private FloatingActionButton backQueueListBtn;
    private DatabaseReference databaseReference;

    private QueueItemAdapter queueItemAdapter;
    private RecyclerView recyclerViewQueue;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connector_queue);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

        final String chargePointId = getIntent().getStringExtra("chargePointId");
        final String connectorId = getIntent().getStringExtra("connectorId");

        joinQueueBtn = findViewById(R.id.joinQueueBtn);
        leaveQueueBtn = findViewById(R.id.leaveQueueBtn);
        recyclerViewQueue = findViewById(R.id.rvQueue);
        backQueueListBtn = findViewById(R.id.backQueueListBtn);

        recyclerViewQueue.setLayoutManager(new LinearLayoutManager(this));

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        ConnectorHelperClass connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                        ArrayList<QueueItemHelperClass> queueItems = connector.getQueue();

                        if(queueItems != null){
                            ArrayList<QueueItemModel> mQueueItems = new ArrayList<>();

                            for(int i = 0; i < queueItems.size(); i++){

                                String queueItemUserId = queueItems.get(i).getQueueUserId();

                                if(queueItemUserId.equals(currentUserId)){
                                    leaveQueueBtn.setVisibility(View.VISIBLE);
                                }

                                QueueItemModel addQueueItemModel = new QueueItemModel(queueItemUserId);

                                mQueueItems.add(addQueueItemModel);

                            }

                            queueItemAdapter = new QueueItemAdapter(ConnectorQueueActivity.this, mQueueItems);
                            recyclerViewQueue.setAdapter(queueItemAdapter);

                        }

                        if(leaveQueueBtn.getVisibility() == View.GONE){
                            joinQueueBtn.setVisibility(View.VISIBLE);
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

                Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

                checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                ConnectorHelperClass connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                                ArrayList<QueueItemHelperClass> queueItems = connector.getQueue();

                                if(queueItems == null) {
                                    queueItems = new ArrayList<>();
                                }

                                QueueItemHelperClass newItemQueue = new QueueItemHelperClass(currentUserId);

                                queueItems.add(newItemQueue);

                                connector.setQueue(queueItems);

                                databaseReference.child(chargePointId).child("connectors").child(connectorId).setValue(connector);

                                ArrayList<QueueItemModel> mQueueItems = new ArrayList<>();

                                for(int i = 0; i < queueItems.size(); i++){

                                    String queueItemUserId = queueItems.get(i).getQueueUserId();

                                    QueueItemModel addQueueItemModel = new QueueItemModel(queueItemUserId);

                                    mQueueItems.add(addQueueItemModel);

                                }

                                joinQueueBtn.setVisibility(View.GONE);
                                leaveQueueBtn.setVisibility(View.VISIBLE);

                                queueItemAdapter = new QueueItemAdapter(ConnectorQueueActivity.this, mQueueItems);
                                recyclerViewQueue.setAdapter(queueItemAdapter);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        leaveQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

                checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                ConnectorHelperClass connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                                ArrayList<QueueItemHelperClass> queueItems = connector.getQueue();

                                boolean stop = false;

                                for(int i = 0; i < queueItems.size() && !stop; i++){
                                    if(queueItems.get(i).getQueueUserId().equals(currentUserId)){
                                        queueItems.remove(queueItems.get(i));
                                        stop = true;
                                    }
                                }

                                connector.setQueue(queueItems);

                                databaseReference.child(chargePointId).child("connectors").child(connectorId).setValue(connector);

                                ArrayList<QueueItemModel> mQueueItems = new ArrayList<>();

                                for(int i = 0; i < queueItems.size(); i++){

                                    String queueItemUserId = queueItems.get(i).getQueueUserId();

                                    QueueItemModel addQueueItemModel = new QueueItemModel(queueItemUserId);

                                    mQueueItems.add(addQueueItemModel);

                                }

                                joinQueueBtn.setVisibility(View.VISIBLE);
                                leaveQueueBtn.setVisibility(View.GONE);

                                queueItemAdapter = new QueueItemAdapter(ConnectorQueueActivity.this, mQueueItems);
                                recyclerViewQueue.setAdapter(queueItemAdapter);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        backQueueListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectorQueueActivity.super.onBackPressed();
            }
        });

    }
}