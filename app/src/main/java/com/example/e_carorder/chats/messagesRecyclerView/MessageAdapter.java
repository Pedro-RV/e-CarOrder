package com.example.e_carorder.chats.messagesRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;
import com.example.e_carorder.chats.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {

    private Context context;
    private ArrayList<ChatModel> mChats;
    private String imgURL;

    FirebaseUser fuser;

    public static final int MSG_TYPE_RECEIVED = 0;
    public static final int MSG_TYPE_SENDED = 1;

    public MessageAdapter(Context context, ArrayList<ChatModel> mChats, String imgURL) {
        this.context = context;
        this.mChats = mChats;
        this.imgURL = imgURL;
    }


    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_SENDED){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_layout, null);

            return new MessageHolder(view);

        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_layout, null);

            return new MessageHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {

        ChatModel chat = mChats.get(position);

        holder.showMessage.setText(chat.getMessage());

        holder.profileImage.setImageResource(R.mipmap.ic_launcher);

        if(chat.getStatus() == true){
            holder.statusImg.setImageResource(R.drawable.double_tick);
        }


    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_SENDED;
        }else{
            return MSG_TYPE_RECEIVED;
        }
    }
}
