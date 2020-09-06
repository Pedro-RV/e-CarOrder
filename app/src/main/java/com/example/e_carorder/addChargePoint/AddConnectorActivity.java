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

import com.example.e_carorder.R;
import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.QueueHelperClass;
import com.example.e_carorder.helpers.ReservationUserHelperClass;

import java.util.ArrayList;

public class AddConnectorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerConnectorType;
    private EditText powerKW_et;
    private Button addConnectorBtn;
    private String spinnerSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_connector);

        final AddressInfoHelperClass addressInfoHelperClass = (AddressInfoHelperClass) getIntent().getSerializableExtra("addressInfoHelperClass");
        final ArrayList<ConnectorHelperClass> connectors = (ArrayList<ConnectorHelperClass>) getIntent().getSerializableExtra("connectors");

        spinnerConnectorType = findViewById(R.id.spinnerConnectorType);
        powerKW_et = findViewById(R.id.powerKW_et);
        addConnectorBtn = findViewById(R.id.addConnectorBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.connectorsType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConnectorType.setAdapter(adapter);

        spinnerConnectorType.setOnItemSelectedListener(this);

        addConnectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String powerKW = powerKW_et.getText().toString();
                connectors.add(new ConnectorHelperClass("", spinnerSelection, powerKW, false, -1, new ArrayList<ReservationUserHelperClass>(), new ArrayList<QueueHelperClass>()));
                Intent i = new Intent(v.getContext(), AddChargePoint3Activity.class);
                i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                i.putExtra("connectors", connectors);
                startActivity(i);
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