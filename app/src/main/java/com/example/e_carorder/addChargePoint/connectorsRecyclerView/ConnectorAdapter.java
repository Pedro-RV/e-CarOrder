package com.example.e_carorder.addChargePoint.connectorsRecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;
import com.example.e_carorder.chargePointInfo.ChargePointInfoActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConnectorAdapter extends RecyclerView.Adapter<ConnectorHolder> {

    Context c;
    ArrayList<ConnectorModel> connectorModels;

    public ConnectorAdapter(Context c, ArrayList<ConnectorModel> connectorModels) {
        this.c = c;
        this.connectorModels = connectorModels;
    }

    @NonNull
    @Override
    public ConnectorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connector_item, null);

        return new ConnectorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ConnectorHolder holder, final int position) {

        holder.connectorType.setText(connectorModels.get(position).getConnectorType());
        holder.connectorPowerKW.setText(connectorModels.get(position).getConnectorPowerKW() + " kW");
        holder.imageConnector.setImageResource(connectorModels.get(position).getImageConnector());

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String chargePointId = connectorModels.get(position).getChargePointId();
        final String connectorId = connectorModels.get(position).getConnectorId();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

        if(connectorModels.get(position).getChargePointId().isEmpty()){
            android.view.ViewGroup.LayoutParams connectorTypeParams = holder.connectorType.getLayoutParams();
            connectorTypeParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.connectorType.setLayoutParams(connectorTypeParams);

            android.view.ViewGroup.LayoutParams connectorPowerKWParams = holder.connectorPowerKW.getLayoutParams();
            connectorPowerKWParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.connectorPowerKW.setLayoutParams(connectorPowerKWParams);

        } else if(connectorModels.get(position).getCheckInUserId().isEmpty()){
            holder.checkInBtn.setVisibility(View.VISIBLE);
            holder.alertBtn.setVisibility(View.VISIBLE);

            if(connectorModels.get(position).getAlert()){
                Long currentDate = Calendar.getInstance().getTimeInMillis();

                Long alertDate = connectorModels.get(position).getAlertDate();

                Long difference = currentDate - alertDate;

                long minutesDiff = TimeUnit.MILLISECONDS.toMinutes(difference);

                if(minutesDiff >= 180){
                    holder.alertTV.setVisibility(View.GONE);

                    Map<String, Object> map = new HashMap<>();
                    map.put("alert", false);
                    map.put("alertDate", -1);

                    databaseReference.child(chargePointId).child("connectors").child(connectorId).updateChildren(map);

                } else{
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(alertDate);

                    int hourAlert = calendar.get(Calendar.HOUR);
                    int minuteAlert = calendar.get(Calendar.MINUTE);

                    if(minuteAlert < 10){
                        holder.alertTV.setText("Alert, invaded parking! Registered at: " + hourAlert + ":0" + minuteAlert);
                    } else{
                        holder.alertTV.setText("Alert, invaded parking! Registered at: " + hourAlert + ":" + minuteAlert);
                    }

                    holder.alertTV.setVisibility(View.VISIBLE);

                }

            }

        } else if(!connectorModels.get(position).getCheckInUserId().equals(userId)) {
            holder.inUseBtn.setVisibility(View.VISIBLE);
            holder.alertBtn.setVisibility(View.VISIBLE);

        } else{
            holder.checkOutBtn.setVisibility(View.VISIBLE);
            holder.alertBtn.setVisibility(View.VISIBLE);

        }

        holder.checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(c);

                if(ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                    locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                double userLatitude = location.getLatitude();
                                double userLongitude = location.getLongitude();

                                double chargePointLatitude = connectorModels.get(position).getChargePointLatitude();
                                double chargePointLongitude = connectorModels.get(position).getChargePointLongitude();

                                double distance = CalculationByDistance(userLatitude, userLongitude, chargePointLatitude, chargePointLongitude);

                                if(distance <= 1){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("checkInUserId", userId);

                                    databaseReference.child(chargePointId).child("connectors").child(connectorId).updateChildren(map);

                                    holder.checkInBtn.setVisibility(View.GONE);
                                    holder.checkOutBtn.setVisibility(View.VISIBLE);

                                    if(holder.alertTV.getVisibility() == View.VISIBLE){
                                        holder.alertTV.setVisibility(View.GONE);

                                        Map<String, Object> mapAlert = new HashMap<>();
                                        mapAlert.put("alert", false);
                                        mapAlert.put("alertDate", -1);

                                        databaseReference.child(chargePointId).child("connectors").child(connectorId).updateChildren(mapAlert);
                                    }

                                }else{
                                    Toast.makeText(c, "You have to be closer to the point.", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(c, "You can't check-in without location activated.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else{
                    Toast.makeText(c, "You must give location permits before trying to check-in.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> map = new HashMap<>();
                map.put("checkInUserId", "");

                databaseReference.child(chargePointId).child("connectors").child(connectorId).updateChildren(map);

                holder.checkOutBtn.setVisibility(View.GONE);
                holder.checkInBtn.setVisibility(View.VISIBLE);

            }
        });

        holder.alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkInBtn.getVisibility() == View.VISIBLE){
                    Calendar rightNow = Calendar.getInstance();
                    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = rightNow.get(Calendar.MINUTE);

                    if(currentMinute < 10){
                        holder.alertTV.setText("Alert, invaded parking! Registered at: " + currentHour + ":0" + currentMinute);
                    } else{
                        holder.alertTV.setText("Alert, invaded parking! Registered at: " + currentHour + ":" + currentMinute);
                    }

                    holder.alertTV.setVisibility(View.VISIBLE);

                    Long currentDate = Calendar.getInstance().getTimeInMillis();

                    Map<String, Object> map = new HashMap<>();
                    map.put("alert", true);
                    map.put("alertDate", currentDate);

                    databaseReference.child(chargePointId).child("connectors").child(connectorId).updateChildren(map);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return connectorModels.size();
    }

    public double CalculationByDistance(double initialLat, double initialLong,
                                        double finalLat, double finalLong){
        int R = 6371; // km (Earth radius)
        double dLat = toRadians(finalLat-initialLat);
        double dLon = toRadians(finalLong-initialLong);
        initialLat = toRadians(initialLat);
        finalLat = toRadians(finalLat);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(initialLat) * Math.cos(finalLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    public double toRadians(double deg) {
        return deg * (Math.PI/180);
    }

}
