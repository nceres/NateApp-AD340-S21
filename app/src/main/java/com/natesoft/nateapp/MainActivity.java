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

    public void showMessageTwo(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_two, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMessageThree(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_three, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMessageFour(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_four, Toast.LENGTH_SHORT);
        toast.show();
    }

}