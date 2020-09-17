package com.example.e_carorder.chargePointInfo.reservationsRecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_carorder.R;
import com.example.e_carorder.chargePointInfo.ConnectorReservationActivity;
import com.example.e_carorder.chargePointInfo.UserInfoActivity;
import com.example.e_carorder.chats.usersRecyclerView.UserHolder;
import com.example.e_carorder.chats.usersRecyclerView.UserModel;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.ReservationUserHelperClass;
import com.example.e_carorder.signInSignUp.SignInSignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.Calendar;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationHolder> {

    private Context context;
    private ArrayList<ReservationModel> mReservations;

    public ReservationAdapter(Context context, ArrayList<ReservationModel> mReservations) {
        this.context = context;
        this.mReservations = mReservations;
    }

    @NonNull
    @Override
    public ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);

        return new ReservationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReservationHolder holder, final int position) {

        final String reservationUserId = mReservations.get(position).getReservationUserId();
        final String chargePointId = mReservations.get(position).getChargePointId();
        final String connectorId = mReservations.get(position).getConnectorId();
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("users").document(reservationUserId);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null){
                    if(documentSnapshot.exists()){
                        final String usernameReservation = documentSnapshot.getString("username");
                        final String carModel = documentSnapshot.getString("carModel");
                        final String description = documentSnapshot.getString("description");

                        holder.usernameReservation.setText(usernameReservation);

                        long date = mReservations.get(position).getDate();

                        Calendar calendarDate = Calendar.getInstance();
                        calendarDate.setTimeInMillis(date);

                        int day = calendarDate.get(Calendar.DAY_OF_MONTH);
                        int month = calendarDate.get(Calendar.MONTH);
                        int year = calendarDate.get(Calendar.YEAR);

                        long timeFrom = mReservations.get(position).getTimeFrom();

                        Calendar calendarTimeFrom = Calendar.getInstance();
                        calendarTimeFrom.setTimeInMillis(timeFrom);

                        int hourTimeFrom = calendarTimeFrom.get(Calendar.HOUR_OF_DAY);
                        int minuteTimeFrom = calendarTimeFrom.get(Calendar.MINUTE);

                        long timeTo = mReservations.get(position).getTimeTo();

                        Calendar calendarTimeTo = Calendar.getInstance();
                        calendarTimeTo.setTimeInMillis(timeTo);

                        int hourTimeTo = calendarTimeTo.get(Calendar.HOUR_OF_DAY);
                        int minuteTimeTo = calendarTimeTo.get(Calendar.MINUTE);

                        String monthString = Integer.toString(month);
                        String hourTimeFromString = Integer.toString(hourTimeFrom);
                        String minuteTimeFromString = Integer.toString(minuteTimeFrom);
                        String hourTimeToString = Integer.toString(hourTimeTo);
                        String minuteTimeToString = Integer.toString(minuteTimeTo);

                        if(month < 10){
                            monthString = "0" + month;
                        }else{
                            monthString = Integer.toString(month);
                        }

                        if(hourTimeFrom < 10){
                            hourTimeFromString = "0" + hourTimeFrom;
                        }else{
                            hourTimeFromString = Integer.toString(hourTimeFrom);
                        }

                        if(minuteTimeFrom < 10){
                            minuteTimeFromString = "0" + minuteTimeFrom;
                        }else{
                            minuteTimeFromString = Integer.toString(minuteTimeFrom);
                        }

                        if(hourTimeTo < 10){
                            hourTimeToString = "0" + hourTimeTo;
                        }else{
                            hourTimeToString = Integer.toString(hourTimeTo);
                        }

                        if(minuteTimeTo < 10){
                            minuteTimeToString = "0" + minuteTimeTo;
                        }else{
                            minuteTimeToString = Integer.toString(minuteTimeTo);
                        }

                        holder.dateReservation.setText(day + "/" + monthString + "/" + year + " " +
                                hourTimeFromString + ":" + minuteTimeFromString + " - " + hourTimeToString + ":" + minuteTimeToString);

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                        StorageReference profileRef = storageReference.child("users/"+reservationUserId+"/profile.jpg");
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context).load(uri).into(holder.imageReservation);
                            }
                        });

                        holder.imageReservation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(v.getContext(), UserInfoActivity.class);
                                i.putExtra("userId", reservationUserId);
                                i.putExtra("username", usernameReservation);
                                i.putExtra("carModel", carModel);
                                i.putExtra("description", description);
                                context.startActivity(i);
                            }
                        });

                        if(reservationUserId.equals(currentUserId)){
                            holder.deleteReservationBtn.setVisibility(View.VISIBLE);

                        }

                    }
                }
            }
        });

        holder.deleteReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder reservationConfirmationDialog = new AlertDialog.Builder(v.getContext());
                reservationConfirmationDialog.setTitle("Are you sure you want to delete the reservation?");

                reservationConfirmationDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");
                        Query checkChargePoint = databaseReference.orderByChild("id").equalTo(chargePointId);

                        checkChargePoint.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {

                                        ConnectorHelperClass connector = ds.child("connectors").child(connectorId).getValue(ConnectorHelperClass.class);

                                        final ArrayList<ReservationUserHelperClass> reservations = connector.getReservations();


                                        for (int i = 0; i < reservations.size(); i++) {
                                            if (reservations.get(i).getReservationUserId().equals(currentUserId)
                                                    && reservations.get(i).getReservationDate() == mReservations.get(position).getDate()) {

                                                reservations.remove(reservations.get(i));
                                            }

                                        }

                                        connector.setReservations(reservations);

                                        databaseReference.child(chargePointId).child("connectors").child(connectorId).setValue(connector);

                                        mReservations.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, mReservations.size());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

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

    }

    @Override
    public int getItemCount() {
        return mReservations.size();
    }
}
