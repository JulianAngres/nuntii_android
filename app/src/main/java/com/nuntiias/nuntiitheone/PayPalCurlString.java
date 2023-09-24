package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class PayPalCurlString {

    private String sender_batch_id, email_subject, email_message, recipient_type, value, currency, note, sender_item_id, receiver;

    public PayPalCurlString(String sender_batch_id, String email_subject, String email_message, String recipient_type, String value, String currency, String note, String sender_item_id, String receiver) {
        this.sender_batch_id = sender_batch_id;
        this.email_subject = email_subject;
        this.email_message = email_message;
        this.recipient_type = recipient_type;
        this.value = value;
        this.currency = currency;
        this.note = note;
        this.sender_item_id = sender_item_id;
        this.receiver = receiver;
    }

    public static void CurlString(String sender_batch_id, String email_subject, String email_message, String recipient_type, String value, String currency, String note, String sender_item_id, String receiver) {

        String localDate = ((LocalDate) LocalDate.now()).toString();

        String dataNew = "{\"recipient_type\": \"" + recipient_type + "\",\"amount\": {\"value\": \"" + value + "\",\"currency\": \"" + currency + "\" },\"note\": \"" + note + "\",\"sender_item_id\": \"" + sender_item_id + "\",\"receiver\": \"" + receiver + "\" }, ";

        FirebaseDatabase.getInstance().getReference().child("payoutData").child(localDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String newCurlString = "newCurlString";
                int count = (int) snapshot.getChildrenCount();

                String oldCurlString = snapshot.child("curlString" + String.valueOf(count)).getValue().toString();

                int l = oldCurlString.length();

                if (l > 1000000) {

                    FirebaseDatabase.getInstance().getReference().child("payoutData").child(localDate).child("curlString" + String.valueOf(count + 1)).setValue(newCurlString);
                } else {

                    newCurlString = oldCurlString + dataNew;
                    FirebaseDatabase.getInstance().getReference().child("payoutData").child(localDate).child("curlString" + String.valueOf(count)).setValue(newCurlString);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
