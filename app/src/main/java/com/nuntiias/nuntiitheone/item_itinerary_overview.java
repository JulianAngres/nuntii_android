package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class item_itinerary_overview extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button btn_deleteItinerary;

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
        setContentView(R.layout.activity_item_itinerary_overview);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("Id");
        String date = intent.getExtras().getString("Date");

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String newEmail = email.replace(".", "__DOT__");

        ArrayList<RecyclerAdapter> recyclerList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("userData").child(newEmail).child("proposedItineraries").child(date).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    recyclerList.add(new RecyclerAdapter(item_itinerary_overview.this, snapshot.child("iataOrigin").getValue().toString(), snapshot.child("iataDestination").getValue().toString(), snapshot.child("dateDestination").getValue().toString(), snapshot.child("timeDestination").getValue().toString(), snapshot.child("airportDestination").getValue().toString(), snapshot.child("dateOrigin").getValue().toString(), snapshot.child("timeOrigin").getValue().toString(), snapshot.child("airportOrigin").getValue().toString(), snapshot.child("flightNumber").getValue().toString(), snapshot.child("originLat").getValue().toString(), snapshot.child("originLon").getValue().toString(), snapshot.child("destinationLat").getValue().toString(), snapshot.child("destinationLon").getValue().toString()));

                }

                mRecyclerView = findViewById(R.id.rv_flightInformation);
                mLayoutManager = new LinearLayoutManager(item_itinerary_overview.this);
                mAdapter = new FlightDataAdapter(recyclerList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                btn_deleteItinerary = findViewById(R.id.btn_deleteItinerary);

                btn_deleteItinerary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(item_itinerary_overview.this);
                        builder.setTitle("Itinerary Deletion");
                        builder.setMessage("Are you sure you want to delete this itinerary?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("userData").child(newEmail).child("proposedItineraries").child(date).child(id).setValue(null);
                                FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(date).child(id).setValue(null);
                                Toast.makeText(item_itinerary_overview.this, "Itinerary Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(item_itinerary_overview.this, ItineraryActivity.class));
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();



                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(item_itinerary_overview.this, "I'm sorry, something went wrong )-:", Toast.LENGTH_SHORT).show();
            }
        });
    }
}