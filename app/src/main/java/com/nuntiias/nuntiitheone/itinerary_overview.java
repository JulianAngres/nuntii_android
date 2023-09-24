package com.nuntiias.nuntiitheone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class itinerary_overview extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btn_addConnection;
    private Button btn_deleteItinerary;
    private Button btn_confirmItinerary;

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
        setContentView(R.layout.activity_itinerary_overview);

        //Intent intent = getIntent();
        //RecyclerAdapter parcelable = intent.getParcelableExtra("Example Item");
        ArrayList<RecyclerAdapter> recyclerList = new ArrayList<>();
        //recyclerList.add(parcelable);

        Intent intent = getIntent();
        int length = intent.getIntExtra("Length", 99);


        for (int i = 0; i < length; i++) {
            RecyclerAdapter parcelable = intent.getParcelableExtra("Flight no. " + String.valueOf(i));
            recyclerList.add(parcelable);
        }




        btn_addConnection = findViewById(R.id.btn_addConnection);
        btn_deleteItinerary = findViewById(R.id.btn_deleteItinerary);
        btn_confirmItinerary = findViewById(R.id.btn_confirmItinerary);
        mRecyclerView = findViewById(R.id.rv_flightInformation);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(itinerary_overview.this);
        mAdapter = new FlightDataAdapter(recyclerList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        btn_addConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itinerary_overview.this, AddItineraryActivity.class);

                for (int i = 0; i < recyclerList.size(); i++) {
                    intent.putExtra("Flight no. " + String.valueOf(i), recyclerList.get(i));
                }
                intent.putExtra("Length", recyclerList.size());
                startActivity(intent);
                finish();
            }
        });

        btn_deleteItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(itinerary_overview.this);
                builder.setTitle("Itinerary Deletion");
                builder.setMessage("Are you sure you want to delete this itinerary?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(itinerary_overview.this, AddItineraryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });

        btn_confirmItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("proposedItinerariesCount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int id = snapshot.getValue(Integer.class) + 1;
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        String newEmail = email.replace(".", "__DOT__");
                        String date = recyclerList.get(0).getRv_dateOrigin();
                        //String date = "2021-10-09";

                        FirebaseDatabase.getInstance().getReference().child("proposedItinerariesCount").setValue(id);

                        for (int i = 0; i < recyclerList.size(); i++) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("iataOrigin", recyclerList.get(i).getRv_iataOrigin());
                            map.put("airportOrigin", recyclerList.get(i).getRv_airportOrigin());
                            map.put("dateOrigin", recyclerList.get(i).getRv_dateOrigin());
                            map.put("timeOrigin", recyclerList.get(i).getRv_timeOrigin());
                            map.put("iataDestination", recyclerList.get(i).getRv_iataDestination());
                            map.put("airportDestination", recyclerList.get(i).getRv_airportDestination());
                            map.put("dateDestination", recyclerList.get(i).getRv_dateDestination());
                            map.put("timeDestination", recyclerList.get(i).getRv_timeDestination());
                            map.put("flightNumber", recyclerList.get(i).getRv_flightNumber());
                            map.put("originLat", recyclerList.get(i).getRv_originLat());
                            map.put("originLon", recyclerList.get(i).getRv_originLon());
                            map.put("destinationLat", recyclerList.get(i).getRv_destinationLat());
                            map.put("destinationLon", recyclerList.get(i).getRv_destinationLon());

                            FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(date).child(String.valueOf(id)).child("legs").child(String.valueOf(i)).updateChildren(map);
                            FirebaseDatabase.getInstance().getReference().child("allItineraries").child(date).child(String.valueOf(id)).child("legs").child(String.valueOf(i)).updateChildren(map);
                            FirebaseDatabase.getInstance().getReference().child("userData").child(newEmail).child("proposedItineraries").child(date).child(String.valueOf(id)).child(String.valueOf(i)).updateChildren(map);
                        }
                        FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(date).child(String.valueOf(id)).child("extra").child("userEmail").setValue(newEmail);
                        FirebaseDatabase.getInstance().getReference().child("allItineraries").child(date).child(String.valueOf(id)).child("extra").child("userEmail").setValue(newEmail);

                        FirebaseDatabase.getInstance().getReference().child("userData").child(newEmail).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(date).child(String.valueOf(id)).child("extra").child("fullName").setValue(snapshot.getValue().toString());
                                FirebaseDatabase.getInstance().getReference().child("allItineraries").child(date).child(String.valueOf(id)).child("extra").child("fullName").setValue(snapshot.getValue().toString());

                                Toast.makeText(itinerary_overview.this, "Your itinerary has been added successfully!", Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(itinerary_overview.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(itinerary_overview.this, "I'm sorry, something went wrong )-:", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

}