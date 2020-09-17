package com.example.e_carorder.chats.usersRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_carorder.R;
import com.example.e_carorder.chats.MessageActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    private Context context;
    private ArrayList<UserModel> mUsers;

    public UserAdapter(Context context, ArrayList<UserModel> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null);

        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {

        holder.name.setText(mUsers.get(position).getName());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+mUsers.get(position).getId()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imageUser);
            }
        });

        if(mUsers.get(position).getNewMessage() == true){
            holder.newMessageTV.setVisibility(View.VISIBLE);
        }else{
            holder.newMessageTV.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("userId", mUsers.get(position).getId());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
