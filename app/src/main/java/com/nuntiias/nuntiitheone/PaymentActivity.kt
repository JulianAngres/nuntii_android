package com.nuntiias.nuntiitheone

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.BillingAddressFields
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_payment.*
import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener

class PaymentActivity : AppCompatActivity() {
    private var currentUser: FirebaseUser? = null
    private lateinit var paymentSession: PaymentSession
    private lateinit var paymentAuthConfig: PaymentAuthConfig
    private lateinit var selectedPaymentMethod: PaymentMethod
    private lateinit var progressDialog: ProgressDialog
    private val stripe: Stripe by lazy { Stripe(applicationContext, PaymentConfiguration.getInstance(applicationContext).publishableKey) }

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
        setContentView(R.layout.activity_payment)

        val ownNewEmail = intent.getStringExtra("ownNewEmail")
        val newEmail = intent.getStringExtra("newEmail")
        val sender = intent.getBooleanExtra("sender", true)
        val id = intent.getStringExtra("id")
        val size = intent.getStringExtra("size")
        val description = intent.getStringExtra("description")
        var price = intent.getStringExtra("price")
        val date = intent.getStringExtra("date")
        var eigenPreis = intent.getStringExtra("eigenPreis")
        var decentraPriceFloat = 0.0


        if (price != null) {
            price = price.dropLast(3)
            price = price.replace(',', '.')

            Toast.makeText(applicationContext, "Booking Will Be Successful even if App Crashes", Toast.LENGTH_LONG).show()
            val priceFloat = eigenPreis?.toFloat()?.toDouble()
            if (priceFloat != null) {
                (priceFloat * 100).toInt().also { val priceStripe = it }
                decentraPriceFloat = price.toFloat().toDouble() - priceFloat
            }

            payButton.setOnClickListener {
                if (ownNewEmail != null) {
                    if (newEmail != null) {
                        if (id != null) {
                            if (size != null) {
                                if (description != null) {
                                    if (date != null) {
                                        if (priceFloat != null) {
                                            confirmPayment(
                                                selectedPaymentMethod.id!!,
                                                (priceFloat * 100).toInt(),
                                                ownNewEmail,
                                                newEmail,
                                                sender,
                                                id,
                                                size,
                                                description,
                                                price,
                                                date
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        if (eigenPreis != null) {
            showUI(eigenPreis, decentraPriceFloat.toString())
        }

        paymentmethod.setOnClickListener {
            // Create the customer session and kick start the payment flow
            paymentSession.presentPaymentMethodSelection()

            if (price != null) {
                showUI(price, decentraPriceFloat.toString())
            }


        }
    }


    private fun confirmPayment(paymentMethodId: String, priceStripe: Int, ownNewEmail: String, newEmail: String, sender: Boolean, id: String, size: String, description: String, price: String, date: String) {
        payButton.isEnabled = false
        progressDialog = ProgressDialog(this)
        progressDialog.show()
        progressDialog.setContentView(R.layout.show_dialog)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        currentUser = FirebaseAuth.getInstance().currentUser

        val paymentCollection = Firebase.firestore
            .collection("stripe_customers").document(currentUser?.uid?:"")
            .collection("payments")

        // Add a new document with a generated ID
        paymentCollection.add(hashMapOf(
            "amount" to priceStripe,
            "currency" to "nok"
        ))
            .addOnSuccessListener { documentReference ->
                Log.d("payment", "DocumentSnapshot added with ID: ${documentReference.id}")
                documentReference.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("payment", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("payment", "Current data: ${snapshot.data}")
                        val clientSecret = snapshot.data?.get("client_secret")
                        Log.d("payment", "Create paymentIntent returns $clientSecret")
                        clientSecret?.let {

                            NuntiiOrderDatabaseValidationClass.NuntiiOrderDatabaseValidation(ownNewEmail, newEmail, sender, id, size, description, price, date, priceStripe/100)


                            stripe.confirmPayment(this, ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                paymentMethodId,
                                (it as String)
                            ))



                            Log.w("Before PaymentAuthConfig", "Danke")

                            val uiCustomization = PaymentAuthConfig.Stripe3ds2UiCustomization.Builder()
                                .setLabelCustomization(
                                    PaymentAuthConfig.Stripe3ds2LabelCustomization.Builder()
                                        .setTextFontSize(12)
                                        .build()
                                )
                                .build()

                            PaymentAuthConfig.init(
                                PaymentAuthConfig.Builder()
                                    .set3ds2Config(
                                        PaymentAuthConfig.Stripe3ds2Config.Builder()
                                            .setTimeout(5)
                                            .setUiCustomization(uiCustomization)
                                            .build(),
                                    )
                                    .build(),
                            )
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Log.e("payment", "Current payment intent : null")
                        payButton.isEnabled = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("payment", "Error adding document", e)
                payButton.isEnabled = true
            }
    }

    @SuppressLint("SetTextI18n")
    private fun showUI(price: String, decentraPrice: String) {
        greeting.visibility = View.VISIBLE
        checkoutSummary.visibility = View.VISIBLE
        //payButton.visibility = View.VISIBLE
        paymentmethod.visibility = View.VISIBLE

        greeting.text = "Hello"
        checkoutSummary.text = "Please Pay $price NOK now, pay $decentraPrice later"

        setupPaymentSession()
    }

    private fun setupPaymentSession () {


        FirebaseDatabase.getInstance().reference.child("stripePk")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val stripePk = snapshot.value.toString()
                    setUpPaymentSessionFunction(stripePk)
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }

    private fun setUpPaymentSessionFunction(stripePk: String) {
        PaymentConfiguration.init(applicationContext, stripePk)
        // Setup Customer Session
        CustomerSession.initCustomerSession(this, FirebaseEphemeralKeyProvider())
        // Setup a payment session
        paymentSession = PaymentSession(this, PaymentSessionConfig.Builder()
            .setShippingInfoRequired(false)
            .setShippingMethodsRequired(false)
            .setBillingAddressFields(BillingAddressFields.None)
            .setShouldShowGooglePay(false)
            .build())

        val uiCustomization = PaymentAuthConfig.Stripe3ds2UiCustomization.Builder()
            .setLabelCustomization(
                PaymentAuthConfig.Stripe3ds2LabelCustomization.Builder()
                    .setTextFontSize(12)
                    .build()
            )
            .build()
        PaymentAuthConfig.init(
            PaymentAuthConfig.Builder()
                .set3ds2Config(
                    PaymentAuthConfig.Stripe3ds2Config.Builder()
                        .setTimeout(5)
                        .setUiCustomization(uiCustomization)
                        .build()
                )
                .build()
        )

        paymentSession.init(
            object: PaymentSession.PaymentSessionListener {
                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
                    Log.d("PaymentSession", "PaymentSession has changed: $data")
                    Log.d("PaymentSession", "${data.isPaymentReadyToCharge} <> ${data.paymentMethod}")

                    if (data.isPaymentReadyToCharge) {
                        Log.d("PaymentSession", "Ready to charge");
                        payButton.isEnabled = true

                        data.paymentMethod?.let {
                            //Log.d("PaymentSession", "PaymentMethod $it selected")
                            paymentmethod.text = "${it.card?.brand} card ends with ${it.card?.last4}"
                            payButton.visibility = View.VISIBLE
                            //Log.d("text", "${it.card?.brand} card ends with ${it.card?.last4}")
                            selectedPaymentMethod = it



                        }
                    }
                }

                override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
                    Log.d("PaymentSession",  "isCommunicating $isCommunicating")
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    Log.e("PaymentSession",  "onError: $errorCode, $errorMessage")
                }

            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intent = Intent(this, PaymentActivity::class.java)

        if (data != null) {
            var resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == RESULT_OK) {
                        // There are no request codes
                        val data: Intent? = result.data
                        //paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
                    }
                }
            resultLauncher.launch(intent)
            paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
        }


    }


}


