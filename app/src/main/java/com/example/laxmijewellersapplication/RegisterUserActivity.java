package com.example.laxmijewellersapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterUserActivity extends AppCompatActivity {


    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText reenterPasswordEditText;
    private Button registerButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;


    //Firestore Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordInput);
        reenterPasswordEditText = findViewById(R.id.reenterPasswordInput);
        registerButton = findViewById(R.id.registerButton);

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

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(emailEditText.getText().toString().trim())
                        && !TextUtils.isEmpty(passwordEditText.getText().toString().trim())
                        && !TextUtils.isEmpty(usernameEditText.getText().toString().trim())
                        && !TextUtils.isEmpty(passwordEditText.getText().toString().trim())){

                    String email = emailEditText.getText().toString().trim();
                    String username = usernameEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String reenterPassword = reenterPasswordEditText.getText().toString().trim();

                    if(passwordValidation(password, reenterPassword)) {
                        createAdminEmailAccount(email, password, username);
                    }
                    else{
                        //Give an error to the user about password not being apt
                        Toast.makeText(RegisterUserActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(RegisterUserActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }     startActivity(new Intent(RegisterUserActivity.this, SettingsActivity.class));
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
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", username);
                                userObj.put("email", email);
                                userObj.put("role", "employee");


                                //save to our firestore database
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(Objects.requireNonNull(task.getResult().exists())){
                                                                    String name = task.getResult()
                                                                            .getString("username");
                                                                    Intent intent = new Intent(RegisterUserActivity.this, SettingsActivity.class);
                                                                    intent.putExtra("username", name);
                                                                    intent.putExtra("userId", currentUserId);
                                                                    startActivity(intent);

                                                                }else{


                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });



                            }
                            else{
                                //something went wrong
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        else{

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