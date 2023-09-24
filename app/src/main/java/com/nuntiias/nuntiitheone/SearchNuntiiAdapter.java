package com.nuntiias.nuntiitheone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchNuntiiAdapter extends RecyclerView.Adapter<SearchNuntiiAdapter.SearchNuntiiViewHolder> {
    private ArrayList<SearchNuntiiItem> mSearchNuntiiList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class SearchNuntiiViewHolder extends RecyclerView.ViewHolder {
        public TextView rv_iataOriginView;
        public TextView rv_iataDestinationView;
        public TextView rv_dateDestinationView;
        public TextView rv_timeDestinationView;
        public TextView rv_airportDestinationView;
        public TextView rv_dateOriginView;
        public TextView rv_timeOriginView;
        public TextView rv_airportOriginView;
        public TextView rv_nuntiusView;
        public TextView rv_priceView;

        public SearchNuntiiViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            rv_iataOriginView = itemView.findViewById(R.id.RV_iataOrigin);
            rv_iataDestinationView = itemView.findViewById(R.id.RV_iataDestination);
            rv_dateDestinationView = itemView.findViewById(R.id.RV_dateDestination);
            rv_timeDestinationView = itemView.findViewById(R.id.RV_timeDestination);
            rv_airportDestinationView = itemView.findViewById(R.id.RV_airportDestination);
            rv_dateOriginView = itemView.findViewById(R.id.RV_dateOrigin);
            rv_timeOriginView = itemView.findViewById(R.id.RV_timeOrigin);
            rv_airportOriginView = itemView.findViewById(R.id.RV_airportOrigin);
            rv_nuntiusView = itemView.findViewById(R.id.RV_nuntius);
            rv_priceView = itemView.findViewById(R.id.RV_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public SearchNuntiiAdapter(ArrayList<SearchNuntiiItem> searchNuntiiList) {
        mSearchNuntiiList = searchNuntiiList;
    }

    @NonNull
    @Override
    public SearchNuntiiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_nuntii_item, parent, false);
        SearchNuntiiViewHolder snvh = new SearchNuntiiViewHolder(v, mListener);
        return snvh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchNuntiiAdapter.SearchNuntiiViewHolder holder, int position) {
        SearchNuntiiItem currentItem = mSearchNuntiiList.get(position);

        holder.rv_iataOriginView.setText(currentItem.getRv_iataOrigin());
        holder.rv_iataDestinationView.setText(currentItem.getRv_iataDestination());
        holder.rv_dateDestinationView.setText(currentItem.getRv_dateDestination());
        holder.rv_timeDestinationView.setText(currentItem.getRv_timeDestination());
        holder.rv_airportDestinationView.setText(currentItem.getRv_airportDestination());
        holder.rv_dateOriginView.setText(currentItem.getRv_dateOrigin());
        holder.rv_timeOriginView.setText(currentItem.getRv_timeOrigin());
        holder.rv_airportOriginView.setText(currentItem.getRv_airportOrigin());
        holder.rv_nuntiusView.setText(currentItem.getRv_nuntius());
        holder.rv_priceView.setText(currentItem.getRv_price());
    }

    @Override
    public int getItemCount() {
        return mSearchNuntiiList.size();
    }
}
