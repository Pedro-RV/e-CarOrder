package com.example.e_carorder.addChargePoint;

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
import android.widget.TextView;

import com.example.e_carorder.Navigation_temporal;
import com.example.e_carorder.R;
import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

public class AddChargePoint2Activity extends AppCompatActivity {

    private TextView tvAddressInfoCP;
    private EditText registerTitle, registerAddress, registerPostCode, registerStateOrProvince, registerTown;
    private Button registerChargePointNextBtn1, adjustLocationBtn;
    private final int PLACE_PICKER_REQUEST = 9999;

    private Double latitude, longitude;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private double userCurrentLatitude = 40.417996, userCurrentLongitude = -3.688878;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charge_point2);

        tvAddressInfoCP = findViewById(R.id.tvAddressInfoCP);
        registerTitle = findViewById(R.id.registerTitle);
        registerAddress = findViewById(R.id.registerAddress);
        registerPostCode = findViewById(R.id.registerPostCode);
        registerStateOrProvince = findViewById(R.id.registerStateOrProvince);
        registerTown = findViewById(R.id.registerTown);
        registerChargePointNextBtn1 = findViewById(R.id.registerChargePointNextBtn1);
        adjustLocationBtn = findViewById(R.id.adjustLocationBtn);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        adjustLocationBtn.setOnClickListener(new View.OnClickListener() {
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

        registerChargePointNextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title, address, postCode, stateOrProvince, town;

                if(registerTitle.getText().toString().isEmpty()){
                    registerTitle.setError("Compulsory field.");
                    return;

                } else{
                    title = registerTitle.getText().toString();
                }

                if(registerAddress.getText().toString().isEmpty()){
                    registerAddress.setError("Compulsory field.");
                    return;

                } else{
                    address = registerAddress.getText().toString();
                }

                if(registerPostCode.getText().toString().isEmpty()){
                    registerPostCode.setError("Compulsory field.");
                    return;

                } else{
                    postCode = registerPostCode.getText().toString();
                }

                if(registerStateOrProvince.getText().toString().isEmpty()){
                    registerStateOrProvince.setError("Compulsory field.");
                    return;

                } else{
                    stateOrProvince = registerStateOrProvince.getText().toString();
                }

                if(registerTown.getText().toString().isEmpty()){
                    registerTown.setError("Compulsory field.");
                    return;

                } else{
                    town = registerTown.getText().toString();
                }

                AddressInfoHelperClass addressInfoHelperClass = new AddressInfoHelperClass(title, address, stateOrProvince, town, postCode, latitude, longitude);

                Intent i = new Intent(v.getContext(), AddChargePoint3Activity.class);
                i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                startActivity(i);
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
                .disableMarkerAnimation(false)   //Disable Marker Animation (Default: false)
                .build(AddChargePoint2Activity.this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);

                registerAddress.setText(addressData.getAddressList().get(0).getAddressLine(0));
                registerPostCode.setText(addressData.getAddressList().get(0).getPostalCode());
                registerStateOrProvince.setText(addressData.getAddressList().get(0).getSubAdminArea());
                registerTown.setText(addressData.getAddressList().get(0).getLocality());
                latitude = addressData.getLatitude();
                longitude = addressData.getLongitude();

                registerTitle.setVisibility(View.VISIBLE);
                registerAddress.setVisibility(View.VISIBLE);
                registerPostCode.setVisibility(View.VISIBLE);
                registerStateOrProvince.setVisibility(View.VISIBLE);
                registerTown.setVisibility(View.VISIBLE);

                tvAddressInfoCP.setText("Address Info");

                adjustLocationBtn.setVisibility(View.INVISIBLE);
                registerChargePointNextBtn1.setVisibility(View.VISIBLE);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddChargePoint2Activity.this, Navigation_temporal.class);
        startActivity(i);
        finish();
    }
}