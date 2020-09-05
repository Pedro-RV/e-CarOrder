package com.example.e_carorder.addChargePoint.connectorsRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;

public class ConnectorHolder extends RecyclerView.ViewHolder {

    ImageView imageConnector;
    TextView connectorType, connectorPowerKW;
    Button checkInBtn, checkOutBtn, inUseBtn;

    public ConnectorHolder(@NonNull View itemView) {
        super(itemView);

        this.imageConnector = itemView.findViewById(R.id.imageConnector);
        this.connectorType = itemView.findViewById(R.id.connectorType);
        this.connectorPowerKW = itemView.findViewById(R.id.connectorPowerKW);
        this.checkInBtn = itemView.findViewById(R.id.checkInBtn);
        this.checkOutBtn = itemView.findViewById(R.id.checkOutBtn);
        this.inUseBtn = itemView.findViewById(R.id.inUseBtn);

    }
}
