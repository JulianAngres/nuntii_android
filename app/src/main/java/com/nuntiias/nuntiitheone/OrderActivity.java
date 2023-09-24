package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.nuntiias.nuntiitheone.ConnectionDistanceComparator.L_orth;
import static com.nuntiias.nuntiitheone.DateTimeComparator.NoPastFlights;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class OrderActivity extends AppCompatActivity {

    private EditText et_originCity, et_destinationCity;
    private TextView et_givenDate;
    private Button btn_searchNuntii;
    private RecyclerView rv_searchNuntii;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchNuntiiAdapter mAdapter;

    private DatePickerDialog.OnDateSetListener setListener;

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
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        float grundpreis = intent.getExtras().getFloat("grundpreis");
        float kmPreis = grundpreis/1115;

        et_originCity = findViewById(R.id.et_originCity);
        et_destinationCity = findViewById(R.id.et_destinationCity);
        et_givenDate = findViewById(R.id.et_givenDate);
        btn_searchNuntii = findViewById(R.id.btn_searchNuntii);



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        et_givenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        OrderActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month+1;
                String stringMonth = "month";
                String stringDay = "day";

                if (month < 10) {
                    stringMonth = "0" + month;
                } else {
                    stringMonth = String.valueOf(month);
                }

                if (dayOfMonth < 10) {
                    stringDay = "0" + dayOfMonth;
                } else {
                    stringDay = String.valueOf(dayOfMonth);
                }

                String date = year + "-" + stringMonth + "-" + stringDay;
                et_givenDate.setText(date);
            }
        };



        String ownEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String ownNewEmail = ownEmail.replace(".", "__DOT__");

        btn_searchNuntii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressBar progressBar = new ProgressBar(OrderActivity.this, null, android.R.attr.progressBarStyle);
                progressBar.setIndeterminate(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this)
                        .setView(progressBar)
                        .setCancelable(false);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 2000);

                String startingLoc = et_originCity.getText().toString();
                String endingLoc = et_destinationCity.getText().toString();

                String givenDate = et_givenDate.getText().toString();

                if (NoPastFlights(givenDate) == true) {
                    Toast.makeText(OrderActivity.this, "Earliest possible date of booking: tomorrow", Toast.LENGTH_SHORT).show();
                } else {
                    Float startingLat = geolocateLatitude(startingLoc);
                    Float startingLon = geolocateLongitude(startingLoc);
                    Float endingLat = geolocateLatitude(endingLoc);
                    Float endingLon = geolocateLongitude(endingLoc);

                    ArrayList<Double> combinedDistances = new ArrayList<>();
                    ArrayList<Double> sortedCombinedDistances = new ArrayList<>();
                    ArrayList<Double> airDistances = new ArrayList<>();
                    ArrayList<Double> sortedAirDistances = new ArrayList<>();
                    ArrayList<String> ids = new ArrayList<>();
                    ArrayList<String> sortedIds = new ArrayList<>();
                    ArrayList<SearchNuntiiItem> searchNuntiiList = new ArrayList<>();

                    ArrayList<String> originLats = new ArrayList<>();
                    ArrayList<String> originLons = new ArrayList<>();
                    ArrayList<String> destinationLats = new ArrayList<>();
                    ArrayList<String> destinationLons = new ArrayList<>();


                    FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(givenDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                            originLats.clear();
                            originLons.clear();
                            destinationLats.clear();
                            destinationLons.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                Log.d("Guntersblum", snapshot.getKey());

                                for (DataSnapshot snapshot1 : snapshot.child("legs").getChildren()) {

                                    try {
                                        originLats.add(snapshot1.child("originLat").getValue().toString());
                                        originLons.add(snapshot1.child("originLon").getValue().toString());
                                        destinationLats.add(snapshot1.child("destinationLat").getValue().toString());
                                        destinationLons.add(snapshot1.child("destinationLon").getValue().toString());
                                    } catch (Exception e) {
                                    }

                                }
                                int size = originLats.size() - 1;

                                Log.d("Frankenthal", originLats.get(0) + ", " + originLons.get(0) + ", " + destinationLats.get(size) + ", " + destinationLons.get(size));


                                Float oriCooLat = Float.parseFloat(originLats.get(0));
                                Float oriCooLon = Float.parseFloat(originLons.get(0));
                                Float desCooLat = Float.parseFloat(destinationLats.get(size));
                                Float desCooLon = Float.parseFloat(destinationLons.get(size));

                                originLats.clear();
                                originLons.clear();
                                destinationLats.clear();
                                destinationLons.clear();


                                double combinedDistance = L_orth(startingLat, oriCooLat, startingLon, oriCooLon) + L_orth(endingLat, desCooLat, endingLon, desCooLon);
                                double airDistance = L_orth(oriCooLat, desCooLat, oriCooLon, desCooLon);

                                airDistances.add(airDistance);

                                combinedDistances.add(combinedDistance);
                                sortedCombinedDistances.add(combinedDistance);

                                ids.add(snapshot.getKey());

                            }

                            Collections.sort(sortedCombinedDistances);
                            sortedIds.clear();


                            if (combinedDistances.size() == 0) {
                                Toast.makeText(OrderActivity.this, "There are unfortunately no itineraries available on this date.", Toast.LENGTH_SHORT).show();
                            }


                            for (int i = 0; i < combinedDistances.size(); i++) {

                                sortedIds.add(ids.get(combinedDistances.indexOf(sortedCombinedDistances.get(i))));
                                sortedAirDistances.add(airDistances.get(combinedDistances.indexOf(sortedCombinedDistances.get(i))));
                            }

                            ArrayList<String> iatasOrigin = new ArrayList<>();
                            ArrayList<String> iatasDestination = new ArrayList<>();
                            ArrayList<String> datesDestination = new ArrayList<>();
                            ArrayList<String> timesDestination = new ArrayList<>();
                            ArrayList<String> airportsDestination = new ArrayList<>();
                            ArrayList<String> datesOrigin = new ArrayList<>();
                            ArrayList<String> timesOrigin = new ArrayList<>();
                            ArrayList<String> airportsOrigin = new ArrayList<>();



                            searchNuntiiList.clear();
                            for(int i = 0; i < sortedIds.size(); i++) {
                                String j = sortedIds.get(i);
                                double greatCircle = sortedAirDistances.get(i);
                                long price1 = Math.round(kmPreis*greatCircle*0.5 + 250);

                                iatasOrigin.clear();
                                iatasDestination.clear();
                                datesDestination.clear();
                                timesDestination.clear();
                                airportsDestination.clear();
                                datesOrigin.clear();
                                timesOrigin.clear();
                                airportsOrigin.clear();

                                Log.d("Stahlbau", j);


                                FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(givenDate).child(j).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot snapshot : dataSnapshot.child("legs").getChildren()){
                                            iatasOrigin.add(snapshot.child("iataOrigin").getValue().toString());
                                            iatasDestination.add(snapshot.child("iataDestination").getValue().toString());
                                            datesDestination.add(snapshot.child("dateDestination").getValue().toString());
                                            timesDestination.add(snapshot.child("timeDestination").getValue().toString());
                                            airportsDestination.add(snapshot.child("airportDestination").getValue().toString());
                                            datesOrigin.add(snapshot.child("dateOrigin").getValue().toString());
                                            timesOrigin.add(snapshot.child("timeOrigin").getValue().toString());
                                            airportsOrigin.add(snapshot.child("airportOrigin").getValue().toString());

                                        }
                                        int size = iatasOrigin.size() - 1;
                                        String fullName = dataSnapshot.child("extra").child("fullName").getValue().toString();
                                        String nuntiusId = dataSnapshot.child("extra").child("userEmail").getValue().toString();

                                        if (!ownNewEmail.equals(nuntiusId)) {
                                            searchNuntiiList.add(new SearchNuntiiItem(iatasOrigin.get(0), iatasDestination.get(size), datesDestination.get(size), timesDestination.get(size), airportsDestination.get(size), datesOrigin.get(0), timesOrigin.get(0), airportsOrigin.get(0), fullName, price1 + " kr", String.valueOf(j)));
                                        }


                                        iatasOrigin.clear();
                                        iatasDestination.clear();
                                        datesDestination.clear();
                                        timesDestination.clear();
                                        airportsDestination.clear();
                                        datesOrigin.clear();
                                        timesOrigin.clear();
                                        airportsOrigin.clear();

                                        rv_searchNuntii = findViewById(R.id.rv_searchNuntii);
                                        mLayoutManager = new LinearLayoutManager(OrderActivity.this);
                                        mAdapter = new SearchNuntiiAdapter(searchNuntiiList);

                                        rv_searchNuntii.setLayoutManager(mLayoutManager);
                                        rv_searchNuntii.setAdapter(mAdapter);

                                        mAdapter.setOnItemClickListener(new SearchNuntiiAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(int position) {

                                                Intent intent = new Intent(OrderActivity.this, item_order_overview.class);
                                                intent.putExtra("iataOrigin", searchNuntiiList.get(position).getRv_iataOrigin());
                                                intent.putExtra("iataDestination", searchNuntiiList.get(position).getRv_iataDestination());
                                                intent.putExtra("dateDestination", searchNuntiiList.get(position).getRv_dateDestination());
                                                intent.putExtra("timeDestination", searchNuntiiList.get(position).getRv_timeDestination());
                                                intent.putExtra("airportDestination", searchNuntiiList.get(position).getRv_airportDestination());
                                                intent.putExtra("dateOrigin", searchNuntiiList.get(position).getRv_dateOrigin());
                                                intent.putExtra("timeOrigin", searchNuntiiList.get(position).getRv_timeOrigin());
                                                intent.putExtra("airportOrigin", searchNuntiiList.get(position).getRv_airportOrigin());
                                                intent.putExtra("nuntius", searchNuntiiList.get(position).getRv_nuntius());
                                                intent.putExtra("price", searchNuntiiList.get(position).getRv_price());
                                                intent.putExtra("id", searchNuntiiList.get(position).getRv_id());
                                                startActivity(intent);
                                                finish();

                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(OrderActivity.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
                        }


                    });
                }


            }
        });

    }

    private float geolocateLatitude(String locSearch)  {

        String locationName = locSearch;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1000);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

                return (float) address.getLatitude();
            }
        } catch(IOException e) {
            Toast.makeText(OrderActivity.this, "Results are most likely not ordered due to a server error. Please restart your device", Toast.LENGTH_SHORT).show();
        }

        return (float) 0.0;
    }

    private float geolocateLongitude(String locSearch)  {

        String locationName = locSearch;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1000);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

                return (float) address.getLongitude();
            }
        } catch(IOException e) {
            Toast.makeText(OrderActivity.this, "Results are most likely not ordered due to a server error. Please restart your device", Toast.LENGTH_SHORT).show();
        }
        return (float) 0.0;
    }



}
