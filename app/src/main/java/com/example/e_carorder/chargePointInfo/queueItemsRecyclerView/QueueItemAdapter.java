package com.example.e_carorder.chargePointInfo.queueItemsRecyclerView;

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
import com.example.e_carorder.chargePointInfo.reservationsRecyclerView.ReservationHolder;
import com.example.e_carorder.chargePointInfo.reservationsRecyclerView.ReservationModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class QueueItemAdapter extends RecyclerView.Adapter<QueueItemHolder> {

    private Context context;
    private ArrayList<QueueItemModel> mQueueItems;

    public QueueItemAdapter(Context context, ArrayList<QueueItemModel> mQueueItems) {
        this.context = context;
        this.mQueueItems = mQueueItems;
    }

    @NonNull
    @Override
    public QueueItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item, parent, false);

        return new QueueItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QueueItemHolder holder, final int position) {

        final String queueItemUserId = mQueueItems.get(position).getQueueItemUserId();

        final DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("users").document(queueItemUserId);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null){
                    if(documentSnapshot.exists()){
                        final String usernameQueueItem = documentSnapshot.getString("username");

                        holder.usernameQueueItem.setText(usernameQueueItem);

                        holder.positionQueueItem.setText("Position: " + (position + 1));

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                        StorageReference profileRef = storageReference.child("users/"+queueItemUserId+"/profile.jpg");
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context).load(uri).into(holder.imageQueueItem);
                            }
                        });

                        holder.imageQueueItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(v.getContext(), UserInfoActivity.class);
                                i.putExtra("userUsingConnectorId", queueItemUserId);
                                i.putExtra("usernameUsingConnector", usernameQueueItem);
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
        return mQueueItems.size();
    }
}
