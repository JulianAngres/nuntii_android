package com.nuntiias.nuntiitheone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.nuntiias.nuntiitheone.Utility.NetworkChangeListener;

public class SettingsActivity extends AppCompatActivity {

    private Button btn_deleteUser, btn_logout, btn_resetPassword;
    private TextView tv_userId;

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
        setContentView(R.layout.activity_settings2);

        btn_deleteUser = findViewById(R.id.btn_deleteUser);
        btn_logout = findViewById(R.id.btn_logout);
        btn_resetPassword = findViewById(R.id.btn_resetPassword);
        tv_userId = findViewById(R.id.tv_userId);

        String ownEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String ownNewEmail = ownEmail.replace(".", "__DOT__");

        tv_userId.setText(ownEmail);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
            }
        });

        btn_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, ResetPasswordActivity.class));
            }
        });

        btn_deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Account Deletion");
                builder.setMessage("Are you sure you want to delete your account?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //FirebaseDatabase.getInstance().getReference().child("userData").child(ownNewEmail).setValue(null);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete();
                        startActivity(new Intent(SettingsActivity.this, RegisterActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();








            }
        });

    }

}