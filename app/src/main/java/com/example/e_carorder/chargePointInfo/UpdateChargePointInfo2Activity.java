package com.example.e_carorder.chargePointInfo;

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
import com.example.e_carorder.addChargePoint.AddConnectorActivity;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorAdapter;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorModel;
import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;

import java.util.ArrayList;

public class UpdateChargePointInfo2Activity extends AppCompatActivity {

    private Button addConnectorInUpdateBtn, updateChargePointNextBtn2;
    private FloatingActionButton backCPUpdate2Btn;

    private ArrayList<ConnectorHelperClass> connectors;
    private AddressInfoHelperClass addressInfoHelperClass;
    private String chargePointStatus;

    private RecyclerView recyclerView;
    private ConnectorAdapter connectorAdapter;


    private final int PLACE_PICKER_REQUEST = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_charge_point_info2);

        addressInfoHelperClass = (AddressInfoHelperClass) getIntent().getSerializableExtra("addressInfoHelperClass");
        connectors = (ArrayList<ConnectorHelperClass>) getIntent().getSerializableExtra("connectors");
        chargePointStatus = getIntent().getStringExtra("chargePointStatus");
        //final String chargePointId = getIntent().getStringExtra("chargePointId");

        if(connectors == null){
            connectors = new ArrayList<>();
        }

        addConnectorInUpdateBtn = findViewById(R.id.addConnectorInUpdateBtn);
        updateChargePointNextBtn2 = findViewById(R.id.updateChargePointNextBtn2);
        backCPUpdate2Btn = findViewById(R.id.backCPUpdate2Btn);

        recyclerView = findViewById(R.id.recyclerViewConnectorsCPUpdate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        connectorAdapter = new ConnectorAdapter(UpdateChargePointInfo2Activity.this, createConnectorsList());
        recyclerView.setAdapter(connectorAdapter);

        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

        Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        ChargePointHelperClass chargePointHelperClass = ds.getValue(ChargePointHelperClass.class);

                        DataSnapshot dataSnapshotConnectors = ds.child("connectors");

                        ArrayList<ConnectorModel> connectorModels = new ArrayList<>();

                        for(DataSnapshot dsConnector : dataSnapshotConnectors.getChildren()){
                            ConnectorHelperClass connectorHelperClass = dsConnector.getValue(ConnectorHelperClass.class);

                            ConnectorModel addConnectorModel = new ConnectorModel(
                                    chargePointId,
                                    dsConnector.getKey(),
                                    "",
                                    connectorHelperClass.getConnectorType(),
                                    connectorHelperClass.getPowerKW(),
                                    R.drawable.electrical,
                                    0,
                                    0,
                                    false,
                                    -1,
                                    true
                            );

                            connectorModels.add(addConnectorModel);
                        }

                        connectorAdapter = new ConnectorAdapter(UpdateChargePointInfo2Activity.this, connectorModels);
                        recyclerView.setAdapter(connectorAdapter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        addConnectorInUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectorAdapter currentConnectorAdapter = (ConnectorAdapter) recyclerView.getAdapter();
                ArrayList<ConnectorModel> connectorsFromAdapter = currentConnectorAdapter.getConnectorModels();

                if(connectors.size() > 1 && connectors.size() != connectorsFromAdapter.size()){
                    for(int i = connectors.size()-1; i >= 0; i--){
                        boolean found = false;

                        for(int j = 0; j < connectorsFromAdapter.size() && !found; j++){
                            if(Integer.parseInt(connectorsFromAdapter.get(j).getConnectorId()) == i){
                                found = true;
                                connectors.get(i).setConnectorType(connectorsFromAdapter.get(j).getConnectorType());
                                connectors.get(i).setPowerKW(connectorsFromAdapter.get(j).getConnectorPowerKW());
                            }
                        }

                        if(found == false){
                            connectors.remove(i);

                        } else {
                            found = false;

                        }
                    }
                }


                Intent i = new Intent(v.getContext(), AddConnectorActivity.class);
                i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                i.putExtra("connectors", connectors);
                i.putExtra("chargePointStatus", chargePointStatus);
                i.putExtra("updateChargePoint", "Yes");
                startActivity(i);
            }
        });

        updateChargePointNextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (connectors.size() == 0) {
                    Toast.makeText(UpdateChargePointInfo2Activity.this, "You have to add minimum 1 connector before continuing.", Toast.LENGTH_SHORT).show();

                } else {
                    Intent i = new Intent(v.getContext(), UpdateChargePointInfo3Activity.class);
                    i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                    i.putExtra("connectors", connectors);
                    i.putExtra("chargePointStatus", chargePointStatus);
                    startActivity(i);

                }

                /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

                Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

                checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                ChargePointHelperClass currentChargePoint = dataSnapshot.getValue(ChargePointHelperClass.class);

                                if (currentChargePoint.getConnectors().size() == 0) {
                                    Toast.makeText(UpdateChargePointInfo2Activity.this, "You have to add minimum 1 connector before continuing.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Intent i = new Intent(v.getContext(), UpdateChargePointInfo3Activity.class);
                                    i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                                    i.putExtra("connectors", connectors);
                                    i.putExtra("chargePointStatus", chargePointStatus);
                                    startActivity(i);

                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
            }
        });

        backCPUpdate2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateChargePointInfo2Activity.super.onBackPressed();
            }
        });

    }

    private ArrayList<ConnectorModel> createConnectorsList(){

        ArrayList<ConnectorModel> connectorModels = new ArrayList<>();

        for(int i=0; i < connectors.size(); i++){
            ConnectorModel m = new ConnectorModel("", Integer.toString(i), "", connectors.get(i).getConnectorType(),
                    connectors.get(i).getPowerKW(), R.drawable.electrical, 0, 0, false, -1,
                    true, addressInfoHelperClass, connectors, chargePointStatus);
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