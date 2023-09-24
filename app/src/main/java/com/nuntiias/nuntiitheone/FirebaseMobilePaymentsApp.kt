package com.nuntiias.nuntiitheone

import android.app.Application
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stripe.android.PaymentConfiguration

class FirebaseMobilePaymentsApp : Application(){
    override fun onCreate() {
        super.onCreate()


        FirebaseDatabase.getInstance().reference.child("stripePk")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val stripePk = snapshot.value.toString()
                    PaymentConfiguration.init(applicationContext, stripePk)
                }

                override fun onCancelled(error: DatabaseError) {}
            })




    }
}