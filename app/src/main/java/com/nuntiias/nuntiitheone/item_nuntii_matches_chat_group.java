package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class item_nuntii_matches_chat_group extends AppCompatActivity {

    private EditText et_textMessage;
    private Button btn_send;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String timestamp;

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
        setContentView(R.layout.activity_item_nuntii_matches_chat_group);

        Intent intent = getIntent();
        String authorId = "authorId";
        String authorFullName = "authorFullName";
        String senderId = intent.getExtras().getString("senderId");
        String senderFullName = intent.getExtras().getString("senderFullName");
        String nuntiusId = intent.getExtras().getString("nuntiusId");
        String nuntiusFullName = intent.getExtras().getString("nuntiusFullName");
        String receiverId = intent.getExtras().getString("receiverId");
        String receiverFullName = intent.getExtras().getString("receiverFullName");
        String role = intent.getExtras().getString("role");
        String matchId = intent.getExtras().getString("matchId");
        String date = intent.getExtras().getString("date");

        et_textMessage =  findViewById(R.id.et_textMessage);
        btn_send = findViewById(R.id.btn_send);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd | HH:mm");
        timestamp = simpleDateFormat.format(calendar.getTime()) + " | current timezone";

        if (role.equals("sender")) {
            authorId = senderId;
            authorFullName = senderFullName;
        }
        if (role.equals("nuntius")) {
            authorId = nuntiusId;
            authorFullName = nuntiusFullName;
        }
        if (role.equals("receiver")) {
            authorId = receiverId;
            authorFullName = receiverFullName;
        }

        String finalAuthorId = authorId;
        String finalAuthorFullName = authorFullName;

        fetchMessages(date, matchId, role, mRecyclerView, mAdapter, mLayoutManager);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = et_textMessage.getText().toString();
                if (!message.isEmpty()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("authorId", finalAuthorId);
                    map.put("authorFullName", finalAuthorFullName);
                    map.put("authorRole", role);
                    map.put("message", message);
                    map.put("timestamp", timestamp);

                    new AaaNewPushNotification().chatNotification(senderId, nuntiusId, receiverId, role, message);

                    new EmailMatchClass.EmailMatchFunction(senderId, nuntiusId, receiverId, role, message, finalAuthorFullName);

                    FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            int index = snapshot.getValue(Integer.class);
                            int newIndex = index + 1;
                            String stringIndex = String.valueOf(newIndex);

                            FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(newIndex);

                            FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(newIndex);
                            FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(newIndex);
                            FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(newIndex);







                            FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("chat").child("messages").child(stringIndex).updateChildren(map);

                            FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messages").child(stringIndex).updateChildren(map);
                            FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messages").child(stringIndex).updateChildren(map);
                            FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messages").child(stringIndex).updateChildren(map);

                            et_textMessage.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(item_nuntii_matches_chat_group.this, "Faaaeeen!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

    }

    public void fetchMessages(String date, String matchId, String ownRole, RecyclerView mRecyclerView, RecyclerView.Adapter mAdapter, RecyclerView.LayoutManager mLayoutManager) {

        ArrayList<ChatItemLeft> chatLeftList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("chat").child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatLeftList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String role = snapshot.child("authorRole").getValue().toString();
                    String author = "author";

                    if (role.equals(ownRole)) {
                        author = "You (" + role + ")";
                    } else {
                        author = snapshot.child("authorFullName").getValue().toString() + " (" + role + ")";
                    }

                    String text = snapshot.child("message").getValue().toString();
                    String timestamp = snapshot.child("timestamp").getValue().toString();

                    chatLeftList.add(new ChatItemLeft(author, text, timestamp, role, ownRole));

                }

                RecyclerView fRecyclerView = mRecyclerView;
                RecyclerView.Adapter fAdapter = mAdapter;
                RecyclerView.LayoutManager fLayoutManager = mLayoutManager;

                fRecyclerView = findViewById(R.id.rv_chat);
                fLayoutManager = new LinearLayoutManager(item_nuntii_matches_chat_group.this);
                fAdapter = new ChatLeftAdapter(chatLeftList);
                fRecyclerView.setLayoutManager(fLayoutManager);
                fRecyclerView.setAdapter(fAdapter);
                fRecyclerView.scrollToPosition(chatLeftList.size() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(item_nuntii_matches_chat_group.this, "Database Problem", Toast.LENGTH_SHORT).show();
            }
        });

    }

}