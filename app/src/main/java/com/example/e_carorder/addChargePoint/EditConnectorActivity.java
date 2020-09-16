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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditConnectorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerConnectorType;
    private EditText powerKW_et;
    private Button editConnectorBtn;
    private FloatingActionButton backEditConnectorBtn;
    private String spinnerSelection;

    private TextView chargePointInfoTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_connector);

        final AddressInfoHelperClass addressInfoHelperClass = (AddressInfoHelperClass) getIntent().getSerializableExtra("addressInfoHelperClass");
        final ArrayList<ConnectorHelperClass> connectors = (ArrayList<ConnectorHelperClass>) getIntent().getSerializableExtra("connectors");
        final String chargePointStatus = getIntent().getStringExtra("chargePointStatus");
        final String updateChargePoint = getIntent().getStringExtra("updateChargePoint");
        final String connectorId = getIntent().getStringExtra("connectorId");

        spinnerConnectorType = findViewById(R.id.spinnerConnectorType);
        powerKW_et = findViewById(R.id.powerKW_et);
        editConnectorBtn = findViewById(R.id.editConnectorBtn);
        chargePointInfoTittle = findViewById(R.id.chargePointInfoTittle);
        backEditConnectorBtn = findViewById(R.id.backEditConnectorBtn);

        if(!updateChargePoint.isEmpty()) {
            chargePointInfoTittle.setText("Charge Point Update");
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.connectorsType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConnectorType.setAdapter(adapter);

        List<String> connectorsType = Arrays.asList(getResources().getStringArray(R.array.connectorsType));

        for(int i = 0; i < connectorsType.size(); i++){
            if(connectors.get(Integer.parseInt(connectorId)).getConnectorType().equals(connectorsType.get(i))){
                spinnerConnectorType.setSelection(i);
            }
        }

        spinnerConnectorType.setOnItemSelectedListener(this);

        powerKW_et.setText(connectors.get(Integer.parseInt(connectorId)).getPowerKW());

        editConnectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String powerKW;

                if(powerKW_et.getText().toString().isEmpty()){
                    powerKW_et.setError("Compulsory field.");
                    return;

                } else{
                    powerKW = powerKW_et.getText().toString();
                }

                connectors.get(Integer.parseInt(connectorId)).setPowerKW(powerKW);

                connectors.get(Integer.parseInt(connectorId)).setConnectorType(spinnerSelection);

                if(!updateChargePoint.isEmpty()){
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



        backEditConnectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditConnectorActivity.super.onBackPressed();
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