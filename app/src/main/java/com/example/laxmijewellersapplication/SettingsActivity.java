package com.example.laxmijewellersapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.Collection;
import java.util.Queue;

public class SettingsActivity extends AppCompatActivity {
    private Button sellButton;
    private Button dashboardButton;
    private Button catalogButton;
    private Button inventoryButton;
    private Button reportingButton;

    private Button settingsButton;

    private Button addUserButton;

    private Button logOutButton;
    private Intent receivedIntent;

    private FirebaseFirestore firebaseFirestore;

    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference storageReference;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    private TextView currentUsername;
    private TextView currentUserRole;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //All Buttons currenly invisible
        logOutButton = findViewById(R.id.logOutButton);
        sellButton = findViewById(R.id.sellButton);
        dashboardButton = findViewById(R.id.dashboardButton);
        catalogButton = findViewById(R.id.catalogButton);
        inventoryButton = findViewById(R.id.inventoryButton);
        reportingButton = findViewById(R.id.reportingButton);
        settingsButton = findViewById(R.id.settingsButton);


        //Username and Role Display
        currentUsername = findViewById(R.id.currentUsername);
        currentUserRole = findViewById(R.id.currentUserRole);


        //Add User for Regitration button
        addUserButton = findViewById(R.id.addUserButton);

        sellButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, SellActivity.class)));
        dashboardButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, DashboardActivity.class)));
        catalogButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, CatalogActivity.class)));
        inventoryButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, InventoryActivity.class)));
        reportingButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, ReportingActivity.class)));
        settingsButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, SettingsActivity.class)));


        //User Registration
        addUserButton.setOnClickListener(view -> startActivity(new Intent(SettingsActivity.this, RegisterUserActivity.class)));

        //Firesbase basic calls
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        //Collection Reference for both the collections
        CollectionReference userCollection = db.collection("Users");
        CollectionReference adminCollection = db.collection("Admin");




       if(currentUser != null){
           String uid = currentUser.getUid();
           String email = currentUser.getEmail();
           //Do Something with the user information

           //Query to find the user with the specified email
           Query query = adminCollection.whereEqualTo("email", email);

           query.get().addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   QuerySnapshot querySnapshot = task.getResult();
                   if(querySnapshot != null && !querySnapshot.isEmpty()){
                       //Email Exists in "Admin" Collection
                       currentUsername.setText("Username: " +email);
                       currentUserRole.setText("Role: Admin");
                   }
                   else{
                       //Email does not exists in the 'Admin' Collection
                       currentUsername.setText("Username: " +email);
                       currentUserRole.setText("Role: Employee");
                   }
               }
               else{
                   Exception exception = task.getException();
                   if(exception != null){
                       Toast.makeText( SettingsActivity.this, "User Privilege Check Failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
           });

       }
       else{
           //User is not signed in, handle this case as needed
           Toast.makeText(SettingsActivity.this, "No User Signed In", Toast.LENGTH_SHORT).show();
       }


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Signout button
                if (currentUser != null && firebaseAuth != null) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    Toast.makeText(SettingsActivity.this, "Log Out Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "No User Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}