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

public class item_nuntii_matches extends AppCompatActivity {

    private TextView tv_date, tv_headerSender, tv_sender, tv_headerNuntiusMatch, tv_nuntiusMatch, tv_headerReceiver, tv_receiver;
    private Button btn_chatGroup, btn_parcelData, btn_itineraryData, btn_confirmReception, btn_reportProblem;

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
        setContentView(R.layout.activity_item_nuntii_matches);

        btn_confirmReception = (Button) findViewById(R.id.btn_confirmReception);
        btn_confirmReception.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String matchId = intent.getExtras().getString("matchId");
        String date = intent.getExtras().getString("date");
        String itineraryId = intent.getExtras().getString("itineraryId");
        String nuntiusFullName = intent.getExtras().getString("nuntiusFullName");
        String nuntiusId = intent.getExtras().getString("nuntiusId");
        String parcelDescription = intent.getExtras().getString("parcelDescription");
        String parcelSize = intent.getExtras().getString("parcelSize");
        String price = intent.getExtras().getString("price");
        String receiverFullName = intent.getExtras().getString("receiverFullName");
        String receiverId = intent.getExtras().getString("receiverId");
        String role = intent.getExtras().getString("role");
        String senderFullName = intent.getExtras().getString("senderFullName");
        String senderId = intent.getExtras().getString("senderId");
        String itineraryOrigin = intent.getExtras().getString("itineraryOrigin");
        String itineraryDestination = intent.getExtras().getString("itineraryDestination");

        /*if (role.equals("receiver")) {
            btn_confirmReception.setVisibility(View.VISIBLE);
        }*/

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_headerSender = (TextView) findViewById(R.id.tv_headerSender);
        tv_sender = (TextView) findViewById(R.id.tv_sender);
        tv_headerNuntiusMatch = (TextView) findViewById(R.id.tv_headerNuntiusMatch);
        tv_nuntiusMatch = (TextView) findViewById(R.id.tv_nuntiusMatch);
        tv_headerReceiver = (TextView) findViewById(R.id.tv_headerReceiver);
        tv_receiver = (TextView) findViewById(R.id.tv_receiver);
        btn_chatGroup = (Button) findViewById(R.id.btn_chatGroup);
        btn_parcelData = (Button) findViewById(R.id.btn_parcelData);
        btn_itineraryData = (Button) findViewById(R.id.btn_itineraryData);
        btn_reportProblem = (Button) findViewById(R.id.btn_reportProblem);

        tv_date.setText(date);

        if (role.equals("sender")) {
            tv_headerSender.setText("Sender (You):");
        }
        if (role.equals("nuntius")) {
            tv_headerNuntiusMatch.setText("Nuntius (You):");
        }
        if (role.equals("receiver")) {
            tv_headerReceiver.setText("Receiver (You):");
        }

        tv_sender.setText(senderFullName);
        tv_nuntiusMatch.setText(nuntiusFullName);
        tv_receiver.setText(receiverFullName);

        btn_parcelData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_nuntii_matches.this, item_nuntii_matches_parcel.class);
                intent.putExtra("parcelSize", parcelSize);
                intent.putExtra("parcelDescription", parcelDescription);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        btn_reportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_nuntii_matches.this, item_nuntii_matches_problem.class);
                startActivity(intent);
            }
        });

        btn_itineraryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_nuntii_matches.this, item_nuntii_matches_itinerary_data.class);
                intent.putExtra("date", date);
                intent.putExtra("itineraryId", itineraryId);
                startActivity(intent);
            }
        });

        btn_chatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_nuntii_matches.this, item_nuntii_matches_chat_group.class);
                intent.putExtra("senderId", senderId);
                intent.putExtra("senderFullName", senderFullName);
                intent.putExtra("nuntiusId", nuntiusId);
                intent.putExtra("nuntiusFullName", nuntiusFullName);
                intent.putExtra("receiverId", receiverId);
                intent.putExtra("receiverFullName", receiverFullName);
                intent.putExtra("role", role);
                intent.putExtra("matchId", matchId);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        btn_confirmReception.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_nuntii_matches.this, item_nuntii_matches_confirm_reception.class);
                intent.putExtra("matchId", matchId);
                intent.putExtra("date", date);
                intent.putExtra("price", price);
                intent.putExtra("nuntiusId", nuntiusId);
                intent.putExtra("senderId", senderId);
                intent.putExtra("receiverId", receiverId);
                intent.putExtra("itineraryId", itineraryId);
                startActivity(intent);
            }
        });
    }
}