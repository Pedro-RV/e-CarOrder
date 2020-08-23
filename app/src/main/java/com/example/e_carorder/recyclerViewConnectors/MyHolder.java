package com.example.e_carorder.recyclerViewConnectors;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;

public class MyHolder extends RecyclerView.ViewHolder {

    ImageView imageConnector;
    TextView connectorType, connectorPowerKW;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.imageConnector = itemView.findViewById(R.id.imageConnector);
        this.connectorType = itemView.findViewById(R.id.connectorType);
        this.connectorPowerKW = itemView.findViewById(R.id.connectorPowerKW);

    }
}
