package com.nuntiias.nuntiitheone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class RegisterActivity extends AppCompatActivity {

    EditText registerFullName, registerEmail, registerPassword, registerConfPass;
    Button btn_registerUser, btn_gotoLogin;
    FirebaseAuth firebaseAuth;

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
        setContentView(R.layout.activity_register);

        registerFullName = findViewById(R.id.registerFullName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfPass = findViewById(R.id.confPassword);
        btn_registerUser = findViewById(R.id.btn_register);
        btn_gotoLogin = findViewById(R.id.gotoLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        btn_registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // extract the data from the form

                String fullName = registerFullName.getText().toString();
                String email = registerEmail.getText().toString();
                String newEmail = email.replace(".", "__DOT__");
                String password = registerPassword.getText().toString();
                String confPass = registerConfPass.getText().toString();

                if (fullName.isEmpty()) {
                    registerFullName.setError("Full Name is Required");
                    return;
                }
                if (email.isEmpty()) {
                    registerEmail.setError("Email is Required");
                    return;
                }
                if (password.isEmpty()) {
                    registerPassword.setError("Password is Required");
                    return;
                }

                if (!password.equals(confPass)) {
                    registerConfPass.setError("Passwords Do not Match");
                    return;
                }

                // data is validated
                // register the user using firebase

                Toast.makeText(RegisterActivity.this, "Data validated.", Toast.LENGTH_SHORT).show();

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        // See: https://developer.android.com/training/basics/intents/result
                        /*final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                                new FirebaseAuthUIActivityResultContract(),
                                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                                    @Override
                                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                                        //onSignInResult(result);
                                    }
                                }
                        );

                        // Choose authentication providers
                        List<AuthUI.IdpConfig> providers = Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build());

                        // Create and launch sign-in intent
                        Intent signInIntent = AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build();
                        signInLauncher.launch(signInIntent);*/

                        FirebaseDatabase.getInstance().getReference().child("userData").child(newEmail).child("fullName").setValue(fullName);
                        FirebaseDatabase.getInstance().getReference().child("userData").child(newEmail).child("payoutBalance").setValue(0);

                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

}