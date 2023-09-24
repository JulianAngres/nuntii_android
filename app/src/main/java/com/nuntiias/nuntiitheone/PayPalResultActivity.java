package com.nuntiias.nuntiitheone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PayPalResultActivity extends AppCompatActivity {

    private Button btn_backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_result);

        btn_backToMain = findViewById(R.id.btn_backToMain);

        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PayPalResultActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}