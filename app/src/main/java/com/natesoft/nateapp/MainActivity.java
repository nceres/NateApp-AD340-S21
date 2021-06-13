package com.natesoft.nateapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    Button loginButton;
    private EditText userField;
    private EditText emailField;
    private EditText passwordField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userField = findViewById(R.id.userName);
        emailField = findViewById(R.id.emailAddress);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);

        sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        userField.setText(getEntry("userName"));
        emailField.setText(getEntry("email"));
        passwordField.setText(getEntry("password"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    public void signIn() {
        Log.d("FIREBASE", "signIn");
        // 1 - validate display name, email, and password entries
        String userName = userField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (!checkForm(userName, email, password)) {
             return;
        }
        // 2 - save valid entries to shared preferences
        saveEntry("userName", userName);
        saveEntry("email", email);
        saveEntry("password", password);
        // 3 - sign into Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            // update profile. displayname is the value entered in UI
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();
                            assert user != null;
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("FIREBASE", "User profile updated.");
                                                // Go to FirebaseActivity
                                                startActivity(new Intent(MainActivity.this, FirebaseActivity.class));
                                            }
                                        }
                                    });
                        } else {
                            Log.d("FIREBASE", "sign-in failed");
                            Toast.makeText(MainActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Log.d("FIREBASE", "FirebaseAuthInvalidCredentialsException");
                        } else if (e instanceof FirebaseAuthInvalidUserException) {
                            String errorCode =
                                    ((FirebaseAuthInvalidUserException) e).getErrorCode();
                            if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                                Log.d("FIREBASE", "ERROR_USER_NOT_FOUND");
                            } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                                Log.d("FIREBASE", "ERROR_USER_DISABLED");
                            } else {
                                Log.d("FIREBASE", "OTHER_ERROR");
                            }
                        }

                    }
                });

    }

    public void saveEntry(String key, String message) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, message);
        editor.commit();
    }

    public String getEntry(String key) {
        return sharedPref.getString(key, "");
    }

    private boolean checkForm(String userName, String email, String password) {
        boolean checked = true;
        if (TextUtils.isEmpty(userName)) {
            userField.setError("Required");
            checked = false;
        } else {
            userField.setError(null);
        }
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required");
            checked = false;
        } else {
            emailField.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required");
            checked = false;
        } else {
            passwordField.setError(null);
        }
        return checked;
    }

    public void runMovies(View view) {
        Intent intent = new Intent(this, Movies.class);
        startActivity(intent);
    }

    public void runTrafficCams(View view) {
        Intent intent = new Intent(this, TrafficCameras.class);
        startActivity(intent);
    }

    public void runTrafficCamMap(View view) {
        Intent intent = new Intent(this, TrafficCamMap.class);
        startActivity(intent);
    }

    public void showMessageFour(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_four, Toast.LENGTH_SHORT);
        toast.show();
    }

}