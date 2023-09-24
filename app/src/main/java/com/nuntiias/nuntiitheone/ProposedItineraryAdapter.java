package com.nuntiias.nuntiitheone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProposedItineraryAdapter extends RecyclerView.Adapter<ProposedItineraryAdapter.ProposedItineraryViewHolder> {
    private ArrayList<ProposedItineraryItem> mProposedItineraryList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ProposedItineraryViewHolder extends RecyclerView.ViewHolder {
        public TextView rv_dateItinerary;
        public TextView rv_itineraryOrigin;
        public TextView rv_itineraryDestination;
        public TextView rv_id;

        public ProposedItineraryViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            rv_dateItinerary = itemView.findViewById(R.id.rv_date);
            rv_itineraryOrigin = itemView.findViewById(R.id.rv_itineraryOrigin);
            rv_itineraryDestination = itemView.findViewById(R.id.rv_itineraryDestination);
            rv_id = itemView.findViewById(R.id.rv_matchId);

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

    public ProposedItineraryAdapter(ArrayList<ProposedItineraryItem> proposedItineraryList) {
        mProposedItineraryList = proposedItineraryList;
    }

    @NonNull
    @Override
    public ProposedItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposed_itineraries_item, parent, false);
        ProposedItineraryViewHolder pivh = new ProposedItineraryViewHolder(v, mListener);
        return pivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProposedItineraryAdapter.ProposedItineraryViewHolder holder, int position) {
        ProposedItineraryItem currentItem = mProposedItineraryList.get(position);

        holder.rv_dateItinerary.setText(currentItem.getRv_dateItinerary());
        holder.rv_itineraryOrigin.setText(currentItem.getRv_itineraryOrigin());
        holder.rv_itineraryDestination.setText(currentItem.getRv_itineraryDestination());
        holder.rv_id.setText(currentItem.getRv_id());
    }

    @Override
    public int getItemCount() {
        return mProposedItineraryList.size();
    }
}
