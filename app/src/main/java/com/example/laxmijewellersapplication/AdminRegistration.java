package com.example.laxmijewellersapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Button;

public class AdminRegistration extends AppCompatActivity {

    private Button adminRegistrationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);


        adminRegistrationButton = findViewById(R.id.adminRegistrationButton);
        adminRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminRegistration.this, DashboardActivity.class));
            }
        });

    }
}