package com.example.laxmijewellersapplication;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView forgotPassword;
    private LinearLayout adminRegistration;

    private EditText emailInput;

    private EditText passwordInput;

    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPasswordTextView);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);


        firebaseAuth = firebaseAuth.getInstance();

        //Checks what user is logged in
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    //user is logged in...
                } else {
                    //no user yet...
                }
            }
        };

        //Once Logged in
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pasted from Register User
                if (!TextUtils.isEmpty(emailInput.getText().toString().trim())
                        && !TextUtils.isEmpty(passwordInput.getText().toString().trim())) {
                    signInUser(emailInput.getText().toString().trim(), passwordInput.getText().toString().trim());
                } else {
                    showToast("Authentication failed: One of the fields are empty");                }
            }
        });
    }

     public void signInUser(String emailInput, String passwordInput) {
         firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             showToast("Sign in successful!");
                             startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                         } else {
                             showToast("Authentication failed." + task.getException().getMessage());
                         }

                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         showToast("Authentication failed." + e.getMessage());
                     }
                 });
     }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void retrieveAccount(View view){
        startActivity(new Intent( MainActivity.this, RetrieveAccountActivity.class));
    }

}
