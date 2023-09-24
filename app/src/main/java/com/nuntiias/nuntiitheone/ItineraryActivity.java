package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

import java.util.ArrayList;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class ItineraryActivity extends AppCompatActivity {

    private RecyclerView rv_proposedItineraries;
    private ProposedItineraryAdapter mAdapter;
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
        setContentView(R.layout.activity_itinerary);



        ArrayList<ProposedItineraryItem> proposedItineraryList = new ArrayList<>();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String newEmail = email.replace(".", "__DOT__");

        ArrayList<String> datesOrigin = new ArrayList<>();
        ArrayList<String> iatasOrigin = new ArrayList<>();
        ArrayList<String> iatasDestination = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("userData").child(newEmail).child("proposedItineraries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                datesOrigin.clear();
                iatasOrigin.clear();
                iatasDestination.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key = snapshot1.getKey();
                        Log.d("key", key);
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            datesOrigin.add(snapshot2.child("dateOrigin").getValue().toString());
                            iatasOrigin.add(snapshot2.child("iataOrigin").getValue().toString());
                            iatasDestination.add(snapshot2.child("iataDestination").getValue().toString());
                        }

                        if (DateTimeComparator.NoPastMatches(datesOrigin.get(0))) {
                            proposedItineraryList.add(new ProposedItineraryItem(datesOrigin.get(0), iatasOrigin.get(0), iatasDestination.get(iatasDestination.size() - 1), key));
                        }

                        datesOrigin.clear();
                        iatasOrigin.clear();
                        iatasDestination.clear();

                    }
                }
                rv_proposedItineraries = findViewById(R.id.rv_proposedItineraries);
                mLayoutManager = new LinearLayoutManager(ItineraryActivity.this);
                mAdapter = new ProposedItineraryAdapter(proposedItineraryList);
                rv_proposedItineraries.setLayoutManager(mLayoutManager);
                rv_proposedItineraries.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new ProposedItineraryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        //Toast.makeText(ItineraryActivity.this, proposedItineraryList.get(position).getRv_id(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ItineraryActivity.this, item_itinerary_overview.class);
                        intent.putExtra("Id", proposedItineraryList.get(position).getRv_id());
                        intent.putExtra("Date", proposedItineraryList.get(position).getRv_dateItinerary());
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(ItineraryActivity.this, "I'm sorry, something went wrong )-:", Toast.LENGTH_SHORT).show();
            }
        });
    }
}