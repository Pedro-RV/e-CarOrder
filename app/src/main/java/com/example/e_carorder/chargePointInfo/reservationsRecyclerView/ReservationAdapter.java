package com.example.e_carorder.chargePointInfo.reservationsRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_carorder.R;
import com.example.e_carorder.chargePointInfo.UserInfoActivity;
import com.example.e_carorder.chats.usersRecyclerView.UserHolder;
import com.example.e_carorder.chats.usersRecyclerView.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, null);

        return new ReservationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReservationHolder holder, final int position) {

        final String reservationUserId = mReservations.get(position).getReservationUserId();

        final DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("users").document(reservationUserId);


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null){
                    if(documentSnapshot.exists()){
                        final String usernameUsingConnector = documentSnapshot.getString("username");

                        holder.usernameReservation.setText(usernameUsingConnector);

                        holder.dateReservation.setText("Reservation date: " + mReservations.get(position).getDate());

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
                                i.putExtra("userUsingConnectorId", reservationUserId);
                                i.putExtra("usernameUsingConnector", usernameUsingConnector);
                                context.startActivity(i);
                            }
                        });

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReservations.size();
    }
}
