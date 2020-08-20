package com.example.e_carorder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddChargePointFragment extends Fragment {

    private EditText registerAddress, registerPostCode, registerStateOrProvince, registerTown, registerGeneralComments, registerPrice, registerPowerKW;
    private Button registerChargePointBtn;
    private ProgressBar progressBar;

    private DatabaseReference mDatabase;


    Button btPicker;
    int PLACE_PICKER_REQUEST = 18001;


    public AddChargePointFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_charge_point, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerAddress = view.findViewById(R.id.registerAddress);
        registerPostCode = view.findViewById(R.id.registerPostCode);
        registerStateOrProvince = view.findViewById(R.id.registerStateOrProvince);
        registerTown = view.findViewById(R.id.registerTown);
        registerGeneralComments = view.findViewById(R.id.registerGeneralComments);
        registerPrice = view.findViewById(R.id.registerPrice);
        registerPowerKW = view.findViewById(R.id.registerPowerKW);
        registerChargePointBtn = view.findViewById(R.id.registerChargePointBtn);
        progressBar = view.findViewById(R.id.registerChargePointProgressBar);


        btPicker = view.findViewById(R.id.prueba_location);

        /*btPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    Toast.makeText(getContext(), "Ha entrado.", Toast.LENGTH_SHORT).show();

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });*/




        mDatabase = FirebaseDatabase.getInstance().getReference("ChargePoints");

        registerChargePointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String address = registerAddress.getText().toString();
                String postCode = registerPostCode.getText().toString();
                String stateOrProvince = registerStateOrProvince.getText().toString();
                String town = registerTown.getText().toString();
                String GeneralComments = registerGeneralComments.getText().toString();
                String Price = registerPrice.getText().toString();
                String powerKW = registerPowerKW.getText().toString();

                AddressInfoHelperClass AddressInfo = new AddressInfoHelperClass(address, postCode, stateOrProvince, town);

                ConnectionHelperClass connectionHelperClass = new ConnectionHelperClass(powerKW);
                ArrayList<ConnectionHelperClass> Connections = new ArrayList<>();
                Connections.add(connectionHelperClass);

                ChargePointHelperClass chargePointHelperClass = new ChargePointHelperClass(GeneralComments, Price, AddressInfo, Connections);

                mDatabase.child(address).setValue(chargePointHelperClass);

                Toast.makeText(getContext(), "Charge point created correctly.", Toast.LENGTH_SHORT).show();


                Fragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.navHostFragment, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });

    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, getContext());
                StringBuilder stringBuilder = new StringBuilder();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                stringBuilder.append("LATITUDE: ");
                stringBuilder.append(latitude);
                stringBuilder.append("\n");
                stringBuilder.append("LONGITUDE: ");
                stringBuilder.append(longitude);
                registerAddress.setText(stringBuilder.toString());
            }
        }
    }*/
}