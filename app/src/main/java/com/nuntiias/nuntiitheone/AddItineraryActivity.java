package com.nuntiias.nuntiitheone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
        import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;


public class  AddItineraryActivity extends AppCompatActivity {

    public static final String EXTRA_FLIGHT_RECYCLER = "com.example.myapplication.EXTRA_FLIGHT_RECYCLER";

    private String lastDestinationLat = "";
    private String lastDestinationLon = "";
    private String lastAirport = "";
    private String lastAirportTime = "";
    private String lastAirportDate = "";
    private boolean beginner = true;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btn_confirmFlight;

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
        setContentView(R.layout.activity_add_itinerary);

        Toast.makeText(AddItineraryActivity.this, "Connecting? Enter each flight seperately", Toast.LENGTH_SHORT);


        // assign values to each control on the layout
        Button btn_getFlight = findViewById(R.id.btn_getFlight);
        TextView et_date = findViewById(R.id.et_date);
        EditText et_origin = findViewById(R.id.et_origin);
        EditText et_destination = findViewById(R.id.et_destination);
        EditText et_flightNumber = findViewById(R.id.et_flightNumber);
        btn_confirmFlight = findViewById(R.id.btn_confirmFlight);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddItineraryActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
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
                et_date.setText(date);
            }
        };

        ////////////////////

        //RecyclerView rv_flightInformation = findViewById(R.id.rv_flightInformation);
        //final RecyclerAdapter[] adapter = new RecyclerAdapter[1];
        //String iataCodes[] = "";
        //rv_flightInformation.setLayoutManager(new LinearLayoutManager(this));

        ////////////////////

        ArrayList<RecyclerAdapter> recyclerList = new ArrayList<>();
        try {
            Intent intent = getIntent();
            int length = intent.getIntExtra("Length", 0);


            for (int i = 0; i < length; i++) {
                RecyclerAdapter parcelable = intent.getParcelableExtra("Flight no. " + String.valueOf(i));
                recyclerList.add(parcelable);
            }

            et_date.setText(recyclerList.get(recyclerList.size()-1).getRv_dateDestination());
            et_origin.setText(recyclerList.get(recyclerList.size()-1).getRv_iataDestination());

            lastDestinationLat = recyclerList.get(recyclerList.size()-1).getRv_destinationLat();
            lastDestinationLon = recyclerList.get(recyclerList.size()-1).getRv_destinationLon();
            lastAirport = recyclerList.get(recyclerList.size()-1).getRv_iataDestination();
            lastAirportTime = recyclerList.get(recyclerList.size()-1).getRv_timeDestination();
            lastAirportDate = recyclerList.get(recyclerList.size()-1).getRv_dateDestination();
            beginner = false;
        }
        catch(Exception e){
            beginner = true;
        }




        btn_getFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    OkHttpClient client = new OkHttpClient();

                    String url = "https://aerodatabox.p.rapidapi.com/flights/number/" + et_flightNumber.getText().toString() + "/" + et_date.getText().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .addHeader("x-rapidapi-key", " ")
                            .addHeader("x-rapidapi-host", "aerodatabox.p.rapidapi.com")
                            .build();

                    Log.d("Nantahala", String.valueOf(request));



                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("Alling", "Kruzifix!");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String myResponse = response.body().string();

                                Log.d("flightdata", myResponse);

                                AddItineraryActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Gson gson = new Gson();

                                        if (myResponse.length() > 10) {
                                            String json = myResponse.substring(1, myResponse.length() - 1);

                                            int curlyCount = 0;
                                            ArrayList<Integer> startingBrackets = new ArrayList<Integer>();
                                            ArrayList<Integer> endingBrackets = new ArrayList<Integer>();
                                            ArrayList<String> origins = new ArrayList<String>();
                                            ArrayList<String> destinations = new ArrayList<String>();
                                            ArrayList<String> datesDestination = new ArrayList<String>();
                                            ArrayList<String> timesDestination = new ArrayList<String>();
                                            ArrayList<String> airportsDestination = new ArrayList<String>();
                                            ArrayList<String> datesOrigin = new ArrayList<String>();
                                            ArrayList<String> timesOrigin = new ArrayList<String>();
                                            ArrayList<String> airportsOrigin = new ArrayList<String>();
                                            ArrayList<Float> originLat = new ArrayList<Float>();
                                            ArrayList<Float> originLon = new ArrayList<Float>();
                                            ArrayList<Float> destinationLat = new ArrayList<Float>();
                                            ArrayList<Float> destinationLon = new ArrayList<Float>();
                                            Boolean isInsideQuote = false;
                                            String originInput = et_origin.getText().toString();
                                            String destinationInput = et_destination.getText().toString();
                                            for (int i = 0; i < json.length(); i++) {
                                                String c = String.valueOf(json.charAt(i));
                                                if (c.equals("\"")) {
                                                    isInsideQuote = !isInsideQuote;
                                                }
                                                if (!isInsideQuote) {
                                                    if (c.equals("{")) {
                                                        if (curlyCount == 0) {
                                                            startingBrackets.add(i);
                                                        }
                                                        curlyCount++;
                                                    }
                                                    if (c.equals("}")) {
                                                        curlyCount--;
                                                        if (curlyCount == 0) {
                                                            endingBrackets.add(i);
                                                        }
                                                    }
                                                }
                                            }
                                            int objectCount = startingBrackets.size();

                                            for (int i = 0; i < objectCount; i++) {
                                                String currentJson = json.substring(startingBrackets.get(i), endingBrackets.get(i) + 1);
                                                Flightdata flightdata = gson.fromJson(currentJson, Flightdata.class);
                                                try{origins.add(flightdata.getDeparture().getAirport().getIata());}catch(Exception e) {origins.add("");}
                                                try{destinations.add(flightdata.getArrival().getArrivalAirport().getArrivalIata());}catch(Exception e){destinations.add("");}
                                                try{datesDestination.add(flightdata.getArrival().getArrivalScheduledTimeLocal());}catch(Exception e){datesDestination.add("");}
                                                try{timesDestination.add(flightdata.getArrival().getArrivalScheduledTimeLocal());}catch(Exception e){timesDestination.add("");}
                                                try{airportsDestination.add(flightdata.getArrival().getArrivalAirport().getArrivalName());}catch(Exception e){airportsDestination.add("");}
                                                try{datesOrigin.add(flightdata.getDeparture().getScheduledTimeLocal());}catch(Exception e){datesOrigin.add("");}
                                                try{timesOrigin.add(flightdata.getDeparture().getScheduledTimeLocal());}catch(Exception e){timesOrigin.add("");}
                                                try{airportsOrigin.add(flightdata.getDeparture().getAirport().getName());}catch(Exception e){airportsOrigin.add("");}
                                                try{originLat.add(flightdata.getDeparture().getAirport().getAirportLocation().getLat());}catch(Exception e){originLat.add(null);}
                                                try{originLon.add(flightdata.getDeparture().getAirport().getAirportLocation().getLon());}catch(Exception e){originLat.add(null);}
                                                try{destinationLat.add(flightdata.getArrival().getArrivalAirport().getArrivalAirportLocation().getArrivalLat());}catch(Exception e){originLat.add(null);}
                                                try{destinationLon.add(flightdata.getArrival().getArrivalAirport().getArrivalAirportLocation().getArrivalLon());}catch(Exception e){originLat.add(null);}
                                            }

                                            boolean permission;
                                            permission = false;

                                            if (destinations.size() == 1) {
                                                permission = true;
                                            }
                                            else if (destinations.indexOf(destinationInput) >= origins.indexOf(originInput)) {
                                                permission = true;
                                            }


                                            if (permission == true) {
                                                int oriInd = 0;
                                                int desInd = 0;
                                                if (destinations.size() == 1) {
                                                } else {
                                                    oriInd = origins.indexOf(originInput);
                                                    desInd = destinations.indexOf(destinationInput);
                                                }


                                                ArrayList<RecyclerAdapter> oneItemRecyclerList = new ArrayList<>();
                                                ArrayList<String> proxyList = new ArrayList<>();
                                                boolean toastie = false;
                                                int errorCount = 0;
                                                try{proxyList.add(origins.get(oriInd));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                                try{proxyList.add(destinations.get(desInd));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                                try{proxyList.add(datesDestination.get(desInd).substring(0, 10));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                                try{proxyList.add(timesDestination.get(desInd).substring(11, 16));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                                try{proxyList.add(airportsDestination.get(desInd));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                                try{proxyList.add(datesOrigin.get(oriInd).substring(0, 10));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                                try{proxyList.add(timesOrigin.get(oriInd).substring(11, 16));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                                try{proxyList.add(airportsOrigin.get(oriInd));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                                try{proxyList.add(et_flightNumber.getText().toString());} catch (Exception e) {proxyList.add(""); toastie = true;}
                                                try{proxyList.add(originLat.get(oriInd).toString());} catch (Exception e) {proxyList.add("");}
                                                try{proxyList.add(originLon.get(oriInd).toString());} catch (Exception e) {proxyList.add("");}
                                                try{proxyList.add(destinationLat.get(desInd).toString());} catch (Exception e) {proxyList.add("");}
                                                try{proxyList.add(destinationLon.get(desInd).toString());} catch (Exception e) {proxyList.add("");}

                                                if(errorCount > 0) {
                                                    Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                                } else if (DateTimeComparator.NoPastFlights(proxyList.get(5)) == true) {
                                                    Toast.makeText(AddItineraryActivity.this, "Your flight is in the past or it's too close to departure", Toast.LENGTH_SHORT).show();
                                                } else if (ConnectionDistanceComparator.LessThanKm(beginner, lastDestinationLat, lastDestinationLon, proxyList.get(9), proxyList.get(10)) == true) {
                                                    Toast.makeText(AddItineraryActivity.this, "If you are connecting via a land transfer: Airports mustn't be more than 130 km apart", Toast.LENGTH_SHORT).show();
                                                } else if (DateTimeComparator.ThreeHoursBetweenDifferentAirports(beginner, lastAirport, lastAirportTime, proxyList.get(0), proxyList.get(6), lastAirportDate, proxyList.get(2)) == true) {
                                                    Toast.makeText(AddItineraryActivity.this, "If you are connecting via a land transfer: At least 3 h between flights required", Toast.LENGTH_SHORT).show();
                                                } else if (DateTimeComparator.ThirtyMinutesBetweenSameAirport(beginner, lastAirport, lastAirportTime, proxyList.get(0), proxyList.get(6), lastAirportDate, proxyList.get(2)) == true) {
                                                    Toast.makeText(AddItineraryActivity.this, "There must be at least 30 minutes between two connecting flights", Toast.LENGTH_SHORT).show();
                                                } else if (DateTimeComparator.FourtyeightHoursBetweenFlights(beginner, lastAirportTime, proxyList.get(6), lastAirportDate, proxyList.get(2)) == true) {
                                                    Toast.makeText(AddItineraryActivity.this, "There mustn't be more than 48 hours between two connecting flights", Toast.LENGTH_SHORT).show();
                                                } else if (DateTimeComparator.OnlyNorwegianAirports(proxyList.get(0), proxyList.get(1)) == true) {
                                                    Toast.makeText(AddItineraryActivity.this, "Only flights within Norway allowed", Toast.LENGTH_SHORT).show();
                                                }
                                                else {

                                                    if (DateTimeComparator.ThreeHoursBetweenDifferentAirportsEmptyStrings(beginner, lastAirport, lastAirportTime, proxyList.get(0), proxyList.get(6)) == true) {
                                                        Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                                    }


                                                    try {
                                                        //recyclerList.add(new RecyclerAdapter(MainActivity.this, proxyList.get(0), proxyList.get(1), proxyList.get(2), proxyList.get(3), proxyList.get(4), proxyList.get(5), proxyList.get(6), proxyList.get(7), proxyList.get(8)));
                                                        oneItemRecyclerList.add(new RecyclerAdapter(AddItineraryActivity.this, proxyList.get(0), proxyList.get(1), proxyList.get(2), proxyList.get(3), proxyList.get(4), proxyList.get(5), proxyList.get(6), proxyList.get(7), proxyList.get(8), proxyList.get(9), proxyList.get(10), proxyList.get(11), proxyList.get(12)));
                                                        //recyclerList.add(new RecyclerAdapter(MainActivity.this, origins.get(oriInd), destinations.get(desInd), datesDestination.get(desInd).substring(0, 10), timesDestination.get(desInd).substring(11, 16), airportsDestination.get(desInd), datesOrigin.get(oriInd).substring(0, 10), timesOrigin.get(oriInd).substring(11, 16), airportsOrigin.get(oriInd), et_flightNumber.getText().toString()));
                                                        //oneItemRecyclerList.add(new RecyclerAdapter(MainActivity.this, origins.get(oriInd), destinations.get(desInd), datesDestination.get(desInd).substring(0, 10), timesDestination.get(desInd).substring(11, 16), airportsDestination.get(desInd), datesOrigin.get(oriInd).substring(0, 10), timesOrigin.get(oriInd).substring(11, 16), airportsOrigin.get(oriInd), et_flightNumber.getText().toString()));
                                                        mRecyclerView = findViewById(R.id.rv_flightInformation);
                                                        mRecyclerView.setHasFixedSize(true);
                                                        mLayoutManager = new LinearLayoutManager(AddItineraryActivity.this);
                                                        mAdapter = new FlightDataAdapter(oneItemRecyclerList);
                                                        mRecyclerView.setLayoutManager(mLayoutManager);
                                                        mRecyclerView.setAdapter(mAdapter);

                                                        btn_confirmFlight.setVisibility(View.VISIBLE);
                                                        btn_confirmFlight.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(AddItineraryActivity.this, itinerary_overview.class);
                                                                recyclerList.add(oneItemRecyclerList.get(oneItemRecyclerList.size() - 1));
                                                                for (int i = 0; i < recyclerList.size(); i++) {
                                                                    intent.putExtra("Flight no. " + String.valueOf(i), recyclerList.get(i));
                                                                }
                                                                intent.putExtra("Length", recyclerList.size());

                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                                    } catch (Exception e) {
                                                        Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                            else {
                                                Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                        }







                                    }
                                });
                            }
                        }
                    });

                } catch (Exception exception) {

                    Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();

                }


                /*OkHttpClient client = new OkHttpClient();

                String url = "https://aerodatabox.p.rapidapi.com/flights/number/" + et_flightNumber.getText().toString() + "/" + et_date.getText().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("x-rapidapi-key", "4986bafec1msh5a1d233cf14a74cp12fdccjsn5a0a72605af9 ")
                        .addHeader("x-rapidapi-host", "aerodatabox.p.rapidapi.com")
                        .build();



                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("Alling", "Kruzifix!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String myResponse = response.body().string();

                            Log.d("flightdata", myResponse);

                            AddItineraryActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Gson gson = new Gson();
                                    String json = myResponse.substring(1, myResponse.length() - 1);

                                    int curlyCount = 0;
                                    ArrayList<Integer> startingBrackets = new ArrayList<Integer>();
                                    ArrayList<Integer> endingBrackets = new ArrayList<Integer>();
                                    ArrayList<String> origins = new ArrayList<String>();
                                    ArrayList<String> destinations = new ArrayList<String>();
                                    ArrayList<String> datesDestination = new ArrayList<String>();
                                    ArrayList<String> timesDestination = new ArrayList<String>();
                                    ArrayList<String> airportsDestination = new ArrayList<String>();
                                    ArrayList<String> datesOrigin = new ArrayList<String>();
                                    ArrayList<String> timesOrigin = new ArrayList<String>();
                                    ArrayList<String> airportsOrigin = new ArrayList<String>();
                                    ArrayList<Float> originLat = new ArrayList<Float>();
                                    ArrayList<Float> originLon = new ArrayList<Float>();
                                    ArrayList<Float> destinationLat = new ArrayList<Float>();
                                    ArrayList<Float> destinationLon = new ArrayList<Float>();
                                    Boolean isInsideQuote = false;
                                    String originInput = et_origin.getText().toString();
                                    String destinationInput = et_destination.getText().toString();
                                    for (int i = 0; i < json.length(); i++) {
                                        String c = String.valueOf(json.charAt(i));
                                        if (c.equals("\"")) {
                                            isInsideQuote = !isInsideQuote;
                                        }
                                        if (!isInsideQuote) {
                                            if (c.equals("{")) {
                                                if (curlyCount == 0) {
                                                    startingBrackets.add(i);
                                                }
                                                curlyCount++;
                                            }
                                            if (c.equals("}")) {
                                                curlyCount--;
                                                if (curlyCount == 0) {
                                                    endingBrackets.add(i);
                                                }
                                            }
                                        }
                                    }
                                    int objectCount = startingBrackets.size();

                                    for (int i = 0; i < objectCount; i++) {
                                        String currentJson = json.substring(startingBrackets.get(i), endingBrackets.get(i) + 1);
                                        Flightdata flightdata = gson.fromJson(currentJson, Flightdata.class);
                                        try{origins.add(flightdata.getDeparture().getAirport().getIata());}catch(Exception e) {origins.add("");}
                                        try{destinations.add(flightdata.getArrival().getArrivalAirport().getArrivalIata());}catch(Exception e){destinations.add("");}
                                        try{datesDestination.add(flightdata.getArrival().getArrivalScheduledTimeLocal());}catch(Exception e){datesDestination.add("");}
                                        try{timesDestination.add(flightdata.getArrival().getArrivalScheduledTimeLocal());}catch(Exception e){timesDestination.add("");}
                                        try{airportsDestination.add(flightdata.getArrival().getArrivalAirport().getArrivalName());}catch(Exception e){airportsDestination.add("");}
                                        try{datesOrigin.add(flightdata.getDeparture().getScheduledTimeLocal());}catch(Exception e){datesOrigin.add("");}
                                        try{timesOrigin.add(flightdata.getDeparture().getScheduledTimeLocal());}catch(Exception e){timesOrigin.add("");}
                                        try{airportsOrigin.add(flightdata.getDeparture().getAirport().getName());}catch(Exception e){airportsOrigin.add("");}
                                        try{originLat.add(flightdata.getDeparture().getAirport().getAirportLocation().getLat());}catch(Exception e){originLat.add(null);}
                                        try{originLon.add(flightdata.getDeparture().getAirport().getAirportLocation().getLon());}catch(Exception e){originLat.add(null);}
                                        try{destinationLat.add(flightdata.getArrival().getArrivalAirport().getArrivalAirportLocation().getArrivalLat());}catch(Exception e){originLat.add(null);}
                                        try{destinationLon.add(flightdata.getArrival().getArrivalAirport().getArrivalAirportLocation().getArrivalLon());}catch(Exception e){originLat.add(null);}
                                    }

                                    boolean permission;
                                    permission = false;

                                    if (destinations.size() == 1) {
                                        permission = true;
                                    }
                                    else if (destinations.indexOf(destinationInput) >= origins.indexOf(originInput)) {
                                        permission = true;
                                    }


                                    if (permission == true) {
                                        int oriInd = 0;
                                        int desInd = 0;
                                        if (destinations.size() == 1) {
                                        } else {
                                            oriInd = origins.indexOf(originInput);
                                            desInd = destinations.indexOf(destinationInput);
                                        }


                                        ArrayList<RecyclerAdapter> oneItemRecyclerList = new ArrayList<>();
                                        ArrayList<String> proxyList = new ArrayList<>();
                                        boolean toastie = false;
                                        int errorCount = 0;
                                        try{proxyList.add(origins.get(oriInd));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                        try{proxyList.add(destinations.get(desInd));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                        try{proxyList.add(datesDestination.get(desInd).substring(0, 10));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                        try{proxyList.add(timesDestination.get(desInd).substring(11, 16));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                        try{proxyList.add(airportsDestination.get(desInd));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                        try{proxyList.add(datesOrigin.get(oriInd).substring(0, 10));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                        try{proxyList.add(timesOrigin.get(oriInd).substring(11, 16));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                        try{proxyList.add(airportsOrigin.get(oriInd));} catch (Exception e) {proxyList.add(""); toastie = true; errorCount++;}
                                        try{proxyList.add(et_flightNumber.getText().toString());} catch (Exception e) {proxyList.add(""); toastie = true;}
                                        try{proxyList.add(originLat.get(oriInd).toString());} catch (Exception e) {proxyList.add("");}
                                        try{proxyList.add(originLon.get(oriInd).toString());} catch (Exception e) {proxyList.add("");}
                                        try{proxyList.add(destinationLat.get(desInd).toString());} catch (Exception e) {proxyList.add("");}
                                        try{proxyList.add(destinationLon.get(desInd).toString());} catch (Exception e) {proxyList.add("");}

                                        if(errorCount > 0) {
                                            Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                        } else if (DateTimeComparator.NoPastFlights(proxyList.get(5)) == true) {
                                            Toast.makeText(AddItineraryActivity.this, "Your flight is in the past or it's too close to departure", Toast.LENGTH_SHORT).show();
                                        } else if (ConnectionDistanceComparator.LessThanKm(beginner, lastDestinationLat, lastDestinationLon, proxyList.get(9), proxyList.get(10)) == true) {
                                            Toast.makeText(AddItineraryActivity.this, "If you are connecting via a land transfer: Airports mustn't be more than 130 km apart", Toast.LENGTH_SHORT).show();
                                        } else if (DateTimeComparator.ThreeHoursBetweenDifferentAirports(beginner, lastAirport, lastAirportTime, proxyList.get(0), proxyList.get(6), lastAirportDate, proxyList.get(2)) == true) {
                                            Toast.makeText(AddItineraryActivity.this, "If you are connecting via a land transfer: At least 3 h between flights required", Toast.LENGTH_SHORT).show();
                                        } else if (DateTimeComparator.ThirtyMinutesBetweenSameAirport(beginner, lastAirport, lastAirportTime, proxyList.get(0), proxyList.get(6), lastAirportDate, proxyList.get(2)) == true) {
                                            Toast.makeText(AddItineraryActivity.this, "There must be at least 30 minutes between two connecting flights", Toast.LENGTH_SHORT).show();
                                        } else if (DateTimeComparator.FourtyeightHoursBetweenFlights(beginner, lastAirportTime, proxyList.get(6), lastAirportDate, proxyList.get(2)) == true) {
                                            Toast.makeText(AddItineraryActivity.this, "There mustn't be more than 48 hours between two connecting flights", Toast.LENGTH_SHORT).show();
                                        } else if (DateTimeComparator.OnlyNorwegianAirports(proxyList.get(0), proxyList.get(1)) == true) {
                                            Toast.makeText(AddItineraryActivity.this, "Only flights within Norway allowed", Toast.LENGTH_SHORT).show();
                                        }
                                        else {

                                            if (DateTimeComparator.ThreeHoursBetweenDifferentAirportsEmptyStrings(beginner, lastAirport, lastAirportTime, proxyList.get(0), proxyList.get(6)) == true) {
                                                Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                            }


                                            try {
                                                //recyclerList.add(new RecyclerAdapter(MainActivity.this, proxyList.get(0), proxyList.get(1), proxyList.get(2), proxyList.get(3), proxyList.get(4), proxyList.get(5), proxyList.get(6), proxyList.get(7), proxyList.get(8)));
                                                oneItemRecyclerList.add(new RecyclerAdapter(AddItineraryActivity.this, proxyList.get(0), proxyList.get(1), proxyList.get(2), proxyList.get(3), proxyList.get(4), proxyList.get(5), proxyList.get(6), proxyList.get(7), proxyList.get(8), proxyList.get(9), proxyList.get(10), proxyList.get(11), proxyList.get(12)));
                                                //recyclerList.add(new RecyclerAdapter(MainActivity.this, origins.get(oriInd), destinations.get(desInd), datesDestination.get(desInd).substring(0, 10), timesDestination.get(desInd).substring(11, 16), airportsDestination.get(desInd), datesOrigin.get(oriInd).substring(0, 10), timesOrigin.get(oriInd).substring(11, 16), airportsOrigin.get(oriInd), et_flightNumber.getText().toString()));
                                                //oneItemRecyclerList.add(new RecyclerAdapter(MainActivity.this, origins.get(oriInd), destinations.get(desInd), datesDestination.get(desInd).substring(0, 10), timesDestination.get(desInd).substring(11, 16), airportsDestination.get(desInd), datesOrigin.get(oriInd).substring(0, 10), timesOrigin.get(oriInd).substring(11, 16), airportsOrigin.get(oriInd), et_flightNumber.getText().toString()));
                                                mRecyclerView = findViewById(R.id.rv_flightInformation);
                                                mRecyclerView.setHasFixedSize(true);
                                                mLayoutManager = new LinearLayoutManager(AddItineraryActivity.this);
                                                mAdapter = new FlightDataAdapter(oneItemRecyclerList);
                                                mRecyclerView.setLayoutManager(mLayoutManager);
                                                mRecyclerView.setAdapter(mAdapter);

                                                btn_confirmFlight.setVisibility(View.VISIBLE);
                                                btn_confirmFlight.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(AddItineraryActivity.this, itinerary_overview.class);
                                                        recyclerList.add(oneItemRecyclerList.get(oneItemRecyclerList.size() - 1));
                                                        for (int i = 0; i < recyclerList.size(); i++) {
                                                            intent.putExtra("Flight no. " + String.valueOf(i), recyclerList.get(i));
                                                        }
                                                        intent.putExtra("Length", recyclerList.size());

                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            } catch (Exception e) {
                                                Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    else {
                                        Toast.makeText(AddItineraryActivity.this, "Please wait until one week before your flight, or email us: info@nuntii.tech", Toast.LENGTH_SHORT).show();
                                    }






                                }
                            });
                        }
                    }
                });*/






            }
        });



    }



}
