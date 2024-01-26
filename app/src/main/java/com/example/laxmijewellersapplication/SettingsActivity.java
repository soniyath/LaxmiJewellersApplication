package com.example.laxmijewellersapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {
    private Button sellButton;
    private Button dashboardButton;
    private Button catalogButton;
    private Button inventoryButton;
    private Button reportingButton;

    private Button settingsButton;

    private Button addUserButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        sellButton = findViewById(R.id.sellButton);
        dashboardButton = findViewById(R.id.dashboardButton);
        catalogButton = findViewById(R.id.catalogButton);
        inventoryButton = findViewById(R.id.inventoryButton);
        reportingButton = findViewById(R.id.reportingButton);
        settingsButton = findViewById(R.id.settingsButton);

        addUserButton = findViewById(R.id.addUserButton);



        sellButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, SellActivity.class)));
        dashboardButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, DashboardActivity.class)));
        catalogButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, CatalogActivity.class)));
        inventoryButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, InventoryActivity.class)));
        reportingButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, ReportingActivity.class)));
        settingsButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, SettingsActivity.class)));

        addUserButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, RegisterUserActivity.class)));
    }
}