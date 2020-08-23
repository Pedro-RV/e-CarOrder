package com.example.e_carorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.recyclerViewConnectors.Model;
import com.example.e_carorder.recyclerViewConnectors.MyAdapter;

import java.util.ArrayList;

public class AddChargePoint2Activity extends AppCompatActivity  {

    private Button addBtn, registerChargePointNextBtn2;

    private ArrayList<ConnectorHelperClass> connectors;

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charge_point2);

        final AddressInfoHelperClass addressInfoHelperClass = (AddressInfoHelperClass) getIntent().getSerializableExtra("addressInfoHelperClass");
        connectors = (ArrayList<ConnectorHelperClass>) getIntent().getSerializableExtra("connectors");

        if(connectors == null){
            connectors = new ArrayList<>();
        }

        addBtn = findViewById(R.id.addBtn);
        registerChargePointNextBtn2 = findViewById(R.id.registerChargePointNextBtn2);

        recyclerView = findViewById(R.id.recyclerViewConnectors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, getConnectorsList(connectors));
        recyclerView.setAdapter(myAdapter);

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
                    Toast.makeText(AddChargePoint2Activity.this, "You have to add minimum 1 connector before continuing.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(v.getContext(), AddChargePoint3Activity.class);
                    i.putExtra("addressInfoHelperClass", addressInfoHelperClass);
                    i.putExtra("connectors", connectors);
                    startActivity(i);
                }
            }
        });

    }

    private ArrayList<Model> getConnectorsList(ArrayList<ConnectorHelperClass> connectors){

        ArrayList<Model> models = new ArrayList<>();

        for(int i=0; i < connectors.size(); i++){
            Model m = new Model(connectors.get(i).getConnectorType(), connectors.get(i).getPowerKW(), R.drawable.electrical);
            models.add(m);
        }

        return models;
    }

}