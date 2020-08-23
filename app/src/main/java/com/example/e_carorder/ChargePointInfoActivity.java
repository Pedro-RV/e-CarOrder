package com.example.e_carorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChargePointInfoActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private TextView titleInfo, address, postCode, stateOrProvince, town, type1, type2, power1, power2, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_point_info);

        mDatabase = FirebaseDatabase.getInstance().getReference("ChargePoints");

        String id = getIntent().getStringExtra("id");


        titleInfo = findViewById(R.id.titleInfo);
        address = findViewById(R.id.address);
        postCode = findViewById(R.id.postCode);
        stateOrProvince = findViewById(R.id.stateOrProvince);
        town = findViewById(R.id.town);
        type1 = findViewById(R.id.type1);
        power1 = findViewById(R.id.power1);
        type2 = findViewById(R.id.type2);
        power2 = findViewById(R.id.power2);
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

                        type1.setText("Connector type: " + ds.child("connectors").child("0").child("connectorType").getValue().toString());
                        power1.setText("PowerKW: " + ds.child("connectors").child("0").child("powerKW").getValue().toString());

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