package com.example.e_carorder.chargePointInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_carorder.R;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorAdapter;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        final String chargePointId = getIntent().getStringExtra("chargePointId").substring(4);;

        recyclerView = findViewById(R.id.recyclerViewConnectorsCPInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        titleInfo = findViewById(R.id.titleInfo);
        address = findViewById(R.id.address);
        postCode = findViewById(R.id.postCode);
        stateOrProvince = findViewById(R.id.stateOrProvince);
        town = findViewById(R.id.town);
        status = findViewById(R.id.status);

        Query checkChargePoint = mDatabase.orderByChild("id").equalTo(chargePointId);

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

                        Double latitude = (Double) ds.child("addressInfo").child("latitude").getValue();
                        Double longitude = (Double) ds.child("addressInfo").child("longitude").getValue();

                        DataSnapshot dataSnapshotConnectors = ds.child("connectors");

                        for(DataSnapshot dsConnector : dataSnapshotConnectors.getChildren()){
                            ConnectorModel m = new ConnectorModel(
                                    chargePointId,
                                    dsConnector.getKey(),
                                    dsConnector.child("checkInUserId").getValue().toString(),
                                    dsConnector.child("connectorType").getValue().toString(),
                                    dsConnector.child("powerKW").getValue().toString(),
                                    R.drawable.electrical,
                                    latitude,
                                    longitude,
                                    (Boolean) dsConnector.child("alert").getValue(),
                                    (long) dsConnector.child("alertDate").getValue());

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