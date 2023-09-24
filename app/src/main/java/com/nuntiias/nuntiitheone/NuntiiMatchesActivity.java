package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class NuntiiMatchesActivity extends AppCompatActivity {
    private RecyclerView rv_nuntiiMatches;
    private NuntiiMatchesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuntii_matches);

        String ownEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String ownNewEmail = ownEmail.replace(".", "__DOT__");
        ArrayList<NuntiiMatchesItem> nuntiiMatchesList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("userData").child(ownNewEmail).child("nuntiiMatches").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot snapshot1: snapshot.getChildren()) {
                        String matchId = snapshot1.getKey();
                        String date = snapshot1.child("date").getValue().toString();
                        String itineraryId = snapshot1.child("itineraryId").getValue().toString();
                        String nuntiusFullName = snapshot1.child("nuntiusFullName").getValue().toString();
                        String nuntiusId = snapshot1.child("nuntiusId").getValue().toString();
                        String parcelDescription = snapshot1.child("parcelDescription").getValue().toString();
                        String parcelSize = snapshot1.child("parcelSize").getValue().toString();
                        String price = snapshot1.child("price").getValue().toString();
                        String receiverFullName = snapshot1.child("receiverFullName").getValue().toString();
                        String receiverId = snapshot1.child("receiverId").getValue().toString();
                        String role = snapshot1.child("role").getValue().toString();
                        String senderFullName = snapshot1.child("senderFullName").getValue().toString();
                        String senderId = snapshot1.child("senderId").getValue().toString();
                        String itineraryOrigin = snapshot1.child("itineraryOrigin").getValue().toString();
                        String itineraryDestination = snapshot1.child("itineraryDestination").getValue().toString();

                        if (DateTimeComparator.NoPastMatches(date) == true) {
                            nuntiiMatchesList.add(new NuntiiMatchesItem(date, matchId, itineraryId, nuntiusFullName, nuntiusId, parcelDescription, parcelSize, price, receiverFullName, receiverId, role, senderFullName, senderId, itineraryOrigin, itineraryDestination));
                        }
                    }
                }

                rv_nuntiiMatches = findViewById(R.id.rv_nuntiiMatches);
                mLayoutManager = new LinearLayoutManager(NuntiiMatchesActivity.this);
                mAdapter = new NuntiiMatchesAdapter(nuntiiMatchesList);

                rv_nuntiiMatches.setLayoutManager(mLayoutManager);
                rv_nuntiiMatches.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new NuntiiMatchesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(NuntiiMatchesActivity.this, item_nuntii_matches.class);
                        intent.putExtra("matchId", nuntiiMatchesList.get(position).getRv_matchId());
                        intent.putExtra("date", nuntiiMatchesList.get(position).getRv_date());
                        intent.putExtra("itineraryId", nuntiiMatchesList.get(position).getRv_itineraryId());
                        intent.putExtra("nuntiusFullName", nuntiiMatchesList.get(position).getRv_nuntiusFullName());
                        intent.putExtra("nuntiusId", nuntiiMatchesList.get(position).getRv_nuntiusId());
                        intent.putExtra("parcelDescription", nuntiiMatchesList.get(position).getRv_parcelDescription());
                        intent.putExtra("parcelSize", nuntiiMatchesList.get(position).getRv_parcelSize());
                        intent.putExtra("price", nuntiiMatchesList.get(position).getRv_price());
                        intent.putExtra("receiverFullName", nuntiiMatchesList.get(position).getRv_receiverFullName());
                        intent.putExtra("receiverId", nuntiiMatchesList.get(position).getRv_receiverId());
                        intent.putExtra("role", nuntiiMatchesList.get(position).getRv_role());
                        intent.putExtra("senderFullName", nuntiiMatchesList.get(position).getRv_senderFullName());
                        intent.putExtra("senderId", nuntiiMatchesList.get(position).getRv_senderId());
                        intent.putExtra("itineraryOrigin", nuntiiMatchesList.get(position).getRv_itineraryOrigin());
                        intent.putExtra("itineraryDestination", nuntiiMatchesList.get(position).getRv_itineraryDestination());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getOriginData(String date, String id) {

        String origin = "origin";

        FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(date).child(id).child("legs").child("0").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String origin = dataSnapshot.child("iataOrigin").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return origin;

    }

}