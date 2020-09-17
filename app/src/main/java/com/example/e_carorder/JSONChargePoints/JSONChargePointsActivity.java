package com.example.e_carorder.JSONChargePoints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.e_carorder.JSONChargePoints.FetchData;
import com.example.e_carorder.R;

public class JSONChargePointsActivity extends AppCompatActivity {

    private Button click;
    public static TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j_s_o_n_charge_points);

        click = findViewById(R.id.jsonBtn);
        data = findViewById(R.id.fetchedData);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchData process = new FetchData();
                process.execute();

            }
        });

    }
}