package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

import org.jetbrains.annotations.NotNull;

public class order_specify_parcel extends AppCompatActivity {

    Switch sw_senderReceiver;
    TextView tv_switchHeader, tv_partnerEmail, tv_headerSenderReceiver, textView5, textView7;
    EditText et_email, et_size, et_description;
    Button btn_confirmEmail, btn_sendIn, btn_changeSenderReceiver;
    Boolean sender = true;

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
        setContentView(R.layout.activity_order_specify_parcel);

        Intent intent = getIntent();
        String itineraryId = intent.getExtras().getString("id");
        String price = intent.getExtras().getString("price");
        String date = intent.getExtras().getString("date");

        tv_switchHeader = (TextView) findViewById(R.id.tv_switchHeader);
        tv_partnerEmail = (TextView) findViewById(R.id.tv_partnerEmail);
        tv_headerSenderReceiver = (TextView) findViewById(R.id.tv_headerSenderReceiver);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView7 = (TextView) findViewById(R.id.textView7);
        sw_senderReceiver = (Switch) findViewById(R.id.sw_senderReceiver);
        et_email = (EditText) findViewById(R.id.et_email);
        et_size = (EditText) findViewById(R.id.et_size);
        et_description = (EditText) findViewById(R.id.et_description);
        btn_confirmEmail = (Button) findViewById(R.id.btn_validateEmail);
        btn_sendIn = (Button) findViewById(R.id.btn_sendIn);
        btn_changeSenderReceiver = (Button) findViewById(R.id.btn_changeSenderReceiver);
        sw_senderReceiver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tv_switchHeader.setText("Specify the Sender:");
                    et_email.setHint("Email Address of Sender");
                    sender = false;
                } else {
                    tv_switchHeader.setText("Specify the Receiver:");
                    et_email.setHint("Email Address of Receiver");
                    sender = true;
                }
            }
        });

        btn_confirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString();
                String newEmail = email.replace(".", "__DOT__");

                String ownEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String ownNewEmail = ownEmail.replace(".", "__DOT__");

                userLookup(newEmail, ownNewEmail, email, sender, itineraryId, price, et_size.getText().toString(), et_description.getText().toString(), date);

            }
        });

    }

    public void userLookup(String newEmail, String ownNewEmail, String email, boolean sender, String id, String price, String size, String description, String date) {
        FirebaseDatabase.getInstance().getReference().child("userData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(date).child(id).child("extra").child("userEmail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String nuntiusId = snapshot.getValue().toString();

                        if (newEmail.isEmpty()) {
                            Toast.makeText(order_specify_parcel.this, "Please provide the email address of your partner", Toast.LENGTH_SHORT).show();
                        } else if (ownNewEmail.equals(nuntiusId)) {
                            Toast.makeText(order_specify_parcel.this, "You can't book yourself", Toast.LENGTH_SHORT).show();
                        } else if (ownNewEmail.equals(newEmail)) {
                            Toast.makeText(order_specify_parcel.this, "You can't use your own email address for your partner waiting hundreds or thousands of kilomters away. Your partner must be registered with Nuntii", Toast.LENGTH_SHORT).show();
                        } else if (newEmail.equals(nuntiusId)) {
                            Toast.makeText(order_specify_parcel.this, "Nuntius of this match can't be receiver or sender", Toast.LENGTH_SHORT).show();
                        } else if (dataSnapshot.hasChild(newEmail)) {

                            btn_confirmEmail.setVisibility(View.GONE);
                            btn_sendIn.setVisibility(View.VISIBLE);
                            btn_changeSenderReceiver.setVisibility(View.VISIBLE);
                            et_email.setVisibility(View.INVISIBLE);
                            tv_partnerEmail.setVisibility(View.VISIBLE);
                            tv_headerSenderReceiver.setVisibility(View.INVISIBLE);
                            textView5.setVisibility(View.INVISIBLE);
                            textView7.setVisibility(View.INVISIBLE);
                            sw_senderReceiver.setVisibility(View.INVISIBLE);
                            tv_partnerEmail.setText(email);

                            btn_sendIn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //NuntiiOrderDatabaseValidation(ownNewEmail, newEmail, sender, id, size, description, price, date);

                                    if (size.isEmpty()) {
                                        et_size.setError("Parcel size required");
                                    } else if (description.isEmpty()) {
                                        et_description.setError("Parcel description required");
                                    } else {

                                        FirebaseDatabase.getInstance().getReference().child("eigenPreis").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                float eigenPreis = snapshot.getValue(int.class);

                                                Intent intent = new Intent(order_specify_parcel.this, PaymentActivity.class);
                                                intent.putExtra("ownNewEmail", ownNewEmail);
                                                intent.putExtra("newEmail", newEmail);
                                                intent.putExtra("sender", sender);
                                                intent.putExtra("id", id);
                                                intent.putExtra("size", size);
                                                intent.putExtra("description", description);
                                                intent.putExtra("price", price);
                                                intent.putExtra("date", date);
                                                intent.putExtra("eigenPreis", String.valueOf(eigenPreis));
                                                startActivity(intent);
                                                finish();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });

                            btn_changeSenderReceiver.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ChangeSenderReceiver();
                                }
                            });

                        } else {
                            Toast.makeText(order_specify_parcel.this, "Please make sure that your partner is registered with Nuntii, or check whether you spelled the email address correctly", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(order_specify_parcel.this, "Please make sure that your partner is registered with Nuntii, or check whether you spelled the email address correctly", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ChangeSenderReceiver () {

        btn_confirmEmail.setVisibility(View.VISIBLE);
        btn_sendIn.setVisibility(View.INVISIBLE);
        btn_changeSenderReceiver.setVisibility(View.INVISIBLE);
        et_email.setVisibility(View.VISIBLE);
        tv_partnerEmail.setVisibility(View.INVISIBLE);
        tv_headerSenderReceiver.setVisibility(View.VISIBLE);
        textView5.setVisibility(View.VISIBLE);
        textView7.setVisibility(View.VISIBLE);
        sw_senderReceiver.setVisibility(View.VISIBLE);

    }

    public void NuntiiOrderDatabaseValidation (String userEmail, String partnerEmail, boolean sender, String itineraryId, String parcelSize, String parcelDescription, String price, String date) {

        String originalSenderId = "senderId";
        String originalReceiverId = "receiverId";

        if (sender) {
            originalSenderId = userEmail;
            originalReceiverId = partnerEmail;
        } else {
            originalSenderId = partnerEmail;
            originalReceiverId = userEmail;
        }

        if (parcelSize.isEmpty()) {
            et_size.setError("Parcel size required");
        } else if (parcelDescription.isEmpty()) {
            et_description.setError("Parcel description required");
        } else {

            String senderId = originalSenderId;
            String receiverId = originalReceiverId;
            FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(date).child(itineraryId).child("extra").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String nuntiusId = dataSnapshot.child("userEmail").getValue().toString();
                    String nuntiusFullName = dataSnapshot.child("fullName").getValue().toString();

                    FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            String senderFullName = dataSnapshot1.getValue().toString();

                            FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    String receiverFullName = dataSnapshot2.getValue().toString();

                                    FirebaseDatabase.getInstance().getReference().child("nuntiiMatchesCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int id = snapshot.getValue(Integer.class) + 1;
                                            FirebaseDatabase.getInstance().getReference().child("nuntiiMatchesCount").setValue(id);
                                            String matchId = "Nuntii" + String.valueOf(id);

                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("senderId", senderId);
                                            map.put("senderFullName", senderFullName);
                                            map.put("nuntiusId", nuntiusId);
                                            map.put("nuntiusFullName", nuntiusFullName);
                                            map.put("receiverId", receiverId);
                                            map.put("receiverFullName", receiverFullName);
                                            map.put("itineraryId", itineraryId);
                                            map.put("parcelSize", parcelSize);
                                            map.put("parcelDescription", parcelDescription);
                                            map.put("price", price);
                                            map.put("date", date);

                                            FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).updateChildren(map);

                                            FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).updateChildren(map);
                                            FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).updateChildren(map);
                                            FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).updateChildren(map);

                                            FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("role").setValue("sender");
                                            FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("role").setValue("nuntius");
                                            FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("role").setValue("receiver");

                                            FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("proposedItineraries").child(date).child(itineraryId).setValue(null);
                                            FirebaseDatabase.getInstance().getReference().child("proposedItineraries").child(date).child(itineraryId).setValue(null);

                                            saveOriginDestination(date, itineraryId, matchId, senderId, nuntiusId, receiverId);




                                            FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(0);

                                            FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(0);
                                            FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(0);
                                            FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(0);




                                            Toast.makeText(order_specify_parcel.this, "Congrats! You Successfully Created a Nuntii Match!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(order_specify_parcel.this, MainActivity.class));
                                            finish();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void saveOriginDestination(String date, String itineraryId, String matchId, String senderId, String nuntiusId, String receiverId) {

        FirebaseDatabase.getInstance().getReference().child("allItineraries").child(date).child(itineraryId).child("legs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                String lastIndex = String.valueOf(count-1);

                String itineraryOrigin = snapshot.child("0").child("iataOrigin").getValue().toString();
                String itineraryDestination = snapshot.child(lastIndex).child("iataDestination").getValue().toString();

                FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("itineraryOrigin").setValue(itineraryOrigin);
                FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("itineraryOrigin").setValue(itineraryOrigin);
                FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("itineraryOrigin").setValue(itineraryOrigin);
                FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("itineraryOrigin").setValue(itineraryOrigin);
                FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("itineraryDestination").setValue(itineraryDestination);
                FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("itineraryDestination").setValue(itineraryDestination);
                FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("itineraryDestination").setValue(itineraryDestination);
                FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("itineraryDestination").setValue(itineraryDestination);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}