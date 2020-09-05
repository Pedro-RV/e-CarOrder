package com.example.e_carorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorAdapter;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorModel;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChargePointInfoActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private TextView titleInfo, address, postCode, stateOrProvince, town, status;

    private ArrayList<ConnectorModel> connectors = new ArrayList<>();

    private RecyclerView recyclerView;
    private ConnectorAdapter connectorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_point_info);

        mDatabase = FirebaseDatabase.getInstance().getReference("ChargePoints");

        String id = getIntent().getStringExtra("id");

        recyclerView = findViewById(R.id.recyclerViewConnectorsCPInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        titleInfo = findViewById(R.id.titleInfo);
        address = findViewById(R.id.address);
        postCode = findViewById(R.id.postCode);
        stateOrProvince = findViewById(R.id.stateOrProvince);
        town = findViewById(R.id.town);
        status = findViewById(R.id.status);

        Query checkChargePoint = mDatabase.orderByChild("id").equalTo(id);

        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        titleInfo.setText("Title: " + ds.child("addressInfo").child("title").getValue().toString());
                        address.setText("Address: " + ds.child("addressInfo").child("address").getValue().toString());
                        postCode.setText("PostCode: " + ds.child("addressInfo").child("postCode").getValue().toString());
                        stateOrProvince.setText("StateOrProvince: " + ds.child("addressInfo").child("stateOrProvince").getValue().toString());
                        town.setText("Town: " + ds.child("addressInfo").child("town").getValue().toString());

                        DataSnapshot dataSnapshotConnectors = ds.child("connectors");

                        for(DataSnapshot dsConnector : dataSnapshotConnectors.getChildren()){
                            ConnectorModel m = new ConnectorModel(
                                    dsConnector.child("connectorType").getValue().toString(),
                                    dsConnector.child("powerKW").getValue().toString(),
                                    R.drawable.electrical,
                                    true);

                            connectors.add(m);
                        }

                        connectorAdapter = new ConnectorAdapter(ChargePointInfoActivity.this, connectors);
                        recyclerView.setAdapter(connectorAdapter);

                        status.setText("Status: " + ds.child("statusType").getValue().toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}