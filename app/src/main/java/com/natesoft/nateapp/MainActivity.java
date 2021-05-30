package com.natesoft.nateapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendConfirmation(View view) {
        Toast toast = Toast.makeText(this, R.string.send_confirmation, Toast.LENGTH_SHORT);
        toast.show();
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