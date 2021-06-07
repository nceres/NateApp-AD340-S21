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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    private EditText userField;
    private EditText emailField;
    private EditText passwordField;
    SharedPreferences sharedPref;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userField = findViewById(R.id.userName);
        emailField = findViewById(R.id.emailAddress);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Log.d("FIREBASE", "signIn");
        // 1 - validate display name, email, and password entries
        String user = userField.getText().toString();
        String email = emailField.getText().toString();
        String pwd = passwordField.getText().toString();
        if (!checkForm(user, email, pwd)) {
             return;
        }

        // 2 - save valid entries to shared preferences
        sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userName", user);
        editor.putString("email", email);
        editor.putString("password", pwd);
        editor.apply();

        // 3 - sign into Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(sharedPref.getString("email", "defaultValue"), sharedPref.getString("password", "defaultValue"))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            // update profile. displayname is the value entered in UI
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(sharedPref.getString("userName", "defaultValue"))
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
                });
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