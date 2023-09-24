package com.nuntiias.nuntiitheone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FlightDataAdapter extends RecyclerView.Adapter<FlightDataAdapter.FlightDataViewHolder> {

    private ArrayList<RecyclerAdapter> mFlightDataList;

    public static class FlightDataViewHolder extends RecyclerView.ViewHolder {
        public TextView rv_iataOriginView;
        public TextView rv_iataDestinationView;
        public TextView rv_dateDestinationView;
        public TextView rv_timeDestinationView;
        public TextView rv_airportDestinationView;
        public TextView rv_dateOriginView;
        public TextView rv_timeOriginView;
        public TextView rv_airportOriginView;
        public TextView rv_flightNumberView;

        public FlightDataViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_iataOriginView = itemView.findViewById(R.id.RV_iataOrigin);
            rv_iataDestinationView = itemView.findViewById(R.id.RV_iataDestination);
            rv_dateDestinationView = itemView.findViewById(R.id.RV_dateDestination);
            rv_timeDestinationView = itemView.findViewById(R.id.RV_timeDestination);
            rv_airportDestinationView = itemView.findViewById(R.id.RV_airportDestination);
            rv_dateOriginView = itemView.findViewById(R.id.RV_dateOrigin);
            rv_timeOriginView = itemView.findViewById(R.id.RV_timeOrigin);
            rv_airportOriginView = itemView.findViewById(R.id.RV_airportOrigin);
            rv_flightNumberView = itemView.findViewById(R.id.RV_flightNumber);
        }
    }

    public FlightDataAdapter(ArrayList<RecyclerAdapter> flightDataList) {
        mFlightDataList = flightDataList;
    }

    @NonNull
    @Override
    public FlightDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_nuntii_item, parent, false);
        FlightDataViewHolder evh = new FlightDataViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull FlightDataViewHolder holder, int position) {
        RecyclerAdapter currentItem = mFlightDataList.get(position);

        holder.rv_iataOriginView.setText(currentItem.getRv_iataOrigin());
        holder.rv_iataDestinationView.setText(currentItem.getRv_iataDestination());
        holder.rv_dateDestinationView.setText(currentItem.getRv_dateDestination());
        holder.rv_timeDestinationView.setText(currentItem.getRv_timeDestination());
        holder.rv_airportDestinationView.setText(currentItem.getRv_airportDestination());
        holder.rv_dateOriginView.setText(currentItem.getRv_dateOrigin());
        holder.rv_timeOriginView.setText(currentItem.getRv_timeOrigin());
        holder.rv_airportOriginView.setText(currentItem.getRv_airportOrigin());
        holder.rv_flightNumberView.setText(currentItem.getRv_flightNumber());
    }

    @Override
    public int getItemCount() {
        return mFlightDataList.size();
    }
}
