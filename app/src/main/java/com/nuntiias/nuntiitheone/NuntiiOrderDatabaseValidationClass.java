package com.nuntiias.nuntiitheone;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class NuntiiOrderDatabaseValidationClass {


    public static void NuntiiOrderDatabaseValidation(String userEmail, String partnerEmail, boolean sender, String itineraryId, String parcelSize, String parcelDescription, String price, String date, int eigenPreis) {

        String originalSenderId = "senderId";
        String originalReceiverId = "receiverId";

        if (sender) {
            originalSenderId = userEmail;
            originalReceiverId = partnerEmail;
        } else {
            originalSenderId = partnerEmail;
            originalReceiverId = userEmail;
        }

        String senderId = originalSenderId;
        String receiverId = originalReceiverId;

        Log.d("date", date);
        Log.d("itineraryId", itineraryId);

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

                                        saveOriginDestination(date, itineraryId, matchId, senderId, nuntiusId, receiverId, userEmail, price, String.valueOf(eigenPreis));




                                        FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(0);

                                        FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(0);
                                        FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(0);
                                        FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("chat").child("messageCount").setValue(0);



                                        FirebaseDatabase.getInstance().getReference().child("nuntiiMatches").child(date).child(matchId).child("confirmed").setValue("false");

                                        FirebaseDatabase.getInstance().getReference().child("userData").child(senderId).child("nuntiiMatches").child(date).child(matchId).child("confirmed").setValue("false");
                                        FirebaseDatabase.getInstance().getReference().child("userData").child(nuntiusId).child("nuntiiMatches").child(date).child(matchId).child("confirmed").setValue("false");
                                        FirebaseDatabase.getInstance().getReference().child("userData").child(receiverId).child("nuntiiMatches").child(date).child(matchId).child("confirmed").setValue("false");


                                        emailMatch(userEmail)
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Exception e = task.getException();
                                                            if (e instanceof FirebaseFunctionsException) {
                                                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                                                FirebaseFunctionsException.Code code = ffe.getCode();
                                                                Object details = ffe.getDetails();
                                                            }
                                                        }
                                                    }
                                                });

                                        emailMatch(partnerEmail)
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Exception e = task.getException();
                                                            if (e instanceof FirebaseFunctionsException) {
                                                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                                                FirebaseFunctionsException.Code code = ffe.getCode();
                                                                Object details = ffe.getDetails();
                                                            }
                                                        }
                                                    }
                                                });

                                        emailMatch(nuntiusId)
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Exception e = task.getException();
                                                            if (e instanceof FirebaseFunctionsException) {
                                                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                                                FirebaseFunctionsException.Code code = ffe.getCode();
                                                                Object details = ffe.getDetails();
                                                            }
                                                        }
                                                    }
                                                });

                                        new AaaNewPushNotification().matchNotification(userEmail, partnerEmail, matchId, date);


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

    public static void saveOriginDestination(String date, String itineraryId, String matchId, String senderId, String nuntiusId, String receiverId, String userEmail, String amount, String eigenPreis) {

        FirebaseDatabase.getInstance().getReference().child("allItineraries").child(date).child(itineraryId).child("legs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                String lastIndex = String.valueOf(count-1);

                String itineraryOrigin = snapshot.child("0").child("iataOrigin").getValue().toString();
                String itineraryDestination = snapshot.child(lastIndex).child("iataDestination").getValue().toString();



                emailBill(userEmail, eigenPreis, itineraryOrigin, itineraryDestination)
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        Object details = ffe.getDetails();
                                    }
                                }
                            }
                        });

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

    private static Task<String> emailMatch(String recipientRaw) {

        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

        String recipient = recipientRaw.replace("__DOT__", ".");

        Map<String, Object> data = new HashMap<>();
        data.put("subject", "subject");
        data.put("text", "text");
        data.put("recipient", recipient);

        return mFunctions
                .getHttpsCallable("emailMatch")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });

    }

    private static Task<String> emailBill(String recipientRaw, String amount, String origin, String destination) {

        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

        String recipient = recipientRaw.replace("__DOT__", ".");

        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        data.put("origin", origin);
        data.put("destination", destination);
        data.put("recipient", recipient);

        return mFunctions
                .getHttpsCallable("emailBill")
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
