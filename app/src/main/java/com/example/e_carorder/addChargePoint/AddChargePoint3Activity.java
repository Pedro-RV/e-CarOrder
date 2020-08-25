package com.example.e_carorder.addChargePoint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_carorder.R;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorModel;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorAdapter;
import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;

import java.util.ArrayList;

public class AddChargePoint3Activity extends AppCompatActivity  {

    private Button addBtn, registerChargePointNextBtn2;

    private ArrayList<ConnectorHelperClass> connectors;

    private RecyclerView recyclerView;
    private ConnectorAdapter connectorAdapter;


    private final int PLACE_PICKER_REQUEST = 9999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charge_point3);

        final AddressInfoHelperClass addressInfoHelperClass = (AddressInfoHelperClass) getIntent().getSerializableExtra("addressInfoHelperClass");
        connectors = (ArrayList<ConnectorHelperClass>) getIntent().getSerializableExtra("connectors");

        if(connectors == null){
            connectors = new ArrayList<>();
        }

        addBtn = findViewById(R.id.addBtn);
        registerChargePointNextBtn2 = findViewById(R.id.registerChargePointNextBtn2);

        recyclerView = findViewById(R.id.recyclerViewConnectors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        connectorAdapter = new ConnectorAdapter(this, createConnectorsList());
        recyclerView.setAdapter(connectorAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddConnectorActivity.class);
                i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                i.putExtra("connectors", connectors);
                startActivity(i);
            }
        });

        registerChargePointNextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectors.size() == 0){
                    Toast.makeText(AddChargePoint3Activity.this, "You have to add minimum 1 connector before continuing.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(v.getContext(), AddChargePoint4Activity.class);
                    i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                    i.putExtra("connectors", connectors);
                    startActivity(i);
                }
            }
        });

    }

    private ArrayList<ConnectorModel> createConnectorsList(){

        ArrayList<ConnectorModel> connectorModels = new ArrayList<>();

        for(int i=0; i < connectors.size(); i++){
            ConnectorModel m = new ConnectorModel(connectors.get(i).getConnectorType(), connectors.get(i).getPowerKW(), R.drawable.electrical);
            connectorModels.add(m);
        }

        return connectorModels;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}