package com.example.laxmijewellersapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.K;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminRegistration extends AppCompatActivity {

    private Button adminRegistrationButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private ProgressBar progressBar;


    //Firestore Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Admin");
    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;

    private TextInputEditText reEnterPasswordEditText;

    private ImageView passwordHelperIcon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);


        //Binding everything with the id
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordInput);
        reEnterPasswordEditText = findViewById(R.id.reenterPasswordInput);
        adminRegistrationButton = findViewById(R.id.adminRegistrationButton);
        passwordHelperIcon = findViewById(R.id.passwordHelperIcon);
        progressBar = findViewById(R.id.progressBar);


        //Password Helper Method
        passwordHelperIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.password_helper_chart, null);
                Dialog passwordHelperDialog = new Dialog(AdminRegistration.this);
                passwordHelperDialog.setContentView(dialogView);
                Button closeButton = dialogView.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        passwordHelperDialog.dismiss();
                    }
                });
                passwordHelperDialog.show();
            }
        });

        //Firebase instantiation
        firebaseAuth = FirebaseAuth.getInstance();


        //Checks what user is logged in
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){
                    //user is logged in...
                }
                else{
                    //no user yet...
                }
            }
        };

        adminRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                if(!TextUtils.isEmpty(emailEditText.getText().toString().trim())
                && !TextUtils.isEmpty(passwordEditText.getText().toString().trim())
                && !TextUtils.isEmpty(usernameEditText.getText().toString().trim())
                && !TextUtils.isEmpty(passwordEditText.getText().toString().trim())
                && !TextUtils.isEmpty(reEnterPasswordEditText.getText().toString().trim())) {

                    String email = emailEditText.getText().toString().trim();
                    String username = usernameEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String reenterPassword = reEnterPasswordEditText.getText().toString().trim();

                    if (passwordValidation(password, reenterPassword)) {
                        createAdminEmailAccount(email, password, username);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        //Give an error to the user about password not being apt
                        if (password.equals(reenterPassword)) {
                            Toast.makeText(AdminRegistration.this, "Invalid Password: Please check required format.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AdminRegistration.this, "Passwords are not identical", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else{

                    Toast.makeText(AdminRegistration.this, "One or more fields empty", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    private boolean passwordValidation(String password, String reenterPassword){
        // Minimum length of 8 characters
        // At least one digit (\d) and one special character (!@#$%^&*()_-+=<>?/\|[]{})
        String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+=<>?/\\|\\[\\]{}])(?=\\S+$).{8,}$";
        return (reenterPassword.matches(passwordPattern) && password.matches(passwordPattern) && reenterPassword.equals(password));
    }


    //Creating the method where email password and username are stored in our Firebase Authentication cloud DB
    private void createAdminEmailAccount(String email, String password, String username){
        if(!TextUtils.isEmpty(email)
        && !TextUtils.isEmpty(password)
        && !TextUtils.isEmpty(username)){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //we take user to settings activity -- Dashboard after we finish this sub app
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserId = currentUser.getUid();

                                //Creating a map for storing all the users in the Firebase database along with the
                                //authentication panel
                                Map<String, String> adminObj = new HashMap<>();
                                adminObj.put("userId", currentUserId);
                                adminObj.put("username", username);
                                adminObj.put("email", email);
                                adminObj.put("role", "admin");


                                //save to our firestore database
                               collectionReference.add(adminObj)
                                       .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                           @Override
                                           public void onSuccess(DocumentReference documentReference) {
                                               progressBar.setVisibility(View.GONE);
                                               documentReference.get()
                                                       .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                               if (Objects.requireNonNull(task.getResult().exists())) {
                                                                   String name = task.getResult()
                                                                           .getString("username");
                                                                   Intent intent = new Intent(AdminRegistration.this, SettingsActivity.class);
                                                                   intent.putExtra("username", name);
                                                                   intent.putExtra("userId", currentUserId);
                                                                   startActivity(intent);

                                                               } else {
                                                                   Toast.makeText(AdminRegistration.this, "Object Null Referenced!", Toast.LENGTH_LONG).show();

                                                               }
                                                           }
                                                       })
                                                       .addOnFailureListener(new OnFailureListener() {
                                                           @Override
                                                           public void onFailure(@NonNull Exception e) {
                                                               progressBar.setVisibility(View.GONE);
                                                               Toast.makeText(AdminRegistration.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();

                                                           }
                                                       });
                                           }
                                       });



                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AdminRegistration.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    //Before on create is called we need our Firebase stuff ready

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}