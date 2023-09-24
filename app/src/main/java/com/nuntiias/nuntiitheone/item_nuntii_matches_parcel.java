package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

import org.jetbrains.annotations.NotNull;

public class item_nuntii_matches_parcel extends AppCompatActivity {

    private TextView tv_size, tv_description, tv_price_parcel;

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
        setContentView(R.layout.activity_item_nuntii_matches_parcel);

        tv_size = (TextView) findViewById(R.id.tv_size);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_price_parcel = (TextView) findViewById(R.id.tv_price_parcel);

        Intent intent = getIntent();
        String parcelSize = intent.getExtras().getString("parcelSize");
        String parcelDescription = intent.getExtras().getString("parcelDescription");
        String price = intent.getExtras().getString("price");

        tv_size.setText(parcelSize);
        tv_description.setText(parcelDescription);

        FirebaseDatabase.getInstance().getReference().child("eigenPreis").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                int eigenPreis = snapshot.getValue(int.class);
                tv_price_parcel.setText(String.valueOf(Float.valueOf(price) - eigenPreis) + " NOK");

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}