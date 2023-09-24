package com.nuntiias.nuntiitheone

import androidx.appcompat.app.AppCompatActivity
import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import com.nuntiias.nuntiitheone.R

class item_nuntii_matches_problem : AppCompatActivity() {
    var networkChangeListener = NetworkChangeListener()
    override fun onStart() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, filter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_nuntii_matches_problem)
    }
}