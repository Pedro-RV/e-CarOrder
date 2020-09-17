package com.example.e_carorder.chats.usersRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;

public class UserHolder extends RecyclerView.ViewHolder {

    TextView name, newMessageTV;
    ImageView imageUser;

    public UserHolder(@NonNull View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.tvUser);
        this.imageUser = itemView.findViewById(R.id.ivUser);
        this.newMessageTV = itemView.findViewById(R.id.newMessageTV);

    }
}
