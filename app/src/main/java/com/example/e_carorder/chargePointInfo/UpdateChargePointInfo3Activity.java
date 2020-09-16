package com.example.e_carorder.chargePointInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.e_carorder.Navigation_temporal;
import com.example.e_carorder.R;
import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ChargePointHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateChargePointInfo3Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerUpdateStatus;
    private String spinnerSelection;
    private Button updateChargePointFinishBtn;
    private FloatingActionButton backCPUpdate3Btn;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_charge_point_info3);

        final AddressInfoHelperClass addressInfoHelperClass = (AddressInfoHelperClass) getIntent().getSerializableExtra("addressInfoHelperClass");
        final ArrayList<ConnectorHelperClass> connectors = (ArrayList<ConnectorHelperClass>) getIntent().getSerializableExtra("connectors");
        String chargePointStatus = getIntent().getStringExtra("chargePointStatus");


        spinnerUpdateStatus = findViewById(R.id.spinnerUpdateStatus);
        updateChargePointFinishBtn = findViewById(R.id.updateChargePointFinishBtn);
        backCPUpdate3Btn = findViewById(R.id.backCPUpdate3Btn);

        mDatabase = FirebaseDatabase.getInstance().getReference("ChargePoints");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.statusType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpdateStatus.setAdapter(adapter);

        List<String> statusTypes = Arrays.asList(getResources().getStringArray(R.array.statusType));

        for(int i = 0; i < statusTypes.size(); i++){
            if(chargePointStatus.equals(statusTypes.get(i))){
                spinnerUpdateStatus.setSelection(i);
            }
        }

        spinnerUpdateStatus.setOnItemSelectedListener(this);

        updateChargePointFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String latitude = Double.toString(addressInfoHelperClass.getLatitude());
                String longitude = Double.toString(addressInfoHelperClass.getLongitude());

                latitude = latitude.replace(".", "");
                latitude = latitude.replace("-", "");
                longitude = longitude.replace(".", "");
                longitude = longitude.replace("-", "");

                if(latitude.length() < 5){
                    latitude = latitude + "0000";
                }

                if(longitude.length() < 5){
                    longitude = longitude + "0000";
                }

                String id = latitude.substring(0,5) + longitude.substring(0,5);

                ChargePointHelperClass chargePointHelperClass = new ChargePointHelperClass(id, spinnerSelection, addressInfoHelperClass, connectors);

                mDatabase.child(id).setValue(chargePointHelperClass);

                Toast.makeText(UpdateChargePointInfo3Activity.this, "Charge point created correctly.", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(v.getContext(), Navigation_temporal.class);
                startActivity(i);
            }
        });

        backCPUpdate3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateChargePointInfo3Activity.super.onBackPressed();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelection = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}