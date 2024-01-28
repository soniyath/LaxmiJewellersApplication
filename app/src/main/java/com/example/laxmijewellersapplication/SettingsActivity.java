package com.example.laxmijewellersapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class SettingsActivity extends AppCompatActivity {
    private Button sellButton;
    private Button dashboardButton;
    private Button catalogButton;
    private Button inventoryButton;
    private Button reportingButton;

    private Button settingsButton;

    private Button addUserButton;

    private Button logOutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference storageReference;

    private FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        logOutButton = findViewById(R.id.logOutButton);
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

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Signout button
                if(user != null && firebaseAuth != null){
                    firebaseAuth.signOut();
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    Toast.makeText(SettingsActivity.this, "Log Out Successful", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SettingsActivity.this, "No User Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        });

     }
}