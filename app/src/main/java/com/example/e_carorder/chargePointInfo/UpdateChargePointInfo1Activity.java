package com.example.e_carorder.chargePointInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.e_carorder.R;
import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ChargePointHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.ArrayList;

public class UpdateChargePointInfo1Activity extends AppCompatActivity {

    private EditText updateTitle, updateAddress, updatePostCode, updateStateOrProvince, updateTown;
    private Button updateChargePointNextBtn1, updateAdjustLocationBtn;
    private FloatingActionButton backCPUpdate1Btn;
    private final int PLACE_PICKER_REQUEST = 9999;

    private Double latitude, longitude;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private double userCurrentLatitude = 40.417996, userCurrentLongitude = -3.688878;

    private ArrayList<ConnectorHelperClass> connectors = new ArrayList<>();

    private String chargePointStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_charge_point_info1);

        final String chargePointId = getIntent().getStringExtra("chargePointId");

        updateTitle = findViewById(R.id.updateTitle);
        updateAddress = findViewById(R.id.updateAddress);
        updatePostCode = findViewById(R.id.updatePostCode);
        updateStateOrProvince = findViewById(R.id.updateStateOrProvince);
        updateTown = findViewById(R.id.updateTown);
        updateChargePointNextBtn1 = findViewById(R.id.updateChargePointNextBtn1);
        updateAdjustLocationBtn = findViewById(R.id.updateAdjustLocationBtn);
        backCPUpdate1Btn = findViewById(R.id.backCPUpdate1Btn);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

        Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        ChargePointHelperClass chargePointHelperClass = ds.getValue(ChargePointHelperClass.class);

                        chargePointStatus = ds.child("statusType").getValue().toString();

                        DataSnapshot dataSnapshotConnectors = ds.child("connectors");

                        for(DataSnapshot dsConnector : dataSnapshotConnectors.getChildren()){
                            ConnectorHelperClass addConnectorHelperClass = dsConnector.getValue(ConnectorHelperClass.class);

                            connectors.add(addConnectorHelperClass);
                        }

                        updateTitle.setText(chargePointHelperClass.getAddressInfo().getTitle());
                        updateAddress.setText(chargePointHelperClass.getAddressInfo().getAddress());
                        updatePostCode.setText(chargePointHelperClass.getAddressInfo().getPostCode());
                        updateStateOrProvince.setText(chargePointHelperClass.getAddressInfo().getStateOrProvince());
                        updateTown.setText(chargePointHelperClass.getAddressInfo().getTown());

                        latitude = chargePointHelperClass.getAddressInfo().getLatitude();
                        longitude = chargePointHelperClass.getAddressInfo().getLongitude();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        updateAdjustLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting user current location
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                    locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                userCurrentLatitude = location.getLatitude();
                                userCurrentLongitude = location.getLongitude();

                            }

                            activatePlacePicker();
                        }
                    });
                }else{

                    activatePlacePicker();
                }

            }
        });

        updateChargePointNextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title, address, postCode, stateOrProvince, town;

                if(updateTitle.getText().toString().isEmpty()){
                    updateTitle.setError("Compulsory field.");
                    return;

                } else{
                    title = updateTitle.getText().toString();
                }

                if(updateAddress.getText().toString().isEmpty()){
                    updateAddress.setError("Compulsory field.");
                    return;

                } else{
                    address = updateAddress.getText().toString();
                }

                if(updatePostCode.getText().toString().isEmpty()){
                    updatePostCode.setError("Compulsory field.");
                    return;

                } else{
                    postCode = updatePostCode.getText().toString();
                }

                if(updateStateOrProvince.getText().toString().isEmpty()){
                    updateStateOrProvince.setError("Compulsory field.");
                    return;

                } else{
                    stateOrProvince = updateStateOrProvince.getText().toString();
                }

                if(updateTown.getText().toString().isEmpty()){
                    updateTown.setError("Compulsory field.");
                    return;

                } else{
                    town = updateTown.getText().toString();
                }

                AddressInfoHelperClass addressInfoHelperClass = new AddressInfoHelperClass(title, address, stateOrProvince, town, postCode, latitude, longitude);

                Intent i = new Intent(v.getContext(), UpdateChargePointInfo2Activity.class);
                i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                i.putExtra("connectors", connectors);
                i.putExtra("chargePointStatus", chargePointStatus);
                i.putExtra("chargePointId", chargePointId);
                startActivity(i);
            }
        });

        backCPUpdate1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateChargePointInfo1Activity.super.onBackPressed();
            }
        });
    }

    private void activatePlacePicker(){
        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(userCurrentLatitude, userCurrentLongitude)  // Initial Latitude and Longitude the Map will load into
                .showLatLong(true)  // Show Coordinates in the Activity
                .setMapZoom(17.0f)  // Map Zoom Level. Default: 14.0
                .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
                //.setMarkerDrawable(R.drawable.marker) // Change the default Marker Image
                .setMarkerImageImageColor(R.color.colorPrimary)
                //.setFabColor(R.color.fabColor)
                .setPrimaryTextColor(R.color.colorPrimaryDark) // Change text color of Shortened Address
                .setSecondaryTextColor(R.color.colorPrimary) // Change text color of full Address
                //.setBottomViewColor(R.color.bottomViewColor) // Change Address View Background Color (Default: White)
                //.setMapRawResourceStyle(R.raw.map_style)  //Set Map Style (https://mapstyle.withgoogle.com/)
                .setMapType(MapType.SATELLITE)
                .setPlaceSearchBar(false, "AIzaSyCugS8Y3DarYsGZmAeQAeeujSTiL7LmGF8") //Activate GooglePlace Search Bar. Default is false/not activated. SearchBar is a chargeable feature by Google
                .onlyCoordinates(false)  //Get only Coordinates from Place Picker
                .hideLocationButton(false)   //Hide Location Button (Default: false)
                .disableMarkerAnimation(true)   //Disable Marker Animation (Default: false)
                .build(UpdateChargePointInfo1Activity.this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);

                updateAddress.setText(addressData.getAddressList().get(0).getAddressLine(0));
                updatePostCode.setText(addressData.getAddressList().get(0).getPostalCode());
                updateStateOrProvince.setText(addressData.getAddressList().get(0).getSubAdminArea());
                updateTown.setText(addressData.getAddressList().get(0).getLocality());
                latitude = addressData.getLatitude();
                longitude = addressData.getLongitude();

                updateTitle.setVisibility(View.VISIBLE);
                updateAddress.setVisibility(View.VISIBLE);
                updatePostCode.setVisibility(View.VISIBLE);
                updateStateOrProvince.setVisibility(View.VISIBLE);
                updateTown.setVisibility(View.VISIBLE);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}