package com.nuntiias.nuntiitheone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatLeftAdapter extends RecyclerView.Adapter<ChatLeftAdapter.ChatLeftViewHolder> {
    private ArrayList<ChatItemLeft> mChatLeftList;

    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;

    @Override
    public int getItemViewType(int position) {

        ChatItemLeft currentItem = mChatLeftList.get(position);

        if (currentItem.getRv_role().equals(currentItem.getRv_ownRole())) {
            return LAYOUT_ONE;
        } else {
            return LAYOUT_TWO;
        }

    }

    public static class ChatLeftViewHolder extends RecyclerView.ViewHolder {
        public TextView rv_userInfoLeft;
        public TextView rv_textLeft;
        public TextView rv_timestampLeft;

        public ChatLeftViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_userInfoLeft = itemView.findViewById(R.id.rv_userInfoLeft);
            rv_textLeft = itemView.findViewById(R.id.rv_textLeft);
            rv_timestampLeft = itemView.findViewById(R.id.rv_timestampLeft);
        }
    }

    public ChatLeftAdapter(ArrayList<ChatItemLeft> chatLeftList) {
        mChatLeftList = chatLeftList;
    }

    @NonNull
    @Override
    public ChatLeftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = null;
        //RecyclerView.ViewHolder = null;

        if (viewType == LAYOUT_ONE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            ChatLeftViewHolder clvh = new ChatLeftViewHolder(v);
            return clvh;
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            ChatLeftViewHolder clvh = new ChatLeftViewHolder(v);
            return clvh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatLeftAdapter.ChatLeftViewHolder holder, int position) {
        ChatItemLeft currentItem = mChatLeftList.get(position);

        holder.rv_userInfoLeft.setText(currentItem.getRv_userInfoLeft());
        holder.rv_textLeft.setText(currentItem.getRv_textLeft());
        //holder.rv_timestampLeft.setText(currentItem.getRv_timestampLeft());
        holder.rv_timestampLeft.setText("");
    }

    @Override
    public int getItemCount() {
        return mChatLeftList.size();
    }
}
