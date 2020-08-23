package com.example.e_carorder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_carorder.helpers.AddressInfoHelperClass;

public class AddChargePoint1Fragment extends Fragment {

    private EditText registerTitle, registerAddress, registerPostCode, registerStateOrProvince, registerTown;
    private Button registerChargePointNextBtn1;

    // Dejar momentaneamente mientras desarrollo registro CP

    //private DatabaseReference mDatabase;


    public AddChargePoint1Fragment() {
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
        return inflater.inflate(R.layout.fragment_add_charge_point1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerTitle = view.findViewById(R.id.registerTitle);
        registerAddress = view.findViewById(R.id.registerAddress);
        registerPostCode = view.findViewById(R.id.registerPostCode);
        registerStateOrProvince = view.findViewById(R.id.registerStateOrProvince);
        registerTown = view.findViewById(R.id.registerTown);
        registerChargePointNextBtn1 = view.findViewById(R.id.registerChargePointNextBtn1);


        // Dejar momentaneamente mientras desarrollo registro CP

        //mDatabase = FirebaseDatabase.getInstance().getReference("ChargePoints");

        registerChargePointNextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = registerTitle.getText().toString();
                String address = registerAddress.getText().toString();
                String postCode = registerPostCode.getText().toString();
                String stateOrProvince = registerStateOrProvince.getText().toString();
                String town = registerTown.getText().toString();

                double latitude = 12;
                double longitude = 12;

                AddressInfoHelperClass addressInfoHelperClass = new AddressInfoHelperClass(title, address, stateOrProvince, town, postCode, latitude, longitude);

                Intent i = new Intent(v.getContext(), AddChargePoint2Activity.class);
                i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                startActivity(i);

                // Dejar momentaneamente mientras desarrollo registro CP

                /*ConnectorHelperClass connectionHelperClass = new ConnectorHelperClass(powerKW);
                ArrayList<ConnectorHelperClass> connections = new ArrayList<>();
                connections.add(connectionHelperClass);

                ChargePointHelperClass chargePointHelperClass = new ChargePointHelperClass(generalComments, price, addressInfoHelperClass, connections);

                mDatabase.child(address).setValue(chargePointHelperClass);

                Toast.makeText(getContext(), "Charge point created correctly.", Toast.LENGTH_SHORT).show();


                Fragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.navHostFragment, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/


            }
        });

    }
}