package com.nuntiias.nuntiitheone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class item_order_overview extends AppCompatActivity {

    private TextView tv_iataOrigin, tv_iataDestination, tv_dateDestination, tv_timeDestination, tv_airportDestination, tv_dateOrigin, tv_timeOrigin, tv_airportOrigin, tv_nuntius, tv_price;
    private Button btn_specify;

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
        setContentView(R.layout.activity_item_order_overview);

        Intent intent = getIntent();

        String iataOrigin = intent.getExtras().getString("iataOrigin");
        String iataDestination = intent.getExtras().getString("iataDestination");
        String dateDestination = intent.getExtras().getString("dateDestination");
        String timeDestination = intent.getExtras().getString("timeDestination");
        String airportDestination = intent.getExtras().getString("airportDestination");
        String dateOrigin = intent.getExtras().getString("dateOrigin");
        String timeOrigin = intent.getExtras().getString("timeOrigin");
        String airportOrigin = intent.getExtras().getString("airportOrigin");
        String nuntius = intent.getExtras().getString("nuntius");
        String price = intent.getExtras().getString("price");
        String id = intent.getExtras().getString("id");

        tv_iataOrigin = findViewById(R.id.tv_iataOrigin);
        tv_iataDestination = findViewById(R.id.tv_iataDestination);
        tv_dateDestination = findViewById(R.id.tv_dateDestination);
        tv_timeDestination = findViewById(R.id.tv_timeDestination);
        tv_airportDestination = findViewById(R.id.tv_airportDestination);
        tv_dateOrigin = findViewById(R.id.tv_dateOrigin);
        tv_timeOrigin = findViewById(R.id.tv_timeOrigin);
        tv_airportOrigin = findViewById(R.id.tv_airportOrigin);
        tv_nuntius = findViewById(R.id.tv_nuntius);
        tv_price = findViewById(R.id.tv_price);

        tv_iataOrigin.setText(iataOrigin);
        tv_iataDestination.setText(iataDestination);
        tv_dateDestination.setText(dateDestination);
        tv_timeDestination.setText(timeDestination);
        tv_airportDestination.setText(airportDestination);
        tv_dateOrigin.setText(dateOrigin);
        tv_timeOrigin.setText(timeOrigin);
        tv_airportOrigin.setText(airportOrigin);
        tv_nuntius.setText(nuntius);
        tv_price.setText(price);

        btn_specify = findViewById(R.id.btn_specify);
        btn_specify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_order_overview.this, order_specify_parcel.class);
                intent.putExtra("id", id);
                intent.putExtra("nuntius", nuntius);
                intent.putExtra("price", price);
                intent.putExtra("date", dateOrigin);
                startActivity(intent);
                finish();
            }
        });

    }
}