package com.nuntiias.nuntiitheone

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class AaaNewPushNotification {

    val TAG = "PushNotificationLogging"


    fun receptionNotification(nuntiusId: String) {
        FirebaseDatabase.getInstance().reference.child("userData").child(nuntiusId).child("messagingToken")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nuntiusToken = snapshot.value.toString()

                    PushNotification(
                        NotificationNotification("Woohoo!", "The reception of your parcel is confirmed. You can now retrieve your earnings.", "nuntii_jingle.wav"), ///////////////////////// Redundant for Android
                        NotificationData("Woohoo!", "The reception of your parcel is confirmed. You can now retrieve your earnings."),
                        nuntiusToken
                    ).also {
                        sendNotification(it)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }


    fun chatNotification(senderId: String, nuntiusId: String, receiverId: String, role: String, message: String) {
        FirebaseDatabase.getInstance().reference.child("userData").child(senderId).child("messagingToken")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val senderToken = snapshot.value.toString()

                    FirebaseDatabase.getInstance().reference.child("userData").child(nuntiusId).child("messagingToken")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val nuntiusToken = snapshot.value.toString()


                                FirebaseDatabase.getInstance().reference.child("userData").child(receiverId).child("messagingToken")
                                    .addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            val receiverToken = snapshot.value.toString()

                                            if (role != "sender") {
                                                PushNotification(
                                                    NotificationNotification("New Nuntii Message", message, "nuntii_jingle.wav"), ///////////////////////// Redundant for Android
                                                    NotificationData("New Nuntii Message", message),
                                                    senderToken
                                                ).also {
                                                    sendNotification(it)
                                                }
                                            }

                                            if (role != "nuntius") {
                                                PushNotification(
                                                    NotificationNotification("New Nuntii Message", message, "nuntii_jingle.wav"), ///////////////////////// Redundant for Android
                                                    NotificationData("New Nuntii Message", message),
                                                    nuntiusToken
                                                ).also {
                                                    sendNotification(it)
                                                }
                                            }

                                            if (role != "receiver") {
                                                PushNotification(
                                                    NotificationNotification("New Nuntii Message", message, "nuntii_jingle.wav"), ///////////////////////// Redundant for Android
                                                    NotificationData("New Nuntii Message", message),
                                                    receiverToken
                                                ).also {
                                                    sendNotification(it)
                                                }
                                            }

                                        }
                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }



    fun matchNotification(ownNewEmail: String, newEmail: String, id: String, date: String) {

        FirebaseDatabase.getInstance().reference.child("allItineraries").child(date).child(id).child("extra").child("userEmail")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val nuntiusId = snapshot.value.toString()

                    FirebaseDatabase.getInstance().reference.child("userData").child(ownNewEmail).child("messagingToken")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val ownToken = snapshot.value.toString()

                                FirebaseDatabase.getInstance().reference.child("userData").child(newEmail).child("messagingToken")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            val partnerToken = snapshot.value.toString()


                                            FirebaseDatabase.getInstance().reference.child("userData").child(nuntiusId).child("messagingToken")
                                                .addListenerForSingleValueEvent(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {

                                                        val nuntiusToken = snapshot.value.toString()

                                                        PushNotification(
                                                            NotificationNotification("Congratulations!", "You just got a Nuntii match on your journey!", "nuntii_jingle.wav"), ///////////////////////// Redundant for Android
                                                            NotificationData("Congratulations!", "You just got a Nuntii match on your journey!"),
                                                            nuntiusToken
                                                        ).also {
                                                            sendNotification(it)
                                                        }

                                                        PushNotification(
                                                            NotificationNotification("Congratulations!", "You have been pointed out by your partner to be part of a Nuntii match!", "nuntii_jingle.wav"), ///////////////////////// Redundant for Android
                                                            NotificationData("Congratulations!", "You have been pointed out by your partner to be part of a Nuntii match!"),
                                                            partnerToken
                                                        ).also {
                                                            sendNotification(it)
                                                        }

                                                        PushNotification(
                                                            NotificationNotification("Congratulations!", "You just created a Nuntii match!", "nuntii_jingle.wav"), ///////////////////////// Redundant for Android
                                                            NotificationData("Congratulations!", "You just created a Nuntii match!"),
                                                            ownToken
                                                        ).also {
                                                            sendNotification(it)
                                                        }

                                                    }
                                                    override fun onCancelled(error: DatabaseError) {}
                                                })
                                        }
                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

}