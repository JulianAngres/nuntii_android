package com.nuntiias.nuntiitheone

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseInstanceId {

    fun getInstanceId() {

        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if(result != null){
                var fbToken = result
                // DO your thing with your firebase token
                FirebaseService.token = fbToken

                val ownEmail = FirebaseAuth.getInstance().currentUser!!.email
                val ownNewEmail = ownEmail!!.replace(".", "__DOT__")

                FirebaseDatabase.getInstance().reference.child("userData").child(ownNewEmail).child("messagingToken").setValue(fbToken)
            }
        }
    }

}