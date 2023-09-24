package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class item_nuntii_matches_confirm_reception extends AppCompatActivity {

    private String matchId;
    private String date;
    private String nuntiusId;
    private String senderId;
    private String receiverId;
    private String nuntiusPrice;
    private String itineraryId;
    private Button btn_finalConfirm;
    private TextView tv_confirmedReception;

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
        setContentView(R.layout.activity_item_nuntii_matches_confirm_reception);

        Intent intent = getIntent();
        matchId = intent.getExtras().getString("matchId");
        date = intent.getExtras().getString("date");
        nuntiusPrice = intent.getExtras().getString("price");
        nuntiusId = intent.getExtras().getString("nuntiusId");
        senderId = intent.getExtras().getString("senderId");
        receiverId = intent.getExtras().getString("receiverId");
        itineraryId = intent.getExtras().getString("itineraryId");

        btn_finalConfirm = (Button) findViewById(R.id.btn_finalConfirm);
        tv_confirmedReception = findViewById(R.id.tv_confirmedReception);

        FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("confirmed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String confirmed = snapshot.getValue().toString();

                FirebaseDatabase.getInstance().getReference().child("allItineraries").child(date).child(itineraryId).child("legs").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                        int index = (int) snapshot.getChildrenCount() - 1;
                        String ind = String.valueOf(index);

                        String arrivalDate = snapshot.child(ind).child("dateDestination").getValue().toString();
                        String arrivalTime = snapshot.child(ind).child("timeDestination").getValue().toString();
                        String origin = snapshot.child(ind).child("iataOrigin").getValue().toString();
                        String destination = snapshot.child(ind).child("iataDestination").getValue().toString();


                        if (confirmed.equals("true")) {

                            tv_confirmedReception.setText("Reception already confirmed");
                            btn_finalConfirm.setVisibility(View.INVISIBLE);
                        } else if (DateTimeComparator.ReceptionNotYet(arrivalDate, arrivalTime) == true) {

                            tv_confirmedReception.setText("Earliest time of reception: ETA of last flight");
                            btn_finalConfirm.setVisibility(View.INVISIBLE);
                        } else if (confirmed.equals("false")) {
                            btn_finalConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("confirmed").setValue("true");

                                    FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("confirmed").setValue("true");
                                    FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("confirmed").setValue("true");
                                    FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("confirmed").setValue("true");

                                    FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("payoutBalance").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            Double payoutBalanceOld = snapshot.getValue(Double.class);

                                            FirebaseDatabase.getInstance().getReference().child("goldenRatio").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                    Double goldenRatio = snapshot.getValue(Double.class);

                                                    Double price = Double.parseDouble(nuntiusPrice);
                                                    Double deductedPrice = price*goldenRatio;


                                                    Double payoutBalanceNew = payoutBalanceOld + deductedPrice;

                                                    FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("payoutBalance").setValue(payoutBalanceNew);

                                                    btn_finalConfirm.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(item_nuntii_matches_confirm_reception.this, "Reception confirmed", Toast.LENGTH_SHORT).show();

                                                    new AaaNewPushNotification().receptionNotification(nuntiusId);

                                                    emailReception(nuntiusId, nuntiusId, origin, destination);

                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            });




                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });

                                }
                            });
                        } else {
                            btn_finalConfirm.setVisibility(View.INVISIBLE);
                            tv_confirmedReception.setText("Server Error");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }

    private static Task<String> emailReception(String recipientRaw, String name, String origin, String destination) {

        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

        String recipient = recipientRaw.replace("__DOT__", ".");
        String nameNew = name.replace("__DOT__", ".");

        Map<String, Object> data = new HashMap<>();
        data.put("recipient", recipient);
        data.put("name", nameNew);
        data.put("origin", origin);
        data.put("destination", destination);

        return mFunctions
                .getHttpsCallable("emailReception")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });

    }

}