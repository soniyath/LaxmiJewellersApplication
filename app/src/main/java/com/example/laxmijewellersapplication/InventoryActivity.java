package com.example.laxmijewellersapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class InventoryActivity extends AppCompatActivity {

    private Button sellButton;
    private Button dashboardButton;
    private Button catalogButton;
    private Button inventoryButton;
    private Button reportingButton;

    private Button settingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);


        sellButton = findViewById(R.id.sellButton);
        dashboardButton = findViewById(R.id.dashboardButton);
        catalogButton = findViewById(R.id.catalogButton);
        inventoryButton = findViewById(R.id.inventoryButton);
        reportingButton = findViewById(R.id.reportingButton);
        settingsButton = findViewById(R.id.settingsButton);



        sellButton.setOnClickListener(view -> startActivity(new Intent(InventoryActivity.this, SellActivity.class)));
        dashboardButton.setOnClickListener(view -> startActivity(new Intent(InventoryActivity.this, DashboardActivity.class)));
        catalogButton.setOnClickListener(view -> startActivity(new Intent(InventoryActivity.this, CatalogActivity.class)));
        inventoryButton.setOnClickListener(view -> startActivity(new Intent(InventoryActivity.this, InventoryActivity.class)));
        reportingButton.setOnClickListener(view -> startActivity(new Intent(InventoryActivity.this, ReportingActivity.class)));
        settingsButton.setOnClickListener(view -> startActivity(new Intent(InventoryActivity.this, SettingsActivity.class)));
    }
}