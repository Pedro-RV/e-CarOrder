package com.example.e_carorder.chargePointInfo.queueItemsRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;

public class QueueItemHolder extends RecyclerView.ViewHolder {
    TextView usernameQueueItem, positionQueueItem;
    ImageView imageQueueItem;


    public QueueItemHolder(@NonNull View itemView) {
        super(itemView);

        this.usernameQueueItem = itemView.findViewById(R.id.usernameQueueItem);
        this.positionQueueItem = itemView.findViewById(R.id.positionQueueItem);
        this.imageQueueItem = itemView.findViewById(R.id.imageQueueItem);

    }
}
