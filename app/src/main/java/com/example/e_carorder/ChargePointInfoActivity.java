package com.example.e_carorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Display;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChargePointInfoActivity extends AppCompatActivity {

    private TextView chargePointTittle;

    private DatabaseReference mDatabase;

    private TextView address, postCode, stateOrProvince, town, power1, power2, generalComments, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_point_info);

        mDatabase = FirebaseDatabase.getInstance().getReference("ChargePoints");

        chargePointTittle = findViewById(R.id.chargePointInfoTittle);

        String title = getIntent().getStringExtra("title");
        chargePointTittle.setText(title);


        address = findViewById(R.id.address);
        postCode = findViewById(R.id.postCode);
        stateOrProvince = findViewById(R.id.stateOrProvince);
        town = findViewById(R.id.town);
        power1 = findViewById(R.id.power1);
        power2 = findViewById(R.id.power2);
        generalComments = findViewById(R.id.generalComments);
        price = findViewById(R.id.price);


        String titleLookFor = chargePointTittle.getText().toString();

        Query checkChargePoint = mDatabase.orderByChild("AddressInfo/Title").equalTo(titleLookFor);

        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        address.setText("Address: " + ds.child("AddressInfo").child("AddressLine1").getValue().toString());
                        postCode.setText("PostCode: " + ds.child("AddressInfo").child("Postcode").getValue().toString());
                        stateOrProvince.setText("StateOrProvince: " + ds.child("AddressInfo").child("StateOrProvince").getValue().toString());
                        town.setText("Town: " + ds.child("AddressInfo").child("Town").getValue().toString());

                        power1.setText("PowerKW: " + ds.child("Connections").child("0").child("PowerKW").getValue().toString());

                        generalComments.setText("GeneralComments: " + ds.child("GeneralComments").getValue().toString());
                        price.setText("Price: " + ds.child("Price").getValue().toString());

                        //Toast.makeText(ChargePointInfoActivity.this, "Ha entrado", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}