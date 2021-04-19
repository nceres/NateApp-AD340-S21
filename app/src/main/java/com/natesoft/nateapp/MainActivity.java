package com.natesoft.nateapp;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // based on Android Fundamentals tutorial "Your first interactive UI"
    public void sendConfirmation(View view) {
        Toast toast = Toast.makeText(this, R.string.send_confirmation,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMessageOne(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_one,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMessageTwo(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_two,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMessageThree(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_three,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMessageFour(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_four,
                Toast.LENGTH_SHORT);
        toast.show();
    }

}