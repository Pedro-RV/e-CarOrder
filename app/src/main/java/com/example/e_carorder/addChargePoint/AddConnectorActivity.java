package com.example.e_carorder.addChargePoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e_carorder.R;
import com.example.e_carorder.chargePointInfo.UpdateChargePointInfo2Activity;
import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.QueueItemHelperClass;
import com.example.e_carorder.helpers.ReservationUserHelperClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddConnectorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerConnectorType;
    private EditText powerKW_et;
    private Button addConnectorBtn;
    private FloatingActionButton backAddConnectorBtn;
    private String spinnerSelection;

    private TextView chargePointInfoTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_connector);

        final AddressInfoHelperClass addressInfoHelperClass = (AddressInfoHelperClass) getIntent().getSerializableExtra("addressInfoHelperClass");
        final ArrayList<ConnectorHelperClass> connectors = (ArrayList<ConnectorHelperClass>) getIntent().getSerializableExtra("connectors");
        final String chargePointStatus = getIntent().getStringExtra("chargePointStatus");
        final String updateChargePoint = getIntent().getStringExtra("updateChargePoint");

        spinnerConnectorType = findViewById(R.id.spinnerConnectorType);
        powerKW_et = findViewById(R.id.powerKW_et);
        addConnectorBtn = findViewById(R.id.addConnectorBtn);
        chargePointInfoTittle = findViewById(R.id.chargePointInfoTittle);
        backAddConnectorBtn = findViewById(R.id.backAddConnectorBtn);

        if(updateChargePoint != null) {
            chargePointInfoTittle.setText("Charge Point Update");
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.connectorsType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConnectorType.setAdapter(adapter);

        spinnerConnectorType.setOnItemSelectedListener(this);

        addConnectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String powerKW;

                if(powerKW_et.getText().toString().isEmpty()){
                    powerKW_et.setError("Compulsory field.");
                    return;

                } else{
                    powerKW = powerKW_et.getText().toString();
                }

                connectors.add(new ConnectorHelperClass("", spinnerSelection, powerKW, false, -1, -1,
                        new ArrayList<ReservationUserHelperClass>(), new ArrayList<QueueItemHelperClass>()));

                if(updateChargePoint != null){
                    Intent i = new Intent(v.getContext(), UpdateChargePointInfo2Activity.class);
                    i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                    i.putExtra("connectors", connectors);
                    i.putExtra("chargePointStatus", chargePointStatus);
                    startActivity(i);

                } else{
                    Intent i = new Intent(v.getContext(), AddChargePoint2Activity.class);
                    i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                    i.putExtra("connectors", connectors);
                    i.putExtra("chargePointStatus", chargePointStatus);
                    startActivity(i);
                }

            }
        });

        backAddConnectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddConnectorActivity.super.onBackPressed();
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