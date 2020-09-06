package com.example.e_carorder.chats.messagesRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;

public class MessageHolder extends RecyclerView.ViewHolder {

    TextView showMessage;
    ImageView profileImage, statusImg;

    public MessageHolder(@NonNull View itemView) {
        super(itemView);

        this.showMessage = itemView.findViewById(R.id.showMessage);
        this.profileImage = itemView.findViewById(R.id.profileImage);
        this.statusImg = itemView.findViewById(R.id.statusImg);
    }
}
