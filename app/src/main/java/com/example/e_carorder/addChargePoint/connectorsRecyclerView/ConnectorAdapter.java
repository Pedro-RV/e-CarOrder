package com.example.e_carorder.addChargePoint.connectorsRecyclerView;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_carorder.R;
import com.example.e_carorder.addChargePoint.EditConnectorActivity;
import com.example.e_carorder.chargePointInfo.ConnectorQueueActivity;
import com.example.e_carorder.chargePointInfo.ConnectorReservationActivity;
import com.example.e_carorder.chargePointInfo.UpdateChargePointInfo3Activity;
import com.example.e_carorder.chargePointInfo.UserInfoActivity;
import com.example.e_carorder.helpers.ChargePointHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.QueueItemHelperClass;
import com.example.e_carorder.helpers.ReservationUserHelperClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
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

    public ArrayList<ConnectorModel> getConnectorModels(){
        return connectorModels;
    }

    @NonNull
    @Override
    public ConnectorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connector_item, parent, false);

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

        if(connectorModels.get(position).getChargePointUpdateConnector() == true){
            holder.editConnectorBtn.setVisibility(View.VISIBLE);
            holder.deleteConnectorBtn.setVisibility(View.VISIBLE);

            holder.editConnectorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), EditConnectorActivity.class);
                    i.putExtra("addressInfoHelperClass", connectorModels.get(position).getAddressInfoHelperClass());
                    i.putExtra("connectors", connectorModels.get(position).getConnectors());
                    i.putExtra("chargePointStatus", connectorModels.get(position).getChargePointStatus());
                    i.putExtra("updateChargePoint", "Yes");
                    i.putExtra("connectorId", Integer.toString(position));
                    c.startActivity(i);

                }
            });

            holder.deleteConnectorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder reservationConfirmationDialog = new AlertDialog.Builder(v.getContext());
                    reservationConfirmationDialog.setTitle("Are you sure you want to delete the reservation?");

                    reservationConfirmationDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(connectorModels.size() == 1){
                                Toast.makeText(c, "there must be at least one connector", Toast.LENGTH_SHORT).show();

                            } else{
                                connectorModels.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, connectorModels.size());
                            }

                        }

                    });

                    reservationConfirmationDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // close the dialog
                        }
                    });

                    reservationConfirmationDialog.create().show();

                }
            });

        } else{

            if(!connectorModels.get(position).getChargePointId().isEmpty()){
                holder.reserveBtn.setVisibility(View.VISIBLE);
                holder.reserveNumber.setVisibility(View.VISIBLE);

                Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

                checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot ds : dataSnapshot.getChildren()){

                                ConnectorHelperClass connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                                if(connector.getCheckOutTime() != -1){
                                    long checkOutTime = connector.getCheckOutTime();

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(checkOutTime);

                                    int hourCheckOut = calendar.get(Calendar.HOUR_OF_DAY);
                                    int minuteCheckOut = calendar.get(Calendar.MINUTE);

                                    if(minuteCheckOut < 10){
                                        holder.checkOutBtn.setText("Check-out at " + hourCheckOut + ":0" + minuteCheckOut);
                                        holder.inUseBtn.setText("In use until " + hourCheckOut + ":0" + minuteCheckOut);

                                    }else{
                                        holder.checkOutBtn.setText("Check-out at " + hourCheckOut + ":" + minuteCheckOut);
                                        holder.inUseBtn.setText("In use until "  + hourCheckOut + ":" + minuteCheckOut);

                                    }

                                }

                                ArrayList<QueueItemHelperClass> queue = connector.getQueue();

                                if(queue != null){
                                    holder.queueNumber.setText(Integer.toString(queue.size()));
                                } else{
                                    holder.queueNumber.setText("0");
                                }

                                ArrayList<ReservationUserHelperClass> reserves = connector.getReservations();

                                if(reserves != null){
                                    holder.reserveNumber.setText(Integer.toString(reserves.size()));

                                } else{
                                    holder.reserveNumber.setText("0");

                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            if(connectorModels.get(position).getChargePointId().isEmpty()){


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

                        int hourAlert = calendar.get(Calendar.HOUR_OF_DAY);
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
                holder.queueBtn.setVisibility(View.VISIBLE);
                holder.queueNumber.setVisibility(View.VISIBLE);

                DocumentReference documentReference = FirebaseFirestore.getInstance()
                        .collection("users").document(connectorModels.get(position).getCheckInUserId());

                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e == null){
                            if(documentSnapshot.exists()){
                                final String usernameUsingConnector = documentSnapshot.getString("username");
                                final String carModel = documentSnapshot.getString("carModel");
                                final String description = documentSnapshot.getString("description");

                                holder.usernameUserUsingConnector.setText(usernameUsingConnector);
                                holder.usernameUserUsingConnector.setVisibility(View.VISIBLE);

                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                                StorageReference profileRef = storageReference.child("users/"+connectorModels.get(position).getCheckInUserId()+"/profile.jpg");
                                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(c).load(uri).into(holder.imageUserUsingConnector);
                                    }
                                });

                                holder.imageUserUsingConnector.setVisibility(View.VISIBLE);

                                holder.imageUserUsingConnector.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(v.getContext(), UserInfoActivity.class);
                                        i.putExtra("userId", connectorModels.get(position).getCheckInUserId());
                                        i.putExtra("username", usernameUsingConnector);
                                        i.putExtra("carModel", carModel);
                                        i.putExtra("description", description);
                                        c.startActivity(i);
                                    }
                                });


                            }
                        }
                    }
                });

                holder.queueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), ConnectorQueueActivity.class);
                        i.putExtra("chargePointId", connectorModels.get(position).getChargePointId());
                        i.putExtra("connectorId", connectorModels.get(position).getConnectorId());
                        c.startActivity(i);

                    }
                });

            } else{
                holder.checkOutBtn.setVisibility(View.VISIBLE);

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

                                    double distance = calculationByDistance(userLatitude, userLongitude, chargePointLatitude, chargePointLongitude);

                                    if(distance <= 1){
                                        TimePickerDialog timePickerDialog;

                                        Calendar currentTime = Calendar.getInstance();
                                        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                                        int minute = currentTime.get(Calendar.MINUTE);

                                        timePickerDialog = new TimePickerDialog(c, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                Calendar time = Calendar.getInstance();
                                                time.set(0, 0, 0, hourOfDay, minute);

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("checkInUserId", userId);
                                                map.put("checkOutTime", time.getTimeInMillis());

                                                databaseReference.child(chargePointId).child("connectors").child(connectorId).updateChildren(map);

                                                if(minute < 10){
                                                    holder.checkOutBtn.setText("Check-out at " + hourOfDay + ":0" + minute);

                                                }else{
                                                    holder.checkOutBtn.setText("Check-out at " + hourOfDay + ":" + minute);

                                                }

                                                holder.checkInBtn.setVisibility(View.GONE);
                                                holder.checkOutBtn.setVisibility(View.VISIBLE);
                                                holder.alertBtn.setVisibility(View.GONE);

                                                if(holder.alertTV.getVisibility() == View.VISIBLE){
                                                    holder.alertTV.setVisibility(View.GONE);

                                                    Map<String, Object> mapAlert = new HashMap<>();
                                                    mapAlert.put("alert", false);
                                                    mapAlert.put("alertDate", -1);

                                                    databaseReference.child(chargePointId).child("connectors").child(connectorId).updateChildren(mapAlert);
                                                }

                                            }
                                        }, hour, minute, true);

                                        TextView titleTV = new TextView(c);
                                        titleTV.setText("Select Check-out Time");
                                        titleTV.setTextSize(30);
                                        titleTV.setTextColor(c.getResources().getColor(R.color.colorPrimary));
                                        titleTV.setTypeface(Typeface.DEFAULT_BOLD);

                                        timePickerDialog.setCustomTitle(titleTV);
                                        timePickerDialog.show();

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
                    holder.alertBtn.setVisibility(View.VISIBLE);

                }
            });

            holder.alertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkInBtn.getVisibility() == View.VISIBLE){

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

                                        double distance = calculationByDistance(userLatitude, userLongitude, chargePointLatitude, chargePointLongitude);

                                        if(distance <= 1){
                                            Calendar rightNow = Calendar.getInstance();
                                            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                                            int currentMinute = rightNow.get(Calendar.MINUTE);

                                            if(currentMinute < 10){
                                                holder.alertTV.setText("Alert, invaded parking! Registered at: " + currentHour + ":0" + currentMinute);
                                            } else{
                                                holder.alertTV.setText("Alert, invaded parking! Registered at: " + currentHour + ":" + currentMinute);
                                            }

                                            holder.alertTV.setVisibility(View.VISIBLE);

                                            Long currentDate = rightNow.getInstance().getTimeInMillis();

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("alert", true);
                                            map.put("alertDate", currentDate);

                                            databaseReference.child(chargePointId).child("connectors").child(connectorId).updateChildren(map);

                                        }else{
                                            Toast.makeText(c, "You have to be closer to the point.", Toast.LENGTH_SHORT).show();
                                        }

                                    }else{
                                        Toast.makeText(c, "You can't give an alert without location activated.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(c, "You must give location permits before trying to give an alert.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

            holder.reserveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ConnectorReservationActivity.class);
                    i.putExtra("chargePointId", connectorModels.get(position).getChargePointId());
                    i.putExtra("connectorId", connectorModels.get(position).getConnectorId());
                    c.startActivity(i);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return connectorModels.size();
    }

    public double calculationByDistance(double initialLat, double initialLong,
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
