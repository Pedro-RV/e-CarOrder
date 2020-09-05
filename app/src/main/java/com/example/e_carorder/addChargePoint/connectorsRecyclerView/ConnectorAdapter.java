package com.example.e_carorder.addChargePoint.connectorsRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_carorder.R;

import java.util.ArrayList;

public class ConnectorAdapter extends RecyclerView.Adapter<ConnectorHolder> {

    Context c;
    ArrayList<ConnectorModel> connectorModels;

    public ConnectorAdapter(Context c, ArrayList<ConnectorModel> connectorModels) {
        this.c = c;
        this.connectorModels = connectorModels;
    }

    @NonNull
    @Override
    public ConnectorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connector_item, null);

        return new ConnectorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectorHolder holder, int position) {

        holder.connectorType.setText(connectorModels.get(position).getConnectorType());
        holder.connectorPowerKW.setText(connectorModels.get(position).getConnectorPowerKW());
        holder.imageConnector.setImageResource(connectorModels.get(position).getImageConnector());

        if(connectorModels.get(position).getActivateBtn()){
            holder.checkInBtn.setVisibility(View.VISIBLE);

        } else{
            android.view.ViewGroup.LayoutParams connectorTypeParams = holder.connectorType.getLayoutParams();
            connectorTypeParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.connectorType.setLayoutParams(connectorTypeParams);

            android.view.ViewGroup.LayoutParams connectorPowerKWParams = holder.connectorPowerKW.getLayoutParams();
            connectorPowerKWParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.connectorPowerKW.setLayoutParams(connectorPowerKWParams);

        }

    }

    @Override
    public int getItemCount() {
        return connectorModels.size();
    }
}