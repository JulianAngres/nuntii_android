package com.nuntiias.nuntiitheone;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class PayPalActivity extends AppCompatActivity {

    private EditText et_email_phone;
    private String sender_batch_id, email_subject, email_message, recipient_type, value, currency, note, sender_item_id, receiver;
    private Button btn_payouts;
    private TextView tv_amountPayout;

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
        setContentView(R.layout.activity_pay_pal);
        btn_payouts = findViewById(R.id.btn_payout);
        tv_amountPayout = findViewById(R.id.tv_amountPayout);

        et_email_phone = findViewById(R.id.et_email_phone);

        String emailRaw = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String email = emailRaw.replace(".", "__DOT__");

        FirebaseDatabase.getInstance().getReference().child("userData").child(email).child("payoutBalance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                Double getPayoutBalance = snapshot.getValue(Double.class);
                String deductedPriceRaw = String.valueOf(getPayoutBalance);

                if (deductedPriceRaw.equals("0.0")) {

                    tv_amountPayout.setText("Nothing to pay out currently ;-)");
                    btn_payouts.setVisibility(View.INVISIBLE);

                } else {

                    String cents = "cents";
                    String dollars = "dollars";
                    String cutOffCents = "cutOffCents";

                    if (deductedPriceRaw.contains(".")) {
                        String[] splitDeductedPrice = deductedPriceRaw.split("\\.");
                        dollars = splitDeductedPrice[0];
                        cents = splitDeductedPrice[1];

                        if (cents.length() == 1) {
                            cutOffCents = cents + "0";
                        } else {
                            cutOffCents = cents.substring(0, 2);
                        }

                    } else {
                        dollars = deductedPriceRaw;
                        cutOffCents = "00";
                    }

                    value = dollars + "." + cutOffCents;

                    tv_amountPayout.setText("You deserved yourself " + value + " NOK");

                    btn_payouts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (et_email_phone.getText().toString().isEmpty()) {
                                Toast.makeText(PayPalActivity.this, "Please enter your Vipps phone number", Toast.LENGTH_SHORT).show();
                            } else if (et_email_phone.getText().toString().length() != 8) {
                                Toast.makeText(PayPalActivity.this, "Please enter your 8-digit Norwegian Vipps phone number without blank spaces", Toast.LENGTH_SHORT).show();
                            } else {
                                receiver = et_email_phone.getText().toString();
                                if (receiver.contains("@")) {
                                    recipient_type = "EMAIL";
                                } else {
                                    recipient_type = "PHONE";
                                }

                                FirebaseDatabase.getInstance().getReference().child("payoutCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        int id = snapshot.getValue(Integer.class) + 1;
                                        FirebaseDatabase.getInstance().getReference().child("payoutCount").setValue(id);
                                        String stringId = "Payout" + String.valueOf(id);

                                        sender_batch_id = stringId;
                                        email_subject = "Congrats! You Received a Nuntii Payout!";
                                        email_message = "It's so great that you are a part of our vision!";
                                        currency = "NOK";
                                        note = "Thank you for being a Nuntius!";
                                        sender_item_id = stringId;

                                        //new NetworkAsyncTask(sender_batch_id, email_subject, email_message, recipient_type, value, currency, note, sender_item_id, receiver).execute();
                                        PayPalCurlString.CurlString(sender_batch_id, email_subject, email_message, recipient_type, value, currency, note, sender_item_id, receiver);

                                        FirebaseDatabase.getInstance().getReference().child("userData").child(email).child("payoutBalance").setValue(0);

                                        startActivity(new Intent(PayPalActivity.this, PayPalResultActivity.class));
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }


                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }
}