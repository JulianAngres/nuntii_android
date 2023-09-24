package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class item_nuntii_matches_itinerary_data extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
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
        setContentView(R.layout.activity_item_nuntii_matches_itinerary_data);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("itineraryId");
        String date = intent.getExtras().getString("date");

        ArrayList<RecyclerAdapter> recyclerList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("allItineraries").child(date).child(id).child("legs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    recyclerList.add(new RecyclerAdapter(item_nuntii_matches_itinerary_data.this, snapshot.child("iataOrigin").getValue().toString(), snapshot.child("iataDestination").getValue().toString(), snapshot.child("dateDestination").getValue().toString(), snapshot.child("timeDestination").getValue().toString(), snapshot.child("airportDestination").getValue().toString(), snapshot.child("dateOrigin").getValue().toString(), snapshot.child("timeOrigin").getValue().toString(), snapshot.child("airportOrigin").getValue().toString(), snapshot.child("flightNumber").getValue().toString(), snapshot.child("originLat").getValue().toString(), snapshot.child("originLon").getValue().toString(), snapshot.child("destinationLat").getValue().toString(), snapshot.child("destinationLon").getValue().toString()));

                }

                mRecyclerView = findViewById(R.id.rv_nuntiiMatchesItineraryInformation);
                mLayoutManager = new LinearLayoutManager(item_nuntii_matches_itinerary_data.this);
                mAdapter = new FlightDataAdapter(recyclerList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(item_nuntii_matches_itinerary_data.this, "I'm sorry, something went wrong )-:", Toast.LENGTH_SHORT).show();
            }
        });
    }
}