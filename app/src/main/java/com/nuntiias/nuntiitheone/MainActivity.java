package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class MainActivity extends AppCompatActivity {

    private Button btn_addItinerary, btn_itinerary, btn_order, btn_nuntiiMatches, btn_payment, btn_backToLogin;
    private ImageButton btn_settings;
    private TextView tv_verifyEmail;

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
        setContentView(R.layout.activity_main);


        //FirebaseDatabase.getInstance().getReference().child("payoutData").child("2023-12-01").child("curlString1").setValue(" "); never never never

        //FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);



        FirebaseDatabase.getInstance().getReference().child("grundpreis").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                float grundpreis = snapshot.getValue(float.class);

                new FirebaseInstanceId().getInstanceId();

                FirebaseAuth auth;

                btn_addItinerary = findViewById(R.id.btn_addItinerary);
                btn_itinerary = findViewById(R.id.btn_itinerary);
                btn_order = findViewById(R.id.btn_order);
                btn_nuntiiMatches = findViewById(R.id.btn_nuntiiMatches);
                btn_settings = findViewById(R.id.btn_settings);
                btn_backToLogin = findViewById(R.id.btn_backToLogin);
                tv_verifyEmail = findViewById(R.id.tv_verifyEmail);
                auth = FirebaseAuth.getInstance();

                if (!auth.getCurrentUser().isEmailVerified()) {
                    tv_verifyEmail.setVisibility(View.VISIBLE);
                    btn_backToLogin.setVisibility(View.VISIBLE);

                    btn_addItinerary.setVisibility(View.GONE);
                    btn_itinerary.setVisibility(View.GONE);
                    btn_order.setVisibility(View.GONE);
                    btn_nuntiiMatches.setVisibility(View.GONE);
                    btn_settings.setVisibility(View.GONE);
                    //btn_payment.setVisibility(View.GONE);


                    /*btn_verifyEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                    btn_verifyEmail.setVisibility(View.GONE);
                                    tv_verifyEmail.setVisibility(View.GONE);

                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });
                        }
                    });*/

                    btn_backToLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                    btn_backToLogin.setVisibility(View.GONE);
                                    tv_verifyEmail.setVisibility(View.GONE);

                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });
                        }
                    });



                } else {
                    tv_verifyEmail.setVisibility(View.GONE);
                    btn_backToLogin.setVisibility(View.GONE);

                    btn_addItinerary.setVisibility(View.VISIBLE);
                    btn_itinerary.setVisibility(View.VISIBLE);
                    btn_order.setVisibility(View.VISIBLE);
                    btn_nuntiiMatches.setVisibility(View.VISIBLE);
                    btn_settings.setVisibility(View.VISIBLE);
                    //btn_payment.setVisibility(View.VISIBLE);
                }
                /*btn_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });*/

                /*btn_resetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
                    }
                });*/

                btn_addItinerary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, AddItineraryActivity.class));
                    }
                });

                btn_itinerary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, ItineraryActivity.class));
                    }
                });

                btn_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                        intent.putExtra("grundpreis", grundpreis);
                        startActivity(intent);
                    }
                });

                btn_nuntiiMatches.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, NuntiiMatchesActivity.class));
                    }
                });

                /*btn_payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, PayPalActivity.class));
                    }
                });*/

                btn_settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}