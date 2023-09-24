package com.nuntiias.nuntiitheone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NuntiiMatchesAdapter extends RecyclerView.Adapter<NuntiiMatchesAdapter.NuntiiMatchesViewHolder> {
    private ArrayList<NuntiiMatchesItem> mNuntiiMatchesList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class NuntiiMatchesViewHolder extends RecyclerView.ViewHolder {
        public TextView rv_date;
        public TextView rv_matchId;
        public TextView rv_itineraryId;
        public TextView rv_nuntiusFullName;
        public TextView rv_nuntiusId;
        public TextView rv_parcelDescription;
        public TextView rv_parcelSize;
        public TextView rv_price;
        public TextView rv_receiverFullName;
        public TextView rv_receiverId;
        public TextView rv_role;
        public TextView rv_senderFullName;
        public TextView rv_senderId;
        public TextView rv_itineraryOrigin;
        public TextView rv_itineraryDestination;

        public NuntiiMatchesViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            rv_date = itemView.findViewById(R.id.rv_date);
            rv_matchId = itemView.findViewById(R.id.rv_matchId);
            rv_itineraryId = itemView.findViewById(R.id.rv_itineraryId);
            rv_nuntiusFullName = itemView.findViewById(R.id.rv_nuntiusFullName);
            rv_nuntiusId = itemView.findViewById(R.id.rv_nuntiusId);
            rv_parcelDescription = itemView.findViewById(R.id.rv_parcelDescription);
            rv_parcelSize = itemView.findViewById(R.id.rv_parcelSize);
            rv_price = itemView.findViewById(R.id.rv_price);
            rv_receiverFullName = itemView.findViewById(R.id.rv_receiverFullName);
            rv_receiverId = itemView.findViewById(R.id.rv_receiverId);
            rv_role = itemView.findViewById(R.id.rv_role);
            rv_senderFullName = itemView.findViewById(R.id.rv_senderFullName);
            rv_senderId = itemView.findViewById(R.id.rv_senderId);
            rv_itineraryOrigin = itemView.findViewById(R.id.rv_itineraryOrigin);
            rv_itineraryDestination = itemView.findViewById(R.id.rv_itineraryDestination);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    public NuntiiMatchesAdapter(ArrayList<NuntiiMatchesItem> nuntiiMatchesList) {
        mNuntiiMatchesList = nuntiiMatchesList;
    }

    @NonNull
    @Override
    public NuntiiMatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nuntii_matches_item, parent, false);
        NuntiiMatchesViewHolder nmvh = new NuntiiMatchesViewHolder(v, mListener);
        return nmvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NuntiiMatchesAdapter.NuntiiMatchesViewHolder holder, int position) {
        NuntiiMatchesItem currentItem = mNuntiiMatchesList.get(position);

        holder.rv_date.setText(currentItem.getRv_date());
        holder.rv_matchId.setText(currentItem.getRv_matchId());
        holder.rv_itineraryId.setText(currentItem.getRv_itineraryId());
        holder.rv_nuntiusFullName.setText(currentItem.getRv_nuntiusFullName());
        holder.rv_nuntiusId.setText(currentItem.getRv_nuntiusId());
        holder.rv_parcelDescription.setText(currentItem.getRv_parcelDescription());
        holder.rv_parcelSize.setText(currentItem.getRv_parcelSize());
        holder.rv_price.setText(currentItem.getRv_price());
        holder.rv_receiverFullName.setText(currentItem.getRv_receiverFullName());
        holder.rv_receiverId.setText(currentItem.getRv_receiverId());
        holder.rv_role.setText(currentItem.getRv_role());
        holder.rv_senderFullName.setText(currentItem.getRv_senderFullName());
        holder.rv_senderId.setText(currentItem.getRv_senderId());
        holder.rv_itineraryOrigin.setText(currentItem.getRv_itineraryOrigin());
        holder.rv_itineraryDestination.setText(currentItem.getRv_itineraryDestination());
    }

    @Override
    public int getItemCount() {
        return mNuntiiMatchesList.size();
    }
}
