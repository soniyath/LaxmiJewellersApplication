package com.example.laxmijewellersapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


        Button sellButton = findViewById(R.id.sellButton);
        Button dashboardButton = findViewById(R.id.dashboardButton);
        Button catalogButton = findViewById(R.id.catalogButton);
        Button reportingButton = findViewById(R.id.reportingButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        Button inventoryButton = findViewById(R.id.inventoryButton);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageView menu = findViewById(R.id.hamburger_icon);



    }

}