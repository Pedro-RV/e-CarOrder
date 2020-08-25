package com.example.e_carorder.chats.usersRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;
import com.example.e_carorder.chats.MessageActivity;

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
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        final UserModel user = mUsers.get(position);

        holder.name.setText(mUsers.get(position).getName());
        holder.imageUser.setImageResource(mUsers.get(position).getImageUser());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("userId", user.getId());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
