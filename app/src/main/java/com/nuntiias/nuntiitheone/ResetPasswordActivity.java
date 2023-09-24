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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText et_newUserPassword, et_confirmNewUserPassword;
    private Button btn_saveNewUserPassword;
    private FirebaseUser user;

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
        setContentView(R.layout.activity_reset_password);

        et_newUserPassword = findViewById(R.id.et_newUserPassword);
        et_confirmNewUserPassword = findViewById(R.id.et_confirmNewUserPassword);
        btn_saveNewUserPassword = findViewById(R.id.btn_saveNewUserPassword);

        user = FirebaseAuth.getInstance().getCurrentUser();

        btn_saveNewUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_newUserPassword.getText().toString().isEmpty()) {
                    et_newUserPassword.setError("Required Field");
                    return;
                }

                if (et_confirmNewUserPassword.getText().toString().isEmpty()) {
                    et_confirmNewUserPassword.setError("Required Field");
                    return;
                }

                if (!et_newUserPassword.getText().toString().equals(et_confirmNewUserPassword.getText().toString())) {
                    et_confirmNewUserPassword.setError("Passwords Do not Match");
                    return;
                }

                user.updatePassword(et_newUserPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetPasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }
}