package com.example.e_carorder.JSONChargePoints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_carorder.JSONChargePoints.FetchData;
import com.example.e_carorder.R;
import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ChargePointHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.QueueItemHelperClass;
import com.example.e_carorder.helpers.ReservationUserHelperClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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

                String dataTotal = null;
                try {
                    dataTotal = process.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

                String title, address, stateOrProvince, town, postCode, latitude, longitude;
                ArrayList<String> titles = new ArrayList<>();

                int counterTotalItems = 0;
                int counterAttributes = -1;

                String[] parts = dataTotal.split("\\\n"); // String array, each element is text between dots

                String beforeFirstDot = parts[0];

                while(counterTotalItems < 200){
                    counterAttributes = counterAttributes+1;
                    address = parts[counterAttributes];
                    counterAttributes = counterAttributes+1;
                    title = parts[counterAttributes];
                    counterAttributes = counterAttributes+1;
                    stateOrProvince = parts[counterAttributes];
                    counterAttributes = counterAttributes+1;
                    town = parts[counterAttributes];
                    counterAttributes = counterAttributes+1;
                    postCode = parts[counterAttributes];
                    counterAttributes = counterAttributes+1;
                    latitude = parts[counterAttributes];
                    counterAttributes = counterAttributes+1;
                    longitude = parts[counterAttributes];

                    AddressInfoHelperClass addressInfoHelperClass = new AddressInfoHelperClass(title, address, stateOrProvince, town, postCode,
                            Double.parseDouble(latitude), Double.parseDouble(longitude));

                    ConnectorHelperClass connectorHelperClass = new ConnectorHelperClass("", "Schuko", "3.4", false,
                            -1, -1, new ArrayList<ReservationUserHelperClass>(), new ArrayList<QueueItemHelperClass>());

                    ArrayList<ConnectorHelperClass> connectors = new ArrayList<>();

                    connectors.add(connectorHelperClass);

                    String statusType;

                    if(counterTotalItems/8 == 0){
                        statusType = "Unkown";

                    } else if(counterTotalItems/15 == 0){
                        statusType = "Damaged";

                    }else{
                        statusType = "Available";

                    }



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

                    ChargePointHelperClass chargePointHelperClass = new ChargePointHelperClass(id, statusType, addressInfoHelperClass, connectors);

                    boolean alreadyAdded = false;

                    for(int j = 0; j < titles.size() && !alreadyAdded; j++){
                        if(title.equals(titles.get(j))){
                            alreadyAdded = true;
                        }
                    }

                    titles.add(title);

                    if(!alreadyAdded){
                        databaseReference.child(id).setValue(chargePointHelperClass);
                    }

                    counterTotalItems = counterTotalItems + 1;

                }


            }
        });

    }
}